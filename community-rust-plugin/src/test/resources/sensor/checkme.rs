//! Contains code shared between this module's submodules

use crate::{
    configs,
    low_level_analysis::{
        BigOAlgorithmType,
        types::*,
    },
};
use std::{
    ops::Range,
    time::{SystemTime, Duration},
};


/// wrap around the original [run_iterator_pass()] to output progress & intermediate results
pub fn run_iterator_pass_verbosely<'a, _IteratorAlgorithmClosure: Fn(u32) -> u32 + Sync,
                                       _OutputClosure:            FnMut(&str),
                                       T: TryInto<u64> + Copy> (result_prefix:      &str,
                                                                result_suffix:      &str,
                                                                iterator_algorithm: &_IteratorAlgorithmClosure,
                                                                algorithm_type:     &BigOAlgorithmType,
                                                                range:              Range<u32>,
                                                                time_unit:          &'a TimeUnit<T>,
                                                                threads:            u32,
                                                                mut output:         _OutputClosure)
                                                                -> (PassResult<'a,T>, u32) {
    let (pass_result, r) = run_iterator_pass(iterator_algorithm, algorithm_type, range, time_unit, threads);
    output(&format!("{}{}/{}{}", result_prefix, pass_result.time_measurements, pass_result.space_measurements, result_suffix));
    (pass_result, r)
}

/// wrap around the original [run_pass()] to output progress & intermediate results
pub fn run_pass_verbosely<'a, _OutputClosure:    FnMut(&str),
                              T: TryInto<u64> + Copy> (result_prefix:  &str,
                                                       result_suffix:  &str,
                                                       algorithm:      impl FnMut() -> u32,
                                                       time_unit:      &'a TimeUnit<T>,
                                                       mut output:     _OutputClosure)
                                                      -> (PassResult<'a,T>, u32) {
    let (pass_result, r) = run_pass(algorithm, time_unit);
    output(&format!("{}{}/{}{}", result_prefix, pass_result.time_measurements, pass_result.space_measurements, result_suffix));
    (pass_result, r)
}

/// Runs a pass on the given `iterator_algorithm` callback function or closure,
/// measuring (and returning) the time it took to run all iterations specified in `range`
/// -- with the option to run the iteration of the given number of `threads`.\
/// An `iterator_algorithm` is one that provides 1 element on each call or processes 1 element on each call.\
/// See [run_pass()] for algorithms which generates or operates on several elements per call.
/// ```
///     /// Iterator Algorithm function under analysis -- receives the iteration number on each call
///     /// (for set changing algorithms) or the set size (for constant set algorithms).
///     /// Returns a(ny) computed number based on the input -- to avoid compiler call cancellation optimizations
///     fn iterator_algorithm(i: u32) -> u32 {0}
/// ```
/// returns: tuple with ([PassResult], computed_number: u32)
pub(crate) fn run_iterator_pass<'a, _AlgorithmClosure: Fn(u32) -> u32 + Sync,
                                    _ScalarDuration:   TryInto<u64> + Copy> (iterator_algorithm: &_AlgorithmClosure,
                                                                             algorithm_type:     &BigOAlgorithmType,
                                                                             range:              Range<u32>,
                                                                             time_unit:          &'a TimeUnit<_ScalarDuration>,
                                                                             threads:            u32)
                                                                            -> (PassResult<'a,_ScalarDuration>, u32) {

    type ThreadLoopResult = (Duration, u32);

    fn thread_loop<_AlgorithmClosure: Fn(u32) -> u32 + Sync>
                  (iterator_algorithm: &_AlgorithmClosure, algorithm_type: &BigOAlgorithmType, range: Range<u32>)
                  -> ThreadLoopResult {
        let mut thread_r: u32 = range.end;

        let thread_start = SystemTime::now();

        // run 'algorithm()' allowing normal or reversed order
        match algorithm_type {
            BigOAlgorithmType::ConstantSet => {
                if range.end < range.start {
                    for e in (range.end..range.start).rev() {
                        thread_r ^= iterator_algorithm(e);
                    }
                } else {
                    for e in range {
                        thread_r ^= iterator_algorithm(e);
                    }
                }
            },
            BigOAlgorithmType::SetResizing => {
                if range.end < range.start {
                    for e in (range.end..range.start).rev() {
                        thread_r ^= iterator_algorithm(e);
                    }
                } else {
                    for e in range {
                        thread_r ^= iterator_algorithm(e);
                    }
                }
            },
        }

        let thread_end = SystemTime::now();
        let thread_duration = thread_end.duration_since(thread_start).unwrap();

        (thread_duration, thread_r)
    }

    // use crossbeam's scoped threads to avoid requiring a 'static lifetime for our algorithm closure
    crossbeam::scope(|scope| {

        // start all threads
        let i32_range = range.end as i32 .. range.start as i32;
        let chunk_size = (i32_range.end-i32_range.start)/threads as i32;
        let mut thread_handlers: Vec<crossbeam::thread::ScopedJoinHandle<ThreadLoopResult>> = Vec::with_capacity(threads as usize);
        let allocator_savepoint = configs::ALLOC.save_point();
        for n in 0..threads as i32 {
            let chunked_range = i32_range.start+chunk_size*n..i32_range.start+chunk_size*(n+1);
            thread_handlers.push( scope.spawn(move |_| thread_loop(iterator_algorithm, algorithm_type, chunked_range.start as u32 .. chunked_range.end as u32)) );
        }

        // wait for them all to finish
        let mut r = range.start+1;
        let mut elapsed_average = 0.0f64;
        for handler in thread_handlers {
            let joining_result = handler.join();
            if joining_result.is_err() {
                panic!("Panic! while running provided 'algorithm' closure: algo type: {:?}, range: {:?}: Error: {:?}", algorithm_type, range, joining_result.unwrap_err())
            }
            let (thread_duration, thread_r) = joining_result.unwrap();
            let thread_elapsed = (time_unit.duration_conversion_fn_ptr)(&thread_duration).try_into().unwrap_or_default();
            elapsed_average += thread_elapsed as f64 / threads as f64;
            r ^= thread_r;
        }

        let allocator_statistics = configs::ALLOC.delta_statistics(&allocator_savepoint);

        (PassResult {
            time_measurements:  BigOTimePassMeasurements {
                elapsed_time: elapsed_average.round() as u64,
                time_unit,
            },
            space_measurements: BigOSpacePassMeasurements {
                used_memory_before: allocator_savepoint.metrics.current_used_memory,
                used_memory_after:  allocator_statistics.current_used_memory,
                min_used_memory:    allocator_statistics.min_used_memory,
                max_used_memory:    allocator_statistics.max_used_memory,
            },
        }, r)

    }).unwrap()

}

