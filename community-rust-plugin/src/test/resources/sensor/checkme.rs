//! See [super]

use std::fmt::{Formatter, Display};
use std::sync::atomic::{AtomicUsize, Ordering};
use std::alloc::{System, GlobalAlloc, Layout};

use crate::metrics_allocator::ring_buffer::{RingBuffer, RingBufferConsumer};

/// struct returned by [MetricsAllocator::delta_statistics()]
pub struct MetricsAllocatorStatistics<NumericType> {
    pub allocations_count:            NumericType,
    pub deallocations_count:          NumericType,
    pub zeroed_allocations_count:     NumericType,
    pub reallocations_count:          NumericType,
    pub allocated_bytes:              NumericType,
    pub deallocated_bytes:            NumericType,
    pub zeroed_allocated_bytes:       NumericType,
    pub reallocated_originals_bytes:  NumericType,
    pub reallocated_news_bytes:       NumericType,
    pub current_used_memory:          NumericType,
    pub min_used_memory:              NumericType,
    pub max_used_memory:              NumericType,
}
impl<NumericType> MetricsAllocatorStatistics<NumericType> {
    fn fmt(&self, statistics: &MetricsAllocatorStatistics<usize>, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{{counts: {{allocations: {}, deallocations: {}, zeroed_allocations: {}, reallocations: {}}}, bytes: {{allocated: {}, deallocated: {}, zeroed: {}, reallocated: {{originals: {}, news: {}}}}}, current_used_memory: {}, min_used_memory: {}, max_used_memory: {}}}",
               statistics.allocations_count, statistics.deallocations_count, statistics.zeroed_allocations_count, statistics.reallocations_count,
               statistics.allocated_bytes, statistics.deallocated_bytes, statistics.zeroed_allocated_bytes,
               statistics.reallocated_originals_bytes, statistics.reallocated_news_bytes,
               statistics.current_used_memory, statistics.min_used_memory, statistics.max_used_memory)
    }
}
impl Display for MetricsAllocatorStatistics<AtomicUsize> {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        self.fmt(&MetricsAllocatorStatistics::<usize> {
            allocations_count:           self.allocations_count.          load(Ordering::Relaxed),
            deallocations_count:         self.deallocations_count.        load(Ordering::Relaxed),
            zeroed_allocations_count:    self.zeroed_allocations_count.   load(Ordering::Relaxed),
            reallocations_count:         self.reallocations_count.        load(Ordering::Relaxed),
            allocated_bytes:             self.allocated_bytes.            load(Ordering::Relaxed),
            deallocated_bytes:           self.deallocated_bytes.          load(Ordering::Relaxed),
            zeroed_allocated_bytes:      self.zeroed_allocated_bytes.     load(Ordering::Relaxed),
            reallocated_originals_bytes: self.reallocated_originals_bytes.load(Ordering::Relaxed),
            reallocated_news_bytes:      self.reallocated_news_bytes.     load(Ordering::Relaxed),
            current_used_memory:         self.current_used_memory.        load(Ordering::Relaxed),
            min_used_memory:             self.min_used_memory.            load(Ordering::Relaxed),
            max_used_memory:             self.max_used_memory.            load(Ordering::Relaxed),
        }, f)
    }
}
impl Display for MetricsAllocatorStatistics<usize> {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        self.fmt(self, f)
    }
}

/// struct returned by [MetricsAllocator::save_point()]
pub struct MetricsAllocatorSavePoint<'a, const RING_BUFFER_SIZE: usize> {
    /// contains the allocation metrics since point-zero
    pub metrics:                      MetricsAllocatorStatistics<usize>,
    /// object to allow retrieving all subsequent saved points -- see the algorithm in [MetricsAllocator::save_point()] and [MetricsAllocator::delta_statistics()]
    used_memory_ring_buffer_consumer: RingBufferConsumer<'a, SavePointRingBufferSlot<usize>, RING_BUFFER_SIZE>,

}

