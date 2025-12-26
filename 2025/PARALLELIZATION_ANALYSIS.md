# Parallelization Analysis for 2025 Module

This document analyzes parallelization opportunities across all days in the 2025 module, identifying different techniques that could be applied.

## Overview

The analysis covers 12 days of Advent of Code problems, examining:
- **Data Parallelism**: Processing independent data elements in parallel
- **Task Parallelism**: Executing independent tasks concurrently
- **Pipeline Parallelism**: Overlapping computation stages
- **Stream Parallelization**: Using Java parallel streams
- **Fork/Join**: Recursive task decomposition
- **Thread-safe Collections**: Concurrent data structures for shared state

---

## Day 1: DialRotator3

### Current Implementation
- Sequential processing of rotation lines
- Stateful: maintains dial position across iterations
- Each line operation depends on previous position

### Parallelization Opportunities

#### Technique 1: **Chunk-based Parallel Processing with State Merging**
- **Approach**: Partition input lines into chunks, process each chunk independently starting from initial position, then merge results accounting for zero crossings
- **Complexity**: Requires careful handling of zero crossings at chunk boundaries
- **Benefit**: Medium - depends on input size and chunk granularity
- **Implementation**: Use `ForkJoinPool` or parallel streams with custom collector

#### Technique 2: **Parallel Stream with Sequential State Accumulation**
- **Approach**: Use `parallelStream()` but maintain sequential state using `reduce()` with identity and accumulator
- **Complexity**: Low - minimal code changes
- **Benefit**: Low - state dependency limits parallelism
- **Note**: Part 2's zero crossing calculation is more amenable to parallelization

---

## Day 2: InvalidIdValidator2

### Current Implementation
- Nested loops: outer loop over ranges, inner loop over IDs in each range
- ID validation is independent (pure function)

### Parallelization Opportunities

#### Technique 1: **Parallel Stream on Range Processing**
- **Approach**: Convert range loop to `ranges.parallelStream().flatMapToLong(range -> range.ids().filter(validator::test)).sum()`
- **Complexity**: Low - straightforward stream conversion
- **Benefit**: High - ID validation is CPU-intensive and independent
- **Implementation**: Replace nested loops with parallel stream pipeline

#### Technique 2: **Parallel ID Validation within Ranges**
- **Approach**: For each range, parallelize ID validation using `LongStream.rangeClosed(start, end).parallel().filter(validator::test).sum()`
- **Complexity**: Low
- **Benefit**: Medium-High - especially for large ranges
- **Note**: Can combine with Technique 1 for nested parallelism

#### Technique 3: **Work-stealing with Fork/Join**
- **Approach**: Custom `RecursiveTask` that splits ranges and IDs recursively
- **Complexity**: Medium - requires custom implementation
- **Benefit**: High - fine-grained control over task splitting
- **Use Case**: When ranges vary significantly in size

---

## Day 3: MaxJoltage

### Current Implementation
- Uses sequential streams for line processing
- Each line is processed independently

### Parallelization Opportunities

#### Technique 1: **Parallel Stream Processing**
- **Approach**: Add `.parallel()` to the stream: `ResourceLines.list(fileName).parallelStream().map(Bank::from)...`
- **Complexity**: Very Low - single method call change
- **Benefit**: Medium-High - line processing is independent
- **Implementation**: Minimal code change, high impact

#### Technique 2: **Parallel Bank Processing**
- **Approach**: Process multiple banks concurrently using `CompletableFuture` or parallel streams
- **Complexity**: Low
- **Benefit**: Medium - depends on number of lines and computation per line

---

## Day 4: GridNeighbor2

### Current Implementation
- Part 1: Sequential stream processing
- Part 2: Iterative removal with dependencies between iterations

### Parallelization Opportunities

#### Technique 1: **Parallel Stream for Part 1**
- **Approach**: `grid.findAll(c -> c == TARGET_CELL).parallelStream().filter(p -> hasFewerThanMinimumNeighbors(grid, p)).count()`
- **Complexity**: Low
- **Benefit**: Medium-High - neighbor counting is independent per cell
- **Note**: Grid is read-only, safe for parallel access

#### Technique 2: **Parallel Cell Removal Detection (Part 2)**
- **Approach**: Parallelize `findCellsToRemove()` within each iteration
- **Complexity**: Low
- **Benefit**: Medium - each iteration still sequential, but detection can be parallel
- **Limitation**: Removal phase must remain sequential due to grid mutation

