//! FFMPEG's AV Decoder

use ::core::ffi::c_void;
use ::core::ops::{Deref, DerefMut};
use ::core::{ptr, slice};
use c_str_macro::c_str;
use ffmpeg_sys_next::*;
use libc::c_int;
use log::*;
use nalgebra as na;
use ofps::prelude::v1::{ptrplus::*, Result, *};
use ofps::utils::*;
use std::io::*;
use std::mem::MaybeUninit;

ofps::define_descriptor!(av, Decoder, |input| {
    let f = open_file(&input)?;
    AvDecoder::try_new(f).map(|d| Box::new(d) as _)
});

pub struct AvBuf(&'static mut [u8]);

impl AvBuf {
    pub fn try_new(size: usize) -> Result<Self> {
        let buf = unsafe { av_malloc(size) as *mut u8 };

        if buf.is_null() {
            Err(anyhow!("Failed to allocate buffer"))
        } else {
            Ok(Self(unsafe { slice::from_raw_parts_mut(buf, size) }))
        }
    }
}

impl Drop for AvBuf {
    fn drop(&mut self) {
        unsafe { av_free(self.0.as_mut_ptr() as *mut _) }
    }
}

impl Deref for AvBuf {
    type Target = [u8];

    fn deref(&self) -> &[u8] {
        self.0
    }
}

impl DerefMut for AvBuf {
    fn deref_mut(&mut self) -> &mut [u8] {
        self.0
    }
}

pub struct AvContext<T: ?Sized> {
    #[allow(clippy::redundant_allocation)]
    _stream: Box<Box<T>>,
    pub fmt_ctx: &'static mut AVFormatContext,
    pub avio_ctx: &'static mut AVIOContext,
}

impl<T: ?Sized> Drop for AvContext<T> {
    fn drop(&mut self) {
        // SAFETY: the references will be dangling,
        // but after the drop nobody will read them.
        unsafe {
            avformat_close_input(&mut (self.fmt_ctx as *mut _));
            av_free((*self.avio_ctx).buffer as *mut _);
            av_free(self.avio_ctx as *mut _ as *mut c_void);
        }
    }
}

impl<T: Read + ?Sized> AvContext<T> {
    pub fn try_new(stream: Box<T>) -> Result<Self> {
        let mut buf = AvBuf::try_new(8196)?;

        let mut stream: Box<Box<T>> = stream.into();

        // SAFETY: Box<T> stream is being passed, which is the expected stream type in the
        // read_callback function.
        let avio_ctx = unsafe {
            avio_alloc_context(
                buf.as_mut_ptr(),
                buf.len() as _,
                0,
                (&mut *stream) as *mut Box<T> as *mut _,
                Some(Self::read_callback),
                None,
                None,
            )
            .as_mut()
        }
        .ok_or_else(|| anyhow!("Failed to allocate AVIOContext"))?;

        let mut fmt_ctx = unsafe { avformat_alloc_context().as_mut() }.ok_or_else(|| {
            unsafe { av_free((*avio_ctx).buffer as *mut _) };
            unsafe { av_free(avio_ctx as *mut AVIOContext as *mut _) };
            anyhow!("Failed to allocate AVFormatContext")
        })?;

        fmt_ctx.pb = avio_ctx;

        match unsafe {
            avformat_open_input(
                fmt_ctx.as_mut_ptr(),
                ptr::null(),
                ptr::null_mut(),
                ptr::null_mut(),
            )
        } {
            0 => {
                std::mem::forget(buf);
                Ok(Self {
                    _stream: stream,
                    fmt_ctx,
                    avio_ctx,
                })
            }
            e => {
                unsafe { av_free((*avio_ctx).buffer as *mut _) };
                unsafe { av_free(avio_ctx as *mut AVIOContext as *mut _) };
                Err(anyhow!("Unable to open input ({:x})", e))
            }
        }
    }

    unsafe extern "C" fn read_callback(
        opaque: *mut c_void,
        buf: *mut u8,
        buf_size: c_int,
    ) -> c_int {
        (*(opaque as *mut Box<T>))
            .read(slice::from_raw_parts_mut(buf, buf_size as _))
            .map(|r| r as c_int)
            .map(|r| {
                trace!("{}", r);
                r
            })
            .map_err(|e| {
                error!("{}", e);
                e
            })
            .unwrap_or(-1)
    }

    pub fn dump_format(&mut self) {
        unsafe { av_dump_format(self.fmt_ctx, 0, std::ptr::null(), 0) };
    }
}

pub struct AvDecoder<T: ?Sized> {
    pub av_ctx: AvContext<T>,
    codec_ctx: &'static mut AVCodecContext,
    av_frame: &'static mut AVFrame,
    stream_idx: i32,
    framerate: f64,
    aspect_ratio: (usize, usize),
    sws_av_frame: &'static mut AVFrame,
    sws_ctx: Option<&'static mut SwsContext>,
}

impl<T: ?Sized> Properties for AvDecoder<T> {}

unsafe impl<T: Send + ?Sized> Send for AvDecoder<T> {}
unsafe impl<T: Sync + ?Sized> Sync for AvDecoder<T> {}

impl<T: ?Sized> Drop for AvDecoder<T> {
    fn drop(&mut self) {
        // SAFETY: the references will be dangling,
        // but after the drop nobody will read them.
        unsafe {
            av_frame_free(&mut (self.av_frame as *mut _));
            avcodec_free_context(&mut (self.codec_ctx as *mut _));
            av_frame_free(&mut (self.sws_av_frame as *mut _));
            if let Some(sws_ctx) = self.sws_ctx.take() {
                sws_freeContext(sws_ctx);
            }
        };
    }
}

struct RefFrame<'a> {
    frame: &'a mut AVFrame,
}