/// Represents a "save point" -- and is used as a ring buffer slot.
/// Used to allow memory usage tracking for several "save points"
/// (measured against the runtime situation).
struct SavePointRingBufferSlot<NumericType> {
    /// contains the minimum used memory between this "save point" and the current runtime
    min_used_memory: NumericType,
    /// contains the maximum used memory between this "save point" and the current runtime
    max_used_memory: NumericType,
}
impl Default for SavePointRingBufferSlot<usize> {
    fn default() -> Self {
        Self {
            min_used_memory: 0,
            max_used_memory: 0,
        }
    }
}
impl Default for SavePointRingBufferSlot<AtomicUsize> {
    fn default() -> Self {
        Self {
            min_used_memory: AtomicUsize::new(0),
            max_used_memory: AtomicUsize::new(0),
        }
    }
}

/// The replacement for the System's Global Allocator.\
/// See [self] for more info.
pub struct MetricsAllocator<'a, const RING_BUFFER_SIZE: usize> {
    system_allocator:        &'a System,
    statistics:              MetricsAllocatorStatistics<AtomicUsize>,
    used_memory_ring_buffer: RingBuffer<SavePointRingBufferSlot<usize>, RING_BUFFER_SIZE>,
}
impl<'a, const RING_BUFFER_SIZE: usize> MetricsAllocator<'a, RING_BUFFER_SIZE> {

    /// Creates an instance capable of replacing the Global Allocator.\
    /// See [self] for more info.
    pub const fn new() -> Self {
        Self {
            system_allocator: &System,
            statistics: MetricsAllocatorStatistics {
                allocations_count:           AtomicUsize::new(0),
                deallocations_count:         AtomicUsize::new(0),
                zeroed_allocations_count:    AtomicUsize::new(0),
                reallocations_count:         AtomicUsize::new(0),
                allocated_bytes:             AtomicUsize::new(0),
                deallocated_bytes:           AtomicUsize::new(0),
                zeroed_allocated_bytes:      AtomicUsize::new(0),
                reallocated_originals_bytes: AtomicUsize::new(0),
                reallocated_news_bytes:      AtomicUsize::new(0),
                current_used_memory:         AtomicUsize::new(0),
                min_used_memory:             AtomicUsize::new(0),
                max_used_memory:             AtomicUsize::new(0),
            },
            used_memory_ring_buffer: RingBuffer::new(),
        }
    }

    /// Prepares a new measurement for future allocations, to be inferred by [delta_statistics()](MetricsAllocator::delta_statistics()).
    pub fn save_point(&self) -> MetricsAllocatorSavePoint<RING_BUFFER_SIZE> {
        // add the current (min,max) to the ring buffer and start a new counter
        // the new consumer will consume any further saved_points + the current (min,max)
        self.used_memory_ring_buffer.enqueue(SavePointRingBufferSlot {
            min_used_memory: self.statistics.min_used_memory.load(Ordering::Relaxed),
            max_used_memory: self.statistics.max_used_memory.load(Ordering::Relaxed),
        });
        let used_memory_ring_buffer_consumer = self.used_memory_ring_buffer.consumer();
        self.statistics.min_used_memory.store(self.statistics.current_used_memory.load(Ordering::Relaxed), Ordering::Relaxed);
        self.statistics.max_used_memory.store(self.statistics.current_used_memory.load(Ordering::Relaxed), Ordering::Relaxed);
        MetricsAllocatorSavePoint {
            metrics: MetricsAllocatorStatistics {
                allocations_count:           self.statistics.allocations_count          .load(Ordering::Relaxed),
                deallocations_count:         self.statistics.deallocations_count        .load(Ordering::Relaxed),
                zeroed_allocations_count:    self.statistics.zeroed_allocations_count   .load(Ordering::Relaxed),
                reallocations_count:         self.statistics.reallocations_count        .load(Ordering::Relaxed),
                allocated_bytes:             self.statistics.allocated_bytes            .load(Ordering::Relaxed),
                deallocated_bytes:           self.statistics.deallocated_bytes          .load(Ordering::Relaxed),
                zeroed_allocated_bytes:      self.statistics.zeroed_allocated_bytes     .load(Ordering::Relaxed),
                reallocated_originals_bytes: self.statistics.reallocated_originals_bytes.load(Ordering::Relaxed),
                reallocated_news_bytes:      self.statistics.reallocated_news_bytes     .load(Ordering::Relaxed),
                current_used_memory:         self.statistics.current_used_memory        .load(Ordering::Relaxed),
                min_used_memory:             self.statistics.min_used_memory            .load(Ordering::Relaxed),
                max_used_memory:             self.statistics.max_used_memory            .load(Ordering::Relaxed),
            },
            used_memory_ring_buffer_consumer
        }
    }