#### Technique 3: **Concurrent Grid Updates**
- **Approach**: Use `ConcurrentHashMap` or synchronized blocks for grid updates
- **Complexity**: Medium - requires thread-safe grid implementation
- **Benefit**: Low - grid mutations are infrequent, synchronization overhead may outweigh benefits

---

## Day 5: Range3

### Current Implementation
- Part 1: Sequential nested loops checking IDs against intervals
- Part 2: Sequential interval merging

### Parallelization Opportunities

#### Technique 1: **Parallel ID Checking (Part 1)**
- **Approach**: `ids.parallelStream().filter(id -> containsId(intervals, id)).count()`
- **Complexity**: Low
- **Benefit**: High - ID checking is independent and CPU-intensive
- **Implementation**: Convert `LongList` iteration to parallel stream

#### Technique 2: **Parallel Interval Sorting (Part 2)**
- **Approach**: Use `parallelSort()` for large interval lists
- **Complexity**: Very Low - use `Arrays.parallelSort()` or `Collections.parallelSort()`
- **Benefit**: Medium - only helps if many intervals
- **Note**: Merging phase remains sequential (inherent dependency)

#### Technique 3: **Parallel Interval Overlap Checking**
- **Approach**: Pre-compute overlap matrix in parallel before merging
- **Complexity**: Medium
- **Benefit**: Low - merging algorithm is already efficient

---

## Day 6: MathBlock

### Current Implementation
- Sequential block processing
- Each block is independent

### Parallelization Opportunities

#### Technique 1: **Parallel Block Processing**
- **Approach**: Collect all blocks first, then process with `blocks.parallelStream().mapToLong(blockProcessor).sum()`
- **Complexity**: Medium - requires refactoring to collect blocks before processing
- **Benefit**: High - blocks are completely independent
- **Implementation**: Extract block collection logic, then parallelize processing

#### Technique 2: **Parallel Column Processing (Part 2)**
- **Approach**: Process columns in parallel when extracting numbers vertically
- **Complexity**: Medium
- **Benefit**: Medium - depends on block structure

---

## Day 7: BeamPathCounter

### Current Implementation
- Part 1: Recursive beam tracking with state
- Part 2: Recursive path counting with memoization

### Parallelization Opportunities

#### Technique 1: **Parallel Path Exploration (Part 2)**
- **Approach**: When beam splits, explore both paths in parallel using `CompletableFuture` or `ForkJoinPool`
- **Complexity**: High - requires thread-safe memoization
- **Benefit**: High - path exploration is independent after splits
- **Implementation**: Use `ConcurrentHashMap` for memo, parallel recursive calls

#### Technique 2: **Thread-safe Memoization**
- **Approach**: Replace `HashMap` with `ConcurrentHashMap` for memo cache
- **Complexity**: Low
- **Benefit**: Medium - enables parallel path counting
- **Note**: Must ensure thread-safe memo access

#### Technique 3: **Parallel Stream for Independent Paths**
- **Approach**: Process multiple starting positions in parallel
- **Complexity**: Medium
- **Benefit**: Low - single start position in current problem

---

## Day 8: PointCluster3

### Current Implementation
- Part 1: Sequential edge computation, PriorityQueue for top-k
- Part 2: Sequential edge computation and sorting

### Parallelization Opportunities

#### Technique 1: **Parallel Edge Computation**
- **Approach**: Parallelize the nested loop in `computeEdges()` using `IntStream.range(0, n).parallel().flatMap(i -> ...)`
- **Complexity**: Medium - requires careful handling of `ObjectArrayList` thread safety
- **Benefit**: Very High - O(n²) computation, highly parallelizable
- **Implementation**: Use thread-safe collection or collect results per thread then merge

#### Technique 2: **Parallel Edge Computation with Thread-local Collections**
- **Approach**: Use `ThreadLocal<ObjectArrayList>` or collect to thread-safe structure
- **Complexity**: Medium
- **Benefit**: Very High - eliminates contention in edge collection
- **Implementation**: Custom collector or `Collectors.toConcurrentMap()`