impl<'a> Drop for RefFrame<'a> {
    fn drop(&mut self) {
        unsafe { av_frame_unref(self.frame) };
    }
}

impl<'a> RefFrame<'a> {
    fn new(codec_ctx: &mut AVCodecContext, frame: &'a mut AVFrame) -> Result<Option<Self>> {
        match unsafe { avcodec_receive_frame(codec_ctx, frame) } {
            // TODO: match non-fatal errors
            -11 => Ok(None),
            e if e < 0 => return Err(anyhow!("Failed to recv frame ({})", e)),
            _ => Ok(Some(Self { frame })),
        }
    }
}

impl<'a> Deref for RefFrame<'a> {
    type Target = AVFrame;

    fn deref(&self) -> &Self::Target {
        self.frame
    }
}

impl<'a> DerefMut for RefFrame<'a> {
    fn deref_mut(&mut self) -> &mut Self::Target {
        self.frame
    }
}

impl<T: Read + ?Sized> AvDecoder<T> {
    pub fn try_new(stream: Box<T>) -> Result<Self> {
        let av_ctx = AvContext::try_new(stream)?;

        let mut decoder: Option<&mut AVCodec> = None;

        let stream_idx = match unsafe {
            av_find_best_stream(
                av_ctx.fmt_ctx,
                AVMediaType::AVMEDIA_TYPE_VIDEO,
                -1,
                -1,
                decoder.as_mut_ptr() as *mut _ as *mut *const AVCodec,
                0,
            )
        } {
            e if e < 0 => Err(anyhow!("Failed to find a stream ({})", e)),
            i => Ok(i),
        }?;

        let mut codec_ctx = unsafe { avcodec_alloc_context3(decoder.as_deref().as_ptr()).as_mut() }
            .ok_or_else(|| anyhow!("Failed to allocate codec context"))?;

        let stream = unsafe { (*av_ctx.fmt_ctx.streams.offset(stream_idx as _)).as_mut() }
            .ok_or_else(|| anyhow!("Stream info null"))?;

        debug!("{:?}", stream);

        let framerate = if stream.avg_frame_rate.den != 0 && stream.avg_frame_rate.num != 0 {
            stream.avg_frame_rate.num as f64 / stream.avg_frame_rate.den as f64
        } else {
            stream.time_base.den as f64 / stream.time_base.num as f64
        };

        match unsafe { avcodec_parameters_to_context(codec_ctx, stream.codecpar) } {
            e if e < 0 => {
                unsafe { avcodec_free_context(codec_ctx.as_mut_ptr()) };
                return Err(anyhow!("Failed to get codec parameters ({})", e));
            }
            _ => {}
        }

        let mut av_opts: Option<&mut AVDictionary> = None;

        unsafe {
            av_dict_set(
                av_opts.as_mut_ptr(),
                c_str!("flags2").as_ptr(),
                c_str!("+export_mvs").as_ptr(),
                0,
            );
        }

        match unsafe { avcodec_open2(codec_ctx, decoder.as_deref().as_ptr(), av_opts.as_mut_ptr()) }
        {
            e if e < 0 => {
                unsafe { avcodec_free_context(codec_ctx.as_mut_ptr()) };
                return Err(anyhow!("Failed to open codec ({})", e));
            }
            _ => {}
        }

        let av_frame = unsafe { av_frame_alloc().as_mut() }
            .ok_or_else(|| anyhow!("Unable to allocate frame"))?;

        let sws_av_frame = unsafe { av_frame_alloc().as_mut() }
            .ok_or_else(|| anyhow!("Unable to allocate sws frame"))?;

        debug!(
            "{:x} {:x}",
            codec_ctx.pix_fmt as usize,
            AVPixelFormat::AV_PIX_FMT_RGBA as usize
        );
        debug!(
            "{:?} {:?}",
            unsafe { av_pix_fmt_desc_get(codec_ctx.pix_fmt) },
            unsafe { av_pix_fmt_desc_get(AVPixelFormat::AV_PIX_FMT_RGBA) }
        );

        Ok(Self {
            av_ctx,
            codec_ctx,
            av_frame,
            stream_idx,
            framerate,
            aspect_ratio: (0, 0),
            sws_av_frame,
            sws_ctx: None,
        })
    }