/// Runs a pass on the given `algorithm` callback function or closure,
/// measuring (and returning) the time it took to run it.\
/// See [run_iterator_pass()] for algorithms which generates or operates on a single element per call.
/// ```
///     /// Algorithm function under analysis.
///     /// Returns a(ny) computed number to avoid compiler call cancellation optimizations
///     fn algorithm() -> u32 {0}
/// ```
/// returns: tuple with ([PassResult]], computed_number: u32)
pub(crate) fn run_pass<'a, _ScalarDuration:   TryInto<u64> + Copy> (mut algorithm:  impl FnMut() -> u32,
                                                                    time_unit:      &'a TimeUnit<_ScalarDuration>)
                                                                   -> (PassResult<'a,_ScalarDuration>, u32) {

    let allocator_savepoint = configs::ALLOC.save_point();
    let start = SystemTime::now();
    let r = algorithm();
    let duration = start.elapsed().unwrap();
    let elapsed = (time_unit.duration_conversion_fn_ptr)(&duration).try_into().unwrap_or_default();
    let allocator_statistics = configs::ALLOC.delta_statistics(&allocator_savepoint);

    (PassResult {
        time_measurements:  BigOTimePassMeasurements {
            elapsed_time: elapsed,
            time_unit,
        },
        space_measurements: BigOSpacePassMeasurements {
            used_memory_before: allocator_savepoint.metrics.current_used_memory,
            used_memory_after:  allocator_statistics.current_used_memory,
            min_used_memory:    allocator_statistics.min_used_memory,
            max_used_memory:    allocator_statistics.max_used_memory,
        },
    }, r)
}

/// contains the measurements for a pass done in [run_pass()]
#[derive(Clone,Copy)]
pub struct PassResult<'a,ScalarTimeUnit: Copy> {
    pub time_measurements:  BigOTimePassMeasurements<'a,ScalarTimeUnit>,
    pub space_measurements: BigOSpacePassMeasurements,
}
impl<ScalarTimeUnit: Copy> Default for PassResult<'_,ScalarTimeUnit> {
    fn default() -> Self {
        Self {
            time_measurements: BigOTimePassMeasurements {
                elapsed_time: 0,
                time_unit: &TimeUnits::get_const_default(),
            },
            space_measurements: BigOSpacePassMeasurements {
                used_memory_before: 0,
                used_memory_after:  0,
                min_used_memory:    0,
                max_used_memory:    0,
            }
        }
    }
}