    /// Returns the allocation statistics between now and the point in time when `save_point` was generated
    /// (with a call to [save_point()](MetricsAllocator::save_point())).
    pub fn delta_statistics(&self, save_point: &MetricsAllocatorSavePoint<RING_BUFFER_SIZE>) -> MetricsAllocatorStatistics<usize> {
        let mut min = usize::MAX;
        let mut max = usize::MIN;
        // compute (min,max) since the given 'save_point'
        for peeked_chunk in save_point.used_memory_ring_buffer_consumer.peek_all().unwrap() {
            for subsequent_save_point in peeked_chunk {
                min = min.min(subsequent_save_point.min_used_memory);
                max = max.max(subsequent_save_point.max_used_memory);
            }
        }
        // compute the current (min,max)
        min = min.min(self.statistics.min_used_memory.load(Ordering::Relaxed));
        max = max.max(self.statistics.max_used_memory.load(Ordering::Relaxed));
        MetricsAllocatorStatistics::<usize> {
            allocations_count:           self.statistics.allocations_count          .load(Ordering::Relaxed) - save_point.metrics.allocations_count,
            deallocations_count:         self.statistics.deallocations_count        .load(Ordering::Relaxed) - save_point.metrics.deallocations_count,
            zeroed_allocations_count:    self.statistics.zeroed_allocations_count   .load(Ordering::Relaxed) - save_point.metrics.zeroed_allocations_count,
            reallocations_count:         self.statistics.reallocations_count        .load(Ordering::Relaxed) - save_point.metrics.reallocations_count,
            allocated_bytes:             self.statistics.allocated_bytes            .load(Ordering::Relaxed) - save_point.metrics.allocated_bytes,
            deallocated_bytes:           self.statistics.deallocated_bytes          .load(Ordering::Relaxed) - save_point.metrics.deallocated_bytes,
            zeroed_allocated_bytes:      self.statistics.zeroed_allocated_bytes     .load(Ordering::Relaxed) - save_point.metrics.zeroed_allocated_bytes,
            reallocated_originals_bytes: self.statistics.reallocated_originals_bytes.load(Ordering::Relaxed) - save_point.metrics.reallocated_originals_bytes,
            reallocated_news_bytes:      self.statistics.reallocated_news_bytes     .load(Ordering::Relaxed) - save_point.metrics.reallocated_news_bytes,
            current_used_memory:         self.statistics.current_used_memory        .load(Ordering::Relaxed),
            min_used_memory:             min,
            max_used_memory:             max,
        }
    }

    /// compute metrics for allocation
    fn compute_alloc_metrics(&self, layout: &Layout) {
        self.statistics.allocations_count.fetch_add(1, Ordering::Relaxed);
        self.statistics.allocated_bytes.fetch_add(layout.size(), Ordering::Relaxed);
        self.statistics.current_used_memory.fetch_add(layout.size(), Ordering::Relaxed);
        self.compute_min_and_max_used_memories();
    }

    /// compute metrics for de-allocation
    fn compute_dealloc_metrics(&self, layout: &Layout) {
        self.statistics.deallocations_count.fetch_add(1, Ordering::Relaxed);
        self.statistics.deallocated_bytes.fetch_add(layout.size(), Ordering::Relaxed);
        self.statistics.current_used_memory.fetch_sub(layout.size(), Ordering::Relaxed);
        self.compute_min_and_max_used_memories();
    }

    /// compute metrics for zeroed allocation
    fn compute_alloc_zeroed_metrics(&self, layout: &Layout) {
        self.statistics.zeroed_allocations_count.fetch_add(1, Ordering::Relaxed);
        self.statistics.zeroed_allocated_bytes.fetch_add(layout.size(), Ordering::Relaxed);
        self.statistics.current_used_memory.fetch_add(layout.size(), Ordering::Relaxed);
        self.compute_min_and_max_used_memories();
    }