    // https://stackoverflow.com/questions/67828088/pyav-ffmpeg-libav-access-side-data-without-decoding-the-video
    // For now we will decode the packets, but later we should be able to do it without a decoder.

    // HEVC uses CTUs: https://en.wikipedia.org/wiki/Coding_tree_unit

    pub fn extract_mvs(
        &mut self,
        packet: &mut AVPacket,
        mf: &mut MotionVectors,
        out_frame: Option<(&mut Vec<RGBA>, &mut usize)>,
    ) -> Result<bool> {
        match unsafe { avcodec_send_packet(self.codec_ctx, packet) } {
            e if e < 0 => return Err(anyhow!("Failed to send packet ({})", e)),
            _ => {}
        }

        if let Some(frame) = RefFrame::new(self.codec_ctx, self.av_frame)? {
            self.aspect_ratio = (frame.width as usize, frame.height as usize);

            if let Some((out_frame, out_height)) = out_frame {
                out_frame.clear();

                // https://stackoverflow.com/questions/41196429/ffmpeg-avframe-to-per-channel-array-conversion
                let sws_ctx = match self.sws_ctx.take() {
                    Some(ctx) => ctx,
                    None => unsafe {
                        let sws_frame = &mut self.sws_av_frame;

                        sws_frame.format = AVPixelFormat::AV_PIX_FMT_RGBA as _;
                        sws_frame.width = frame.width;
                        sws_frame.height = frame.height;

                        av_frame_get_buffer(*sws_frame, 32);

                        sws_getContext(
                            frame.width,
                            frame.height,
                            std::mem::transmute(frame.format),
                            sws_frame.width,
                            sws_frame.height,
                            std::mem::transmute(sws_frame.format),
                            0,
                            ptr::null_mut(),
                            ptr::null_mut(),
                            ptr::null(),
                        )
                        .as_mut()
                    }
                    .ok_or_else(|| anyhow!("Unable to allocate sws context"))?,
                };

                let sws_frame = &mut self.sws_av_frame;

                unsafe {
                    sws_scale(
                        sws_ctx,
                        frame.data.as_ptr() as *const *const _,
                        frame.linesize.as_ptr(),
                        0,
                        frame.height,
                        sws_frame.data.as_mut_ptr(),
                        sws_frame.linesize.as_mut_ptr(),
                    )
                };

                let frame_data = sws_frame.data[0];
                let linesize = sws_frame.linesize[0] as usize;
                let width = sws_frame.width as usize;
                *out_height = sws_frame.height as usize;
                for y in 0..*out_height {
                    let frame_slice =
                        unsafe { slice::from_raw_parts(frame_data.add(linesize * y), width * 4) };
                    for chunk in frame_slice.chunks_exact(4) {
                        out_frame.push(RGBA::from_rgb_slice(chunk));
                    }
                }

                self.sws_ctx = Some(sws_ctx);
            }

            if let Some(side_data) = unsafe {
                av_frame_get_side_data(&*frame, AVFrameSideDataType::AV_FRAME_DATA_MOTION_VECTORS)
                    .as_ref()
            } {
                let size = side_data.size as usize / std::mem::size_of::<AVMotionVector>();
                let motion_vectors =
                    unsafe { slice::from_raw_parts(side_data.data as *const AVMotionVector, size) };

                let frame_norm =
                    na::Vector2::new(1f32 / frame.width as f32, 1f32 / frame.height as f32);

                // TODO: use dst or src?
                for mv in motion_vectors {
                    let pos = na::Vector2::new(mv.src_x as f32, mv.src_y as f32)
                        .component_mul(&frame_norm)
                        .into();
                    let motion = na::Vector2::new(mv.motion_x as f32, mv.motion_y as f32)
                        .component_div(&na::Vector2::new(
                            mv.motion_scale as f32,
                            mv.motion_scale as f32,
                        ))
                        .component_mul(&-frame_norm);

                    mf.push((pos, motion));
                }

                Ok(true)
            } else {
                Ok(false)
            }
        } else {
            Ok(false)
        }
    }
}