#### Technique 3: **Parallel Sorting**
- **Approach**: Use `Arrays.parallelSort()` for edge array (Part 2)
- **Complexity**: Very Low
- **Benefit**: Medium - helps with large edge lists
- **Note**: Already using efficient sort, but parallel sort can help for large datasets

#### Technique 4: **Parallel Top-K Selection (Part 1)**
- **Approach**: Use parallel stream to compute edges, then parallel top-k selection
- **Complexity**: Medium
- **Benefit**: High - reduces O(n² log k) to parallel computation

---

## Day 9: MaxRectangleArea

### Current Implementation
- Part 1: Sequential stream processing
- Part 2: **Already uses `.parallel()`** ✓

### Parallelization Opportunities

#### Technique 1: **Parallel Stream for Part 1**
- **Approach**: Add `.parallel()` to the `IntStream.range()` in `findMaxRectangleArea()`
- **Complexity**: Very Low - single method call
- **Benefit**: High - pair generation and area calculation are independent
- **Implementation**: `IntStream.range(0, points.size()).parallel().boxed()...`

#### Technique 2: **Optimize Existing Parallel (Part 2)**
- **Approach**: Already parallelized, but could optimize with better chunking or work distribution
- **Complexity**: Low
- **Benefit**: Low-Medium - already well parallelized

---

## Day 10: ButtonPressOptimizer

### Current Implementation
- Part 1: Sequential stream processing
- Part 2: **Already uses `.parallelStream()`** ✓

### Parallelization Opportunities

#### Technique 1: **Parallel Stream for Part 1**
- **Approach**: Change `.stream()` to `.parallelStream()` in `solvePartOne()`
- **Complexity**: Very Low - single method call change
- **Benefit**: High - line processing is independent
- **Implementation**: `lines.parallelStream().filter(...).map(...).sum()`

#### Technique 2: **Optimize Existing Parallel (Part 2)**
- **Approach**: Already parallelized, ensure optimal thread pool size
- **Complexity**: Low
- **Benefit**: Low - already well parallelized

---

## Day 11: GraphPathCounter

### Current Implementation
- Part 1: Sequential recursive path counting with memoization
- Part 2: Sequential path counting for multiple pairs

### Parallelization Opportunities

#### Technique 1: **Parallel Path Pair Processing (Part 2)**
- **Approach**: Process the three path pairs in parallel using `CompletableFuture.allOf()`
- **Complexity**: Low-Medium
- **Benefit**: High - path pairs are independent
- **Implementation**: 
  ```java
  CompletableFuture<Long> f1 = CompletableFuture.supplyAsync(() -> countPaths(...));
  CompletableFuture<Long> f2 = CompletableFuture.supplyAsync(() -> countPaths(...));
  CompletableFuture<Long> f3 = CompletableFuture.supplyAsync(() -> countPaths(...));
  long product = f1.join() * f2.join() * f3.join();
  ```

#### Technique 2: **Thread-safe Memoization**
- **Approach**: Use `ConcurrentHashMap` for memo cache to enable parallel path exploration
- **Complexity**: Low
- **Benefit**: Medium - enables parallel recursive calls
- **Note**: Each path pair already uses separate memo, but could parallelize within a single path exploration

#### Technique 3: **Parallel Neighbor Exploration**
- **Approach**: When exploring neighbors, process them in parallel
- **Complexity**: Medium-High
- **Benefit**: Medium - depends on graph structure and memoization effectiveness

---

## Day 12: ShapePacking

### Current Implementation
- Part 1: **Already uses `.parallelStream()`** ✓ for region processing
- Each region is processed independently

### Parallelization Opportunities

#### Technique 1: **Optimize Existing Parallel**
- **Approach**: Already parallelized at region level, but could optimize:
  - Ensure optimal thread pool sizing
  - Consider work-stealing optimizations
- **Complexity**: Low
- **Benefit**: Low-Medium - already well parallelized

#### Technique 2: **Parallel Variant Processing**
- **Approach**: Within backtracking, process variant placements in parallel
- **Complexity**: High - requires thread-safe grid and memo
- **Benefit**: Medium - but synchronization overhead may outweigh benefits
- **Note**: Backtracking is inherently sequential, parallelization difficult

#### Technique 3: **Parallel Position Generation**
- **Approach**: Generate positions in parallel before backtracking
- **Complexity**: Low
- **Benefit**: Low - position generation is fast, not a bottleneck