    /// compute metrics for re-allocation
    fn compute_realloc_metrics(&self, layout: &Layout, new_size: usize) {
        self.statistics.reallocations_count.fetch_add(1, Ordering::Relaxed);
        self.statistics.reallocated_originals_bytes.fetch_add(layout.size(), Ordering::Relaxed);
        self.statistics.reallocated_news_bytes.fetch_add(new_size, Ordering::Relaxed);
        if new_size > layout.size() {
            self.statistics.current_used_memory.fetch_add(new_size-layout.size(), Ordering::Relaxed);
        } else if new_size < layout.size() {
            self.statistics.current_used_memory.fetch_sub(layout.size()-new_size, Ordering::Relaxed);
        }
        self.compute_min_and_max_used_memories();
    }

    /// helper functions for metrics computation
    fn compute_min_and_max_used_memories(&self) {
        let current_used_memory = self.statistics.current_used_memory.load(Ordering::Relaxed);
        let mut min_used_memory = self.statistics.min_used_memory.load(Ordering::Relaxed);
        loop {
            if current_used_memory < min_used_memory {
                match self.statistics.min_used_memory.compare_exchange_weak(min_used_memory, current_used_memory, Ordering::Relaxed, Ordering::Relaxed) {
                    Ok(_) => break,
                    Err(reloaded_val) => min_used_memory = reloaded_val,
                }
            } else {
                break;
            }
        }
        let mut max_used_memory = self.statistics.max_used_memory.load(Ordering::Relaxed);
        loop {
            if current_used_memory > max_used_memory {
                match self.statistics.max_used_memory.compare_exchange_weak(max_used_memory, current_used_memory, Ordering::Relaxed, Ordering::Relaxed) {
                    Ok(_) => break,
                    Err(reloaded_val) => max_used_memory = reloaded_val,
                }
            } else {
                break;
            }
        }
    }
}

/// the global allocator
unsafe impl<'a, const RING_BUFFER_SIZE: usize> GlobalAlloc for MetricsAllocator<'a, RING_BUFFER_SIZE> {
    unsafe fn alloc(&self, layout: Layout) -> *mut u8 {
        self.compute_alloc_metrics(&layout);
        self.system_allocator.alloc(layout)
    }
    unsafe fn dealloc(&self, ptr: *mut u8, layout: Layout) {
        self.compute_dealloc_metrics(&layout);
        self.system_allocator.dealloc(ptr, layout)
    }
    unsafe fn alloc_zeroed(&self, layout: Layout) -> *mut u8 {
        self.compute_alloc_zeroed_metrics(&layout);
        self.system_allocator.alloc_zeroed(layout)
    }
    unsafe fn realloc(&self, ptr: *mut u8, layout: Layout, new_size: usize) -> *mut u8 {
        self.compute_realloc_metrics(&layout, new_size);
        self.system_allocator.realloc(ptr, layout, new_size)
    }
}


#[cfg(any(test, feature="dox"))]
mod tests {

    //! Unit tests for [metrics_allocator](super) module

    use super::*;


    /// the same code used in [metrics_allocator](super) module docs
    #[cfg_attr(not(feature = "dox"), test)]
    fn usage_example() {
        use crate::configs::ALLOC;
        let save_point = ALLOC.save_point();
        let _vec = Vec::<u32>::with_capacity(1024);
        let metrics = ALLOC.delta_statistics(&save_point);
        println!("Allocator Metrics for the Vec allocation: {}", metrics);
    }