impl<T: Read + ?Sized> Decoder for AvDecoder<T> {
    fn process_frame(
        &mut self,
        mf: &mut MotionVectors,
        mut out_frame: Option<(&mut Vec<RGBA>, &mut usize)>,
        mut skip: usize,
    ) -> Result<bool> {
        let mut packet = MaybeUninit::uninit();
        let mut reached_stream = false;
        let mut ret = false;

        while !reached_stream || skip > 0 {
            match unsafe { av_read_frame(self.av_ctx.fmt_ctx, packet.as_mut_ptr()) } {
                e if e < 0 => return Err(anyhow!("Failed to read frame ({})", e)),
                _ => {
                    if skip > 0 {
                        skip -= 1;
                        if skip > 20 {
                            continue;
                        }
                    }

                    let packet = unsafe { packet.assume_init_mut() };

                    trace!("Read packet: {} {}", packet.stream_index, packet.size);

                    if packet.stream_index == self.stream_idx {
                        debug!("Reached wanted stream!");
                        reached_stream = true;
                        ret = self.extract_mvs(packet, mf, out_frame.take())?;
                    }

                    unsafe { av_packet_unref(packet) };
                }
            }
        }

        Ok(ret)
    }

    fn get_framerate(&self) -> Option<f64> {
        Some(self.framerate)
    }

    fn get_aspect(&self) -> Option<(usize, usize)> {
        Some(self.aspect_ratio)
    }
}