---

## Summary by Technique

### 1. **Parallel Streams** (Easiest, High Impact)
- **Days**: 3, 4 (Part 1), 5 (Part 1), 9 (Part 1), 10 (Part 1)
- **Complexity**: Very Low
- **Benefit**: Medium-High
- **Implementation**: Add `.parallel()` or use `.parallelStream()`

### 2. **Parallel Edge/Point Pair Computation** (High Impact)
- **Days**: 8, 9
- **Complexity**: Medium
- **Benefit**: Very High (O(n²) operations)
- **Implementation**: Parallel streams with thread-safe collections

### 3. **Parallel Range/ID Processing** (High Impact)
- **Days**: 2, 5
- **Complexity**: Low-Medium
- **Benefit**: High (CPU-intensive independent operations)
- **Implementation**: Parallel streams or Fork/Join

### 4. **Parallel Block Processing** (Medium Impact)
- **Days**: 6
- **Complexity**: Medium
- **Benefit**: High (independent blocks)
- **Implementation**: Collect blocks, then parallel stream

### 5. **Thread-safe Memoization** (Medium Impact)
- **Days**: 7, 11
- **Complexity**: Low-Medium
- **Benefit**: Medium (enables parallel recursive calls)
- **Implementation**: Replace `HashMap` with `ConcurrentHashMap`

### 6. **Parallel Path Processing** (Medium Impact)
- **Days**: 7, 11
- **Complexity**: Medium-High
- **Benefit**: Medium-High (independent path exploration)
- **Implementation**: `CompletableFuture` or parallel streams with thread-safe memo

### 7. **Parallel Sorting** (Low-Medium Impact)
- **Days**: 5 (Part 2), 8 (Part 2)
- **Complexity**: Very Low
- **Benefit**: Medium (only for large datasets)
- **Implementation**: `Arrays.parallelSort()` or `Collections.parallelSort()`

### 8. **Fork/Join Custom Tasks** (High Complexity, High Control)
- **Days**: 1, 2, 6
- **Complexity**: High
- **Benefit**: High (fine-grained control)
- **Implementation**: Custom `RecursiveTask` or `RecursiveAction`

---

## Recommendations by Priority

### **High Priority** (Easy wins, high impact)
1. **Day 3**: Add `.parallel()` to stream
2. **Day 9 Part 1**: Add `.parallel()` to stream
3. **Day 10 Part 1**: Change to `.parallelStream()`
4. **Day 2**: Parallelize range/ID processing
5. **Day 5 Part 1**: Parallelize ID checking

### **Medium Priority** (Moderate effort, good impact)
1. **Day 8**: Parallelize edge computation (very high benefit for O(n²))
2. **Day 4 Part 1**: Parallelize cell neighbor checking
3. **Day 6**: Parallelize block processing
4. **Day 11 Part 2**: Parallelize path pair processing

### **Low Priority** (Complex or limited benefit)
1. **Day 1**: Chunk-based processing (complex state merging)
2. **Day 7**: Parallel path exploration (requires careful synchronization)
3. **Day 12**: Already parallelized, minor optimizations only

---

## Thread Safety Considerations

When parallelizing, ensure:
1. **Immutable data structures** are used where possible (already done in most cases)
2. **Read-only shared data** (like grids, graphs) can be safely accessed in parallel
3. **Mutable shared state** requires synchronization (`ConcurrentHashMap`, `synchronized` blocks)
4. **Local variables** are thread-safe (each thread has its own stack)
5. **Collectors** should be thread-safe or use `Collectors.toConcurrentMap()`

---

## Performance Considerations

1. **Overhead**: Parallel streams have overhead - only beneficial for sufficient work per element
2. **Thread Pool**: Default `ForkJoinPool.commonPool()` may need tuning for CPU-bound tasks
3. **Memory**: Parallel processing may increase memory usage (multiple threads processing simultaneously)
4. **Cache Locality**: Sequential processing may have better cache performance
5. **Load Balancing**: Ensure work is evenly distributed across threads

---

## Testing Recommendations

After parallelization:
1. Verify correctness (results match sequential version)
2. Measure performance improvement (benchmark with JMH)
3. Test with various input sizes
4. Monitor thread pool utilization
5. Check for race conditions or deadlocks