    /// uses the metrics computation functions to simulate a bunch of allocations / de-allocations,
    /// checking the [save_point()](MetricsAllocator::save_point()) and [delta_statistics()](MetricsAllocator::delta_statistics())  results
    #[cfg_attr(not(feature = "dox"), test)]
    fn test_save_point_min_and_max_memory_usage() {
        let allocator = MetricsAllocator::<16>::new();
        let mut used_mem = 0usize;
        let mut min_mem = usize::MAX;
        let mut max_mem = usize::MIN;

        let save_point = |used_mem: &usize, min_mem: &mut usize, max_mem: &mut usize| {
            *min_mem = *used_mem;
            *max_mem = *used_mem;
            allocator.save_point()
        };

        let assert_current_min_and_max_memory = |save_point, expected_used_mem, expected_min_mem, expected_max_mem| {
            let metrics = allocator.delta_statistics(save_point);
            assert_eq!(metrics.current_used_memory, expected_used_mem, "wrong used memory metrics");
            assert_eq!(metrics.min_used_memory,     expected_min_mem,  "wrong min memory metrics");
            assert_eq!(metrics.max_used_memory,     expected_max_mem,  "wrong max memory metrics");
        };

        let allocate_and_check = |size, save_point, used_mem: &mut usize, min_mem: &mut usize, max_mem: &mut usize| {
            let layout = Layout::from_size_align(size, 4).unwrap();
            allocator.compute_alloc_metrics(&layout);
            // update local metrics
            *min_mem = *min_mem.min(used_mem);
            *used_mem += size;
            *max_mem = *max_mem.max(used_mem);
            assert_current_min_and_max_memory(save_point, *used_mem, *min_mem, *max_mem);
        };

        let deallocate_and_check = |size, save_point, used_mem: &mut usize, min_mem: &mut usize, max_mem: &mut usize| {
            let layout = Layout::from_size_align(size, 4).unwrap();
            allocator.compute_dealloc_metrics(&layout);
            // update local metrics
            *max_mem = *max_mem.max(used_mem);
            *used_mem -= size;
            *min_mem = *min_mem.min(used_mem);
            assert_current_min_and_max_memory(save_point, *used_mem, *min_mem, *max_mem);
        };

        // first save_point -- from zero
        let save_point1 = save_point(&used_mem, &mut min_mem, &mut max_mem);
        allocate_and_check(12345, &save_point1, &mut used_mem, &mut min_mem, &mut max_mem);
        allocate_and_check(54321, &save_point1, &mut used_mem, &mut min_mem, &mut max_mem);
        assert_current_min_and_max_memory(&save_point1, 66666, 0, 66666);

        // new save_point
        let save_point2 = save_point(&used_mem, &mut min_mem, &mut max_mem);
        allocate_and_check(30303, &save_point2, &mut used_mem, &mut min_mem, &mut max_mem);
        allocate_and_check(03030, &save_point2, &mut used_mem, &mut min_mem, &mut max_mem);
        assert_current_min_and_max_memory(&save_point2, 99999, 66666, 99999);
        assert_current_min_and_max_memory(&save_point1, 99999, 0,     99999);

        // new save_point
        let save_point3 = save_point(&used_mem, &mut min_mem, &mut max_mem);
        deallocate_and_check(05050, &save_point3, &mut used_mem, &mut min_mem, &mut max_mem);
        deallocate_and_check(50505, &save_point3, &mut used_mem, &mut min_mem, &mut max_mem);
        assert_current_min_and_max_memory(&save_point3, 44444, 44444, 99999);
        assert_current_min_and_max_memory(&save_point2, 44444, 44444, 99999);
        assert_current_min_and_max_memory(&save_point1, 44444, 0,     99999);

        // new save_point
        let save_point4 = save_point(&used_mem, &mut min_mem, &mut max_mem);
        deallocate_and_check(01010, &save_point4, &mut used_mem, &mut min_mem, &mut max_mem);
        deallocate_and_check(10101, &save_point4, &mut used_mem, &mut min_mem, &mut max_mem);
        assert_current_min_and_max_memory(&save_point4, 33333, 33333, 44444);
        assert_current_min_and_max_memory(&save_point3, 33333, 33333, 99999);
        assert_current_min_and_max_memory(&save_point2, 33333, 33333, 99999);
        assert_current_min_and_max_memory(&save_point1, 33333, 0,     99999);

        // debug info
        eprintln!("Final metrics for 'save_point1': {}", allocator.delta_statistics(&save_point1));
        eprintln!("Final metrics for 'save_point2': {}", allocator.delta_statistics(&save_point2));
        eprintln!("Final metrics for 'save_point3': {}", allocator.delta_statistics(&save_point3));
        eprintln!("Final metrics for 'save_point4': {}", allocator.delta_statistics(&save_point4));
    }
}
