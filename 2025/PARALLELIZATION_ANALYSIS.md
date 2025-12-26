# Parallelization Analysis for 2025 Module

This document analyzes parallelization opportunities across all days in the 2025 module, identifying different techniques that could be applied.

## Overview

The analysis covers 12 days of Advent of Code problems, examining:
- **Data Parallelism**: Processing independent data elements in parallel
- **Task Parallelism**: Executing independent tasks concurrently
- **Pipeline Parallelism**: Overlapping computation stages
- **Stream Parallelization**: Using Java parallel streams
- **Fork/Join**: Recursive task decomposition
- **Structured Concurrency**: Modern task lifecycle management (Java 21+)
- **Thread-safe Collections**: Concurrent data structures for shared state

---

## Day 1: DialRotator3

### Current Implementation
- Sequential processing of rotation lines
- Stateful: maintains dial position across iterations
- Each line operation depends on previous position

### Parallelization Opportunities

#### Technique 1: **Fork/Join RecursiveTask with State Merging**
- **Approach**: Custom `RecursiveTask<DialResult>` that processes chunks independently and merges state
- **Complexity**: High - requires careful handling of zero crossings at chunk boundaries
- **Benefit**: Medium-High - depends on input size and chunk granularity
- **Implementation Example**:
  ```java
  class DialRotationTask extends RecursiveTask<DialResult> {
      private final List<String> lines;
      private final int start, end;
      private final int initialPosition;
      private static final int THRESHOLD = 1000;
      
      record DialResult(int finalPosition, int zeroCount) {}
      
      @Override
      protected DialResult compute() {
          if (end - start <= THRESHOLD) {
              int position = initialPosition;
              int zeroCount = 0;
              for (int i = start; i < end; i++) {
                  String line = lines.get(i);
                  if (isValidRotation(line)) {
                      // Process rotation and count zeros
                      // ...
                  }
              }
              return new DialResult(position, zeroCount);
          } else {
              int mid = (start + end) / 2;
              DialRotationTask left = new DialRotationTask(lines, start, mid, initialPosition);
              DialRotationTask right = new DialRotationTask(lines, mid, end, initialPosition);
              left.fork();
              DialResult rightResult = right.compute();
              DialResult leftResult = left.join();
              // Merge: right starts from left's final position
              DialResult mergedRight = processWithInitialPosition(
                  lines, mid, end, leftResult.finalPosition());
              return new DialResult(
                  mergedRight.finalPosition(),
                  leftResult.zeroCount() + mergedRight.zeroCount()
              );
          }
      }
  }
  ```

#### Technique 2: **Parallel Stream with Custom Collector for State**
- **Approach**: Use parallel stream with custom collector that maintains dial state
- **Complexity**: Medium
- **Benefit**: Medium - simpler than Fork/Join but less control
- **Implementation**: Custom collector with thread-local state accumulators

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

#### Technique 3: **Fork/Join RecursiveTask for Range Processing**
- **Approach**: Custom `RecursiveTask<Long>` that recursively splits ranges and IDs
- **Complexity**: Medium - requires custom implementation
- **Benefit**: High - fine-grained control over task splitting, optimal work-stealing
- **Use Case**: When ranges vary significantly in size
- **Implementation Example**:
  ```java
  class RangeValidationTask extends RecursiveTask<Long> {
      private final List<Range> ranges;
      private final int start, end;
      private final LongPredicate validator;
      private static final int THRESHOLD = 1000; // ranges per task
      
      RangeValidationTask(List<Range> ranges, int start, int end, LongPredicate validator) {
          this.ranges = ranges;
          this.start = start;
          this.end = end;
          this.validator = validator;
      }
      
      @Override
      protected Long compute() {
          if (end - start <= THRESHOLD) {
              // Sequential processing for small chunks
              long sum = 0;
              for (int i = start; i < end; i++) {
                  Range range = ranges.get(i);
                  for (long id = range.start(); id <= range.end(); id++) {
                      if (validator.test(id)) {
                          sum += id;
                      }
                  }
              }
              return sum;
          } else {
              // Split and fork
              int mid = (start + end) / 2;
              RangeValidationTask left = new RangeValidationTask(ranges, start, mid, validator);
              RangeValidationTask right = new RangeValidationTask(ranges, mid, end, validator);
              left.fork();
              long rightResult = right.compute();
              long leftResult = left.join();
              return leftResult + rightResult;
          }
      }
  }
  ```

#### Technique 4: **Fork/Join with Nested ID Splitting**
- **Approach**: Recursively split large ranges into smaller ID chunks
- **Complexity**: Medium-High
- **Benefit**: Very High - optimal for very large ranges
- **Implementation**: `RecursiveTask` that splits both ranges and ID ranges within ranges

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

#### Technique 2: **Structured Concurrency for Bank Processing**
- **Approach**: Use `StructuredTaskScope` to process multiple banks concurrently
- **Complexity**: Low-Medium
- **Benefit**: Medium-High - better error handling and cancellation than CompletableFuture
- **Implementation Example**:
  ```java
  try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      List<Subtask<Long>> tasks = banks.stream()
          .map(bank -> scope.fork(() -> bank.getMaxJoltage(length)))
          .toList();
      
      scope.join();
      scope.throwIfFailed();
      
      return tasks.stream()
          .mapToLong(Subtask::get)
          .sum();
  }
  ```

#### Technique 2b: **Parallel Stream Alternative**
- **Approach**: Process multiple banks concurrently using parallel streams
- **Complexity**: Low
- **Benefit**: Medium - simpler but less control over error handling

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

#### Technique 1: **Parallel ID Checking with Parallel Stream**
- **Approach**: `ids.parallelStream().filter(id -> containsId(intervals, id)).count()`
- **Complexity**: Low
- **Benefit**: High - ID checking is independent and CPU-intensive
- **Implementation**: Convert `LongList` iteration to parallel stream

#### Technique 1b: **Fork/Join RecursiveTask for ID Checking**
- **Approach**: Custom `RecursiveTask<Long>` that splits ID ranges recursively
- **Complexity**: Medium
- **Benefit**: Very High - optimal for large ID lists, fine-grained work-stealing
- **Implementation Example**:
  ```java
  class IdCheckingTask extends RecursiveTask<Long> {
      private final LongList ids;
      private final List<Interval> intervals;
      private final int start, end;
      private static final int THRESHOLD = 10000;
      
      @Override
      protected Long compute() {
          if (end - start <= THRESHOLD) {
              long count = 0;
              for (int i = start; i < end; i++) {
                  if (containsId(intervals, ids.getLong(i))) {
                      count++;
                  }
              }
              return count;
          } else {
              int mid = (start + end) / 2;
              IdCheckingTask left = new IdCheckingTask(ids, intervals, start, mid);
              IdCheckingTask right = new IdCheckingTask(ids, intervals, mid, end);
              left.fork();
              return right.compute() + left.join();
          }
      }
  }
  ```

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

#### Technique 3: **Fork/Join RecursiveTask for Block Processing**
- **Approach**: Custom `RecursiveTask<Long>` that recursively processes blocks
- **Complexity**: Medium
- **Benefit**: High - fine-grained work-stealing for variable-sized blocks
- **Implementation Example**:
  ```java
  class BlockProcessingTask extends RecursiveTask<Long> {
      private final List<Integer[]> blocks;
      private final int start, end;
      private final BiFunction<List<String>, Integer[], Long> processor;
      private final List<String> lines;
      private static final int THRESHOLD = 10; // blocks per task
      
      @Override
      protected Long compute() {
          if (end - start <= THRESHOLD) {
              long sum = 0;
              for (int i = start; i < end; i++) {
                  sum += processor.apply(lines, blocks.get(i));
              }
              return sum;
          } else {
              int mid = (start + end) / 2;
              BlockProcessingTask left = new BlockProcessingTask(blocks, start, mid, processor, lines);
              BlockProcessingTask right = new BlockProcessingTask(blocks, mid, end, processor, lines);
              left.fork();
              return right.compute() + left.join();
          }
      }
  }
  ```

---

## Day 7: BeamPathCounter

### Current Implementation
- Part 1: Recursive beam tracking with state
- Part 2: Recursive path counting with memoization

### Parallelization Opportunities

#### Technique 1: **Fork/Join RecursiveTask for Path Exploration (Part 2)**
- **Approach**: Custom `RecursiveTask<Long>` that recursively explores paths in parallel
- **Complexity**: High - requires thread-safe memoization
- **Benefit**: Very High - optimal work-stealing for tree-like path exploration
- **Implementation Example**:
  ```java
  class PathCountingTask extends RecursiveTask<Long> {
      private final Grid grid;
      private final int x, y;
      private final Map<Point, Long> memo; // ConcurrentHashMap
      private static final int SPLIT_THRESHOLD = 2; // minimum depth to fork
      
      @Override
      protected Long compute() {
          if (y >= grid.maxY()) return 1L;
          
          Point point = Point.of(x, y);
          Long cached = memo.get(point);
          if (cached != null) return cached;
          
          CellType cellType = CellType.from(grid.get(point));
          long result;
          
          if (cellType == CellType.SPLITTER) {
              // Fork both paths
              PathCountingTask left = new PathCountingTask(grid, x - 1, y + 1, memo);
              PathCountingTask right = new PathCountingTask(grid, x + 1, y + 1, memo);
              left.fork();
              result = right.compute() + left.join();
          } else {
              result = new PathCountingTask(grid, x, y + 1, memo).compute();
          }
          
          memo.put(point, result);
          return result;
      }
  }
  ```

#### Technique 2: **Structured Concurrency for Path Exploration**
- **Approach**: Use `StructuredTaskScope` when beam splits to explore paths concurrently
- **Complexity**: Medium-High
- **Benefit**: High - path exploration is independent after splits, automatic cancellation
- **Implementation Example**:
  ```java
  private long countPathsWithStructuredConcurrency(Grid grid, int x, int y, Map<Point, Long> memo) {
      if (y >= grid.maxY()) return 1L;
      
      Point point = Point.of(x, y);
      Long cached = memo.get(point);
      if (cached != null) return cached;
      
      CellType cellType = CellType.from(grid.get(point));
      long result;
      
      if (cellType == CellType.SPLITTER) {
          try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
              Subtask<Long> left = scope.fork(() -> 
                  countPathsWithStructuredConcurrency(grid, x - 1, y + 1, memo));
              Subtask<Long> right = scope.fork(() -> 
                  countPathsWithStructuredConcurrency(grid, x + 1, y + 1, memo));
              
              scope.join();
              scope.throwIfFailed();
              result = left.get() + right.get();
          }
      } else {
          result = countPathsWithStructuredConcurrency(grid, x, y + 1, memo);
      }
      
      memo.put(point, result);
      return result;
  }
  ```

#### Technique 2b: **CompletableFuture Alternative**
- **Approach**: When beam splits, explore both paths using `CompletableFuture`
- **Complexity**: Medium-High
- **Benefit**: High - path exploration is independent after splits
- **Implementation**: Use `ConcurrentHashMap` for memo, parallel recursive calls with `CompletableFuture`
- **Note**: Structured concurrency (Technique 2) is preferred for better error handling

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

#### Technique 2: **Parallel Edge Computation with Custom Parallel Collector**
- **Approach**: Use custom `Collector` that combines thread-local collections efficiently
- **Complexity**: Medium
- **Benefit**: Very High - eliminates contention, optimal for parallel collection
- **Implementation Example**:
  ```java
  Collector<Edge, ?, ObjectList<Edge>> edgeCollector = Collector.of(
      () -> new ObjectArrayList<>(),  // Supplier: thread-local accumulator
      ObjectArrayList::add,            // Accumulator: add to local list
      (left, right) -> {               // Combiner: merge two lists
          left.addAll(right);
          return left;
      },
      Collector.Characteristics.CONCURRENT,
      Collector.Characteristics.UNORDERED
  );
  
  ObjectList<Edge> edges = IntStream.range(0, n)
      .parallel()
      .boxed()
      .flatMap(i -> IntStream.range(i + 1, n)
          .mapToObj(j -> new Edge(i, j, distance(points.get(i), points.get(j)))))
      .collect(edgeCollector);
  ```

#### Technique 3: **Fork/Join RecursiveTask for Edge Computation**
- **Approach**: Custom `RecursiveTask<ObjectList<Edge>>` that recursively computes edge pairs
- **Complexity**: Medium-High
- **Benefit**: Very High - optimal work-stealing for O(n²) computation
- **Implementation Example**:
  ```java
  class EdgeComputationTask extends RecursiveTask<ObjectList<Edge>> {
      private final ObjectList<Point3D> points;
      private final int startI, endI;
      private static final int THRESHOLD = 100; // points per task
      
      @Override
      protected ObjectList<Edge> compute() {
          if (endI - startI <= THRESHOLD) {
              ObjectList<Edge> edges = new ObjectArrayList<>();
              for (int i = startI; i < endI; i++) {
                  Point3D p1 = points.get(i);
                  for (int j = i + 1; j < points.size(); j++) {
                      Point3D p2 = points.get(j);
                      edges.add(new Edge(i, j, p1.distanceSquared(p2)));
                  }
              }
              return edges;
          } else {
              int mid = (startI + endI) / 2;
              EdgeComputationTask left = new EdgeComputationTask(points, startI, mid);
              EdgeComputationTask right = new EdgeComputationTask(points, mid, endI);
              left.fork();
              ObjectList<Edge> rightEdges = right.compute();
              ObjectList<Edge> leftEdges = left.join();
              leftEdges.addAll(rightEdges);
              return leftEdges;
          }
      }
  }
  ```

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

#### Technique 1: **Structured Concurrency for Path Pair Processing (Part 2)**
- **Approach**: Use `StructuredTaskScope` to manage independent path counting tasks
- **Complexity**: Low-Medium
- **Benefit**: High - path pairs are independent, better error handling and cancellation
- **Implementation Example**:
  ```java
  try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      Subtask<Long> f1 = scope.fork(() -> countPaths(pair1.from(), pair1.to(), graph, new HashMap<>()));
      Subtask<Long> f2 = scope.fork(() -> countPaths(pair2.from(), pair2.to(), graph, new HashMap<>()));
      Subtask<Long> f3 = scope.fork(() -> countPaths(pair3.from(), pair3.to(), graph, new HashMap<>()));
      
      scope.join();  // Wait for all tasks
      scope.throwIfFailed();  // Propagate any exceptions
      
      long product = f1.get() * f2.get() * f3.get();
      return product;
  }
  ```
- **Benefits over CompletableFuture**:
  - Automatic cancellation if one task fails
  - Better error propagation
  - Clear task lifecycle management
  - No need for manual exception handling

#### Technique 1b: **CompletableFuture Alternative (Part 2)**
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
- **Note**: Structured concurrency (Technique 1) is preferred for Java 21+ as it provides better error handling and cancellation

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

### 6. **Structured Concurrency** (Medium Impact, Modern Approach)
- **Days**: 7, 10, 11
- **Complexity**: Low-Medium
- **Benefit**: High (better error handling, cancellation, lifecycle management)
- **Implementation**: `StructuredTaskScope` for managing independent tasks
- **When to Use**: 
  - Multiple independent tasks that need coordination
  - Need automatic cancellation on failure
  - Want better error propagation
  - Java 21+ available

### 7. **Parallel Path Processing** (Medium Impact)
- **Days**: 7, 11
- **Complexity**: Medium-High
- **Benefit**: Medium-High (independent path exploration)
- **Implementation**: `StructuredTaskScope` (preferred) or `CompletableFuture` or parallel streams with thread-safe memo

### 8. **Parallel Sorting** (Low-Medium Impact)
- **Days**: 5 (Part 2), 8 (Part 2)
- **Complexity**: Very Low
- **Benefit**: Medium (only for large datasets)
- **Implementation**: `Arrays.parallelSort()` or `Collections.parallelSort()`

### 9. **Fork/Join RecursiveTask** (High Complexity, High Control)
- **Days**: 1, 2, 6, 7, 8
- **Complexity**: Medium-High
- **Benefit**: Very High (fine-grained control, optimal work-stealing)
- **Implementation**: Custom `RecursiveTask<T>` extending `ForkJoinTask`
- **When to Use**:
  - Variable-sized work chunks
  - Recursive divide-and-conquer algorithms
  - Need fine-grained control over splitting
  - Work-stealing optimization needed

### 10. **Parallel Collectors** (Medium Complexity, High Performance)
- **Days**: 2, 5, 6, 8
- **Complexity**: Medium
- **Benefit**: High (eliminates contention, optimal parallel collection)
- **Implementation**: Custom `Collector` with thread-local accumulators
- **Types**:
  - **Thread-local collectors**: Each thread accumulates to local collection, merge at end
  - **Concurrent collectors**: Use `ConcurrentHashMap`, `ConcurrentLinkedQueue`
  - **Custom combiners**: Efficient merging strategies for specific data structures

---

## Theoretical Performance Improvement Estimates

### Methodology

Performance improvements are estimated using **Amdahl's Law** and practical considerations:

**Amdahl's Law**: `Speedup = 1 / ((1 - P) + P/N)`
- `P` = proportion of code that can be parallelized
- `N` = number of processors/threads

**Assumptions:**
- Typical system: 8-16 CPU cores
- Overhead: 5-15% for parallel streams, 10-20% for Fork/Join
- Memory bandwidth: May become bottleneck for memory-intensive operations
- Cache effects: Sequential may have better cache locality

**Estimate Categories:**
- **Ideal Speedup**: Theoretical maximum (ignoring overhead)
- **Realistic Speedup**: Accounting for overhead, contention, and real-world factors
- **Conditions**: When improvement is most/least significant

---

### Day-by-Day Performance Estimates

#### Day 1: DialRotator3

**Technique**: Fork/Join with State Merging
- **Parallelizable Portion**: ~85% (rotation processing)
- **Sequential Portion**: ~15% (state merging, zero crossing calculations)
- **Ideal Speedup**: 4-6x (8 cores)
- **Realistic Speedup**: **2.5-4x** (accounting for state merging overhead)
- **Conditions**:
  - Best: Large input files (>10K lines)
  - Worst: Small inputs (<1K lines) - overhead dominates
- **Factors**: State merging complexity, zero crossing boundary calculations

#### Day 2: InvalidIdValidator2

**Technique**: Parallel Stream on Range/ID Processing
- **Parallelizable Portion**: ~95% (ID validation is independent)
- **Sequential Portion**: ~5% (range parsing, result aggregation)
- **Ideal Speedup**: 6-7x (8 cores)
- **Realistic Speedup**: **4-6x** (CPU-intensive validation benefits well)
- **Conditions**:
  - Best: Many ranges with large ID counts (>100K IDs total)
  - Worst: Few ranges with small ID counts (<10K IDs total)
- **Factors**: ID validation complexity (Part 2 more complex = better speedup)

**Technique**: Fork/Join RecursiveTask
- **Realistic Speedup**: **5-7x** (better work-stealing for variable-sized ranges)
- **Best For**: Highly variable range sizes

#### Day 3: MaxJoltage

**Technique**: Parallel Stream Processing
- **Parallelizable Portion**: ~98% (line processing is independent)
- **Sequential Portion**: ~2% (file I/O, result aggregation)
- **Ideal Speedup**: 7-7.5x (8 cores)
- **Realistic Speedup**: **5-7x** (minimal overhead, independent work)
- **Conditions**:
  - Best: Many lines (>1000 lines)
  - Worst: Few lines (<100 lines) - overhead not worth it
- **Factors**: Computation per line (Part 2 with length 12 = better speedup)

#### Day 4: GridNeighbor2

**Part 1 - Parallel Stream for Cell Filtering:**
- **Parallelizable Portion**: ~90% (neighbor checking per cell)
- **Sequential Portion**: ~10% (grid creation, result collection)
- **Ideal Speedup**: 5-6x (8 cores)
- **Realistic Speedup**: **3.5-5x** (memory access patterns affect cache)
- **Conditions**:
  - Best: Large grids (>100x100 cells)
  - Worst: Small grids (<50x50 cells)
- **Factors**: Grid size, neighbor access patterns

**Part 2 - Parallel Cell Removal Detection:**
- **Realistic Speedup**: **2-3x** (only detection parallelized, removal sequential)
- **Limitation**: Iterative dependencies reduce parallelization benefit

#### Day 5: Range3

**Part 1 - Parallel ID Checking:**
- **Parallelizable Portion**: ~92% (ID checking against intervals)
- **Sequential Portion**: ~8% (interval parsing, result aggregation)
- **Ideal Speedup**: 6-7x (8 cores)
- **Realistic Speedup**: **4-6x** (CPU-intensive interval checking)
- **Conditions**:
  - Best: Many IDs (>100K IDs) with many intervals (>100 intervals)
  - Worst: Few IDs (<10K IDs)
- **Factors**: Number of intervals (more intervals = more work per ID)

**Part 2 - Parallel Interval Sorting:**
- **Realistic Speedup**: **2-3x** (only sorting parallelized, merging sequential)
- **Best For**: Very large interval lists (>10K intervals)

#### Day 6: MathBlock

**Technique**: Parallel Block Processing
- **Parallelizable Portion**: ~95% (block processing is independent)
- **Sequential Portion**: ~5% (block detection, result aggregation)
- **Ideal Speedup**: 6-7x (8 cores)
- **Realistic Speedup**: **4-6x** (independent blocks, minimal overhead)
- **Conditions**:
  - Best: Many blocks (>50 blocks)
  - Worst: Few blocks (<10 blocks)
- **Factors**: Block size variability (more uniform = better load balancing)

**Technique**: Fork/Join RecursiveTask
- **Realistic Speedup**: **5-7x** (better for variable-sized blocks)

#### Day 7: BeamPathCounter

**Part 2 - Fork/Join Path Exploration:**
- **Parallelizable Portion**: ~70-85% (varies with splitter density)
- **Sequential Portion**: ~15-30% (memoization overhead, sequential paths)
- **Ideal Speedup**: 4-6x (8 cores, depends on graph structure)
- **Realistic Speedup**: **3-5x** (memoization contention, tree structure)
- **Conditions**:
  - Best: High splitter density (many parallel paths)
  - Worst: Low splitter density (mostly sequential paths)
- **Factors**: 
  - Memoization effectiveness (more cache hits = less benefit)
  - Graph structure (balanced tree = better speedup)

**Technique**: Structured Concurrency
- **Realistic Speedup**: **2.5-4x** (similar to Fork/Join, better error handling)

#### Day 8: PointCluster3

**Technique**: Parallel Edge Computation
- **Parallelizable Portion**: ~98% (O(n²) pair computation)
- **Sequential Portion**: ~2% (point parsing, result collection)
- **Ideal Speedup**: 7-7.5x (8 cores)
- **Realistic Speedup**: **6-8x** (highly parallelizable, CPU-intensive)
- **Conditions**:
  - Best: Large point sets (>1000 points = >500K edges)
  - Worst: Small point sets (<100 points)
- **Factors**: 
  - Point count (O(n²) means massive benefit for large n)
  - Distance calculation complexity

**Part 1 - Parallel Top-K Selection:**
- **Realistic Speedup**: **5-7x** (edge computation dominates)

**Part 2 - Parallel Sorting:**
- **Realistic Speedup**: **3-5x** (for large edge lists >100K edges)

#### Day 9: MaxRectangleArea

**Part 1 - Parallel Stream:**
- **Parallelizable Portion**: ~95% (pair generation and area calculation)
- **Sequential Portion**: ~5% (point parsing, max finding)
- **Ideal Speedup**: 6-7x (8 cores)
- **Realistic Speedup**: **4-6x** (independent pair processing)
- **Conditions**:
  - Best: Many points (>500 points = >125K pairs)
  - Worst: Few points (<50 points)

**Part 2 - Already Parallelized:**
- **Current Speedup**: ~4-6x (already using `.parallel()`)
- **Potential Improvement**: **+10-20%** (optimization only)

#### Day 10: ButtonPressOptimizer

**Part 1 - Parallel Stream:**
- **Parallelizable Portion**: ~96% (line processing is independent)
- **Sequential Portion**: ~4% (file I/O, result aggregation)
- **Ideal Speedup**: 6-7x (8 cores)
- **Realistic Speedup**: **4-6x** (independent line processing)
- **Conditions**:
  - Best: Many lines (>1000 lines)
  - Worst: Few lines (<100 lines)

**Part 2 - Already Parallelized:**
- **Current Speedup**: ~4-6x (already using `.parallelStream()`)

**Technique**: Structured Concurrency
- **Realistic Speedup**: **4-6x** (similar to parallel stream, better error handling)

#### Day 11: GraphPathCounter

**Part 2 - Structured Concurrency for Path Pairs:**
- **Parallelizable Portion**: ~100% (6 independent path counts)
- **Sequential Portion**: ~0% (only result multiplication)
- **Ideal Speedup**: 6x (6 independent tasks on 8 cores)
- **Realistic Speedup**: **4-5x** (limited by number of tasks, not cores)
- **Conditions**:
  - Best: All path counts take similar time
  - Worst: One path count dominates (Amdahl's Law)
- **Factors**: 
  - Path count computation time (more uniform = better speedup)
  - Graph structure (affects individual path computation time)

**Technique**: Fork/Join for Path Exploration
- **Realistic Speedup**: **3-5x** (depends on graph structure and memoization)

#### Day 12: ShapePacking

**Already Parallelized:**
- **Current Speedup**: ~4-6x (already using `.parallelStream()` for regions)
- **Potential Improvement**: **+5-15%** (thread pool tuning, work distribution)

**Technique**: Parallel Variant Processing (within backtracking)
- **Realistic Speedup**: **1.5-2x** (synchronization overhead limits benefit)
- **Note**: Backtracking is inherently sequential, parallelization difficult

---

### Summary of Expected Improvements

| Day | Technique | Realistic Speedup | Best Case | Worst Case |
|-----|-----------|------------------|-----------|------------|
| **Day 1** | Fork/Join State Merging | **2.5-4x** | Large inputs | Small inputs |
| **Day 2** | Parallel Stream | **4-6x** | Many large ranges | Few small ranges |
| **Day 2** | Fork/Join | **5-7x** | Variable ranges | Uniform ranges |
| **Day 3** | Parallel Stream | **5-7x** | Many lines | Few lines |
| **Day 4 P1** | Parallel Stream | **3.5-5x** | Large grids | Small grids |
| **Day 4 P2** | Parallel Detection | **2-3x** | Large grids | Small grids |
| **Day 5 P1** | Parallel ID Check | **4-6x** | Many IDs/intervals | Few IDs |
| **Day 5 P2** | Parallel Sort | **2-3x** | Many intervals | Few intervals |
| **Day 6** | Parallel Blocks | **4-6x** | Many blocks | Few blocks |
| **Day 6** | Fork/Join | **5-7x** | Variable blocks | Uniform blocks |
| **Day 7** | Fork/Join Paths | **3-5x** | High splitter density | Low splitter density |
| **Day 8** | Parallel Edges | **6-8x** | Large point sets | Small point sets |
| **Day 9 P1** | Parallel Stream | **4-6x** | Many points | Few points |
| **Day 10 P1** | Parallel Stream | **4-6x** | Many lines | Few lines |
| **Day 11 P2** | Structured Concurrency | **4-5x** | Uniform path times | One dominates |
| **Day 12** | Already optimized | **+5-15%** | - | - |

### Factors Affecting Speedup

#### Positive Factors (Increase Speedup)
1. **Large Dataset Size**: More work = better amortization of overhead
2. **CPU-Intensive Operations**: Computation-bound tasks benefit more
3. **Independent Work**: No dependencies between parallel tasks
4. **Uniform Work Distribution**: Even load across threads
5. **Good Cache Locality**: Each thread works on local data
6. **Minimal Synchronization**: Low contention on shared resources

#### Negative Factors (Decrease Speedup)
1. **Small Dataset Size**: Overhead dominates benefits
2. **I/O-Bound Operations**: Limited by I/O bandwidth, not CPU
3. **Sequential Dependencies**: Must wait for previous results
4. **Uneven Work Distribution**: Some threads finish early (load imbalance)
5. **High Contention**: Many threads competing for shared resources
6. **Memory Bandwidth Limits**: Memory becomes bottleneck
7. **Cache Thrashing**: Poor cache locality reduces performance

### Amdahl's Law Application

**Example: Day 8 Edge Computation**
- Parallelizable: 98%
- Sequential: 2%
- Speedup = 1 / (0.02 + 0.98/8) = 1 / 0.1425 = **7.0x** (ideal)
- With 15% overhead: **6.0x** (realistic)

**Example: Day 1 State Merging**
- Parallelizable: 85%
- Sequential: 15%
- Speedup = 1 / (0.15 + 0.85/8) = 1 / 0.256 = **3.9x** (ideal)
- With 20% overhead: **2.5-3x** (realistic, accounting for state merging complexity)

### Real-World Considerations

1. **Overhead Costs**:
   - Parallel stream creation: ~1-5ms
   - Thread creation/context switching: ~10-50μs per thread
   - Synchronization: Variable, depends on contention

2. **Memory Effects**:
   - Parallel processing increases memory usage (multiple threads)
   - May cause cache misses if data doesn't fit in cache
   - Memory bandwidth can become bottleneck

3. **Scalability Limits**:
   - Most improvements plateau around 8-16 cores
   - Beyond that, memory bandwidth and cache become limiting factors
   - Some algorithms scale better than others (embarrassingly parallel vs. complex dependencies)

4. **Input Size Thresholds**:
   - **Minimum**: ~1000-10000 elements for parallel streams to be beneficial
   - **Optimal**: 100K+ elements for maximum benefit
   - **Diminishing Returns**: Beyond 1M elements, other bottlenecks emerge

### Benchmarking Recommendations

To validate these estimates:
1. **Baseline**: Measure sequential performance first
2. **Parallel**: Measure parallel performance with same input
3. **Vary Input Size**: Test with small, medium, large inputs
4. **Multiple Runs**: Average over multiple runs to account for JIT warmup
5. **JMH**: Use Java Microbenchmark Harness for accurate measurements
6. **Profile**: Use JFR (Java Flight Recorder) to identify bottlenecks

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
4. **Day 11 Part 2**: Use structured concurrency for path pair processing
5. **Day 10 Part 1**: Use structured concurrency for line processing

### **Low Priority** (Complex or limited benefit)
1. **Day 1**: Fork/Join chunk-based processing (complex state merging)
2. **Day 7**: Fork/Join path exploration (requires careful synchronization)
3. **Day 12**: Already parallelized, minor optimizations only

---

## Implementation Examples Summary

### Quick Wins (Parallel Streams)
- Day 3, 9 Part 1, 10 Part 1: Add `.parallel()` or `.parallelStream()`
- Day 4 Part 1: `parallelStream()` for cell filtering
- Day 5 Part 1: `parallelStream()` for ID checking

### Medium Effort (Structured Concurrency - Java 21+)
- Day 10 Part 1: `StructuredTaskScope` for line processing
- Day 11 Part 2: `StructuredTaskScope` for path pair processing
- Day 7 Part 2: `StructuredTaskScope` for path exploration

### Medium Effort (Custom Collectors)
- Day 2: Parallel stream with `flatMapToLong()` and `sum()`
- Day 8: Custom parallel collector for `ObjectArrayList<Edge>`
- Day 6: Parallel stream with block collection

### High Effort (Fork/Join)
- Day 1: `RecursiveTask<DialResult>` with state merging
- Day 2: `RecursiveTask<Long>` for range/ID processing
- Day 6: `RecursiveTask<Long>` for block processing
- Day 7: `RecursiveTask<Long>` for path exploration
- Day 8: `RecursiveTask<ObjectList<Edge>>` for edge computation

---

## Fork/Join Framework Deep Dive

### When to Use Fork/Join vs Parallel Streams

**Use Fork/Join (`RecursiveTask`/`RecursiveAction`) when:**
- Need fine-grained control over task splitting
- Work chunks vary significantly in size
- Recursive algorithms with variable depth
- Custom work-stealing strategies needed
- Need to control thread pool size explicitly
- Complex state merging required

**Use Parallel Streams when:**
- Uniform work distribution
- Simple transformations/filtering
- Standard collection operations
- Want simplicity and readability

### Fork/Join Patterns

#### Pattern 1: Range Splitting
```java
class RangeTask extends RecursiveTask<Result> {
    private final int start, end;
    private static final int THRESHOLD = 1000;
    
    @Override
    protected Result compute() {
        if (end - start <= THRESHOLD) {
            return computeSequentially();
        }
        int mid = (start + end) / 2;
        RangeTask left = new RangeTask(start, mid);
        RangeTask right = new RangeTask(mid, end);
        left.fork();
        Result rightResult = right.compute();
        Result leftResult = left.join();
        return combine(leftResult, rightResult);
    }
}
```

#### Pattern 2: Tree Traversal
```java
class TreeTask extends RecursiveTask<Result> {
    private final Node node;
    
    @Override
    protected Result compute() {
        if (node.isLeaf()) {
            return processLeaf(node);
        }
        List<TreeTask> subtasks = node.children().stream()
            .map(TreeTask::new)
            .collect(Collectors.toList());
        invokeAll(subtasks);
        return combine(subtasks.stream().map(ForkJoinTask::join));
    }
}
```

#### Pattern 3: Divide and Conquer with State
```java
class StatefulTask extends RecursiveTask<Result> {
    private final Data data;
    private final State initialState;
    
    @Override
    protected Result compute() {
        if (shouldComputeSequentially(data)) {
            return computeWithState(data, initialState);
        }
        Pair<Data, Data> split = divide(data);
        StatefulTask left = new StatefulTask(split.left(), initialState);
        StatefulTask right = new StatefulTask(split.right(), mergeState(initialState, left.getState()));
        left.fork();
        Result rightResult = right.compute();
        Result leftResult = left.join();
        return combine(leftResult, rightResult);
    }
}
```

### Fork/Join Best Practices

1. **Threshold Selection**: Choose threshold based on:
   - Work per element (CPU-intensive = smaller threshold)
   - Overhead vs computation ratio
   - Typical data sizes
   - Benchmark to find optimal value

2. **Fork vs Compute**: 
   - Use `fork()` + `compute()` for asymmetric splits
   - Use `invokeAll()` for symmetric splits
   - Prefer `compute()` on one side to reduce thread creation

3. **Avoid Blocking**: Fork/Join threads are worker threads - blocking reduces parallelism

4. **Exception Handling**: Override `getException()` or use `quietlyJoin()` for error handling

---

## Parallel Collectors Deep Dive

### Standard Parallel Collectors

#### 1. **Concurrent Collectors** (Built-in)
```java
// Thread-safe concurrent collection
Map<K, V> map = stream.parallel()
    .collect(Collectors.toConcurrentMap(keyMapper, valueMapper));

// Concurrent grouping
Map<K, List<V>> grouped = stream.parallel()
    .collect(Collectors.groupingByConcurrent(classifier));
```

#### 2. **Thread-local Collectors** (Custom)
```java
Collector<T, ?, List<T>> threadLocalCollector = Collector.of(
    ArrayList::new,                    // Supplier: creates thread-local accumulator
    List::add,                         // Accumulator: adds to local list
    (left, right) -> {                 // Combiner: merges two lists
        left.addAll(right);
        return left;
    },
    Collector.Characteristics.CONCURRENT,  // Optional: concurrent characteristics
    Collector.Characteristics.UNORDERED     // Optional: order not preserved
);
```

#### 3. **FastUtil Parallel Collectors** (Custom for Day 8)
```java
// Custom collector for ObjectArrayList (FastUtil)
Collector<Edge, ?, ObjectList<Edge>> fastUtilCollector = Collector.of(
    ObjectArrayList::new,              // Supplier
    ObjectArrayList::add,              // Accumulator
    (left, right) -> {                 // Combiner
        left.addAll(right);
        return left;
    },
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
);
```

### Collector Characteristics

- **CONCURRENT**: Accumulator can be called concurrently from multiple threads
- **UNORDERED**: Order of elements not preserved
- **IDENTITY_FINISH**: Finisher is identity function (can be optimized away)

### When to Use Each Type

**Concurrent Collectors** (`toConcurrentMap`, `groupingByConcurrent`):
- When order doesn't matter
- High contention expected
- Map/grouping operations

**Thread-local Collectors**:
- When building lists/collections
- Want to minimize contention
- Need to merge at end
- Custom data structures (FastUtil, etc.)

**Standard Collectors** (non-concurrent):
- Sequential streams
- When order matters
- Low contention scenarios

### Performance Considerations

1. **Contention**: Concurrent collectors reduce contention but may have overhead
2. **Memory**: Thread-local collectors use more memory (one collection per thread)
3. **Merging Cost**: Consider cost of combiner function
4. **Characteristics**: Set appropriate characteristics for better optimization

---

## Thread Safety Considerations

When parallelizing, ensure:
1. **Immutable data structures** are used where possible (already done in most cases)
2. **Read-only shared data** (like grids, graphs) can be safely accessed in parallel
3. **Mutable shared state** requires synchronization (`ConcurrentHashMap`, `synchronized` blocks)
4. **Local variables** are thread-safe (each thread has its own stack)
5. **Collectors** should be thread-safe or use `Collectors.toConcurrentMap()`
6. **Fork/Join tasks** should avoid shared mutable state or use thread-safe structures
7. **Thread-local collections** eliminate contention but require efficient merging

---

## Structured Concurrency Deep Dive (Java 21+)

### Overview

Structured concurrency is a modern approach to managing concurrent tasks introduced in Java 21 (JEP 453). It provides a way to manage the lifecycle of concurrent tasks, ensuring that child tasks complete before their parent, with automatic cancellation and better error handling.

### Key Benefits

1. **Automatic Cancellation**: If one task fails, others are automatically cancelled
2. **Error Propagation**: Exceptions are properly propagated and handled
3. **Lifecycle Management**: Clear parent-child relationship between tasks
4. **Scoped Values**: Can use `ScopedValue` for thread-local-like values
5. **Platform Threads**: Works with both virtual threads and platform threads

### When to Use Structured Concurrency

**Use Structured Concurrency when:**
- Managing multiple independent tasks that need coordination
- Need automatic cancellation if one task fails
- Want better error handling than CompletableFuture
- Need clear task lifecycle management
- Java 21+ is available

**Don't use Structured Concurrency when:**
- Simple parallel streams suffice
- Need recursive task decomposition (use Fork/Join)
- Need fine-grained work-stealing (use Fork/Join)
- Java version < 21

### StructuredTaskScope Types

#### 1. ShutdownOnFailure
- Cancels all tasks if any task fails
- Use when: All tasks must succeed, or all should be cancelled

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> task1 = scope.fork(() -> compute1());
    Subtask<String> task2 = scope.fork(() -> compute2());
    
    scope.join();           // Wait for all tasks
    scope.throwIfFailed();  // Throw if any failed
    
    return task1.get() + task2.get();
}
```

#### 2. ShutdownOnSuccess
- Cancels remaining tasks when one succeeds
- Use when: Need first successful result (like `anyOf`)

```java
try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
    scope.fork(() -> tryMethod1());
    scope.fork(() -> tryMethod2());
    scope.fork(() -> tryMethod3());
    
    scope.join();
    return scope.result();  // First successful result
}
```

### Common Patterns

#### Pattern 1: Parallel Independent Tasks
```java
public Long processMultiplePairs(List<PathPair> pairs, Map<GraphNode, List<GraphNode>> graph) {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        List<Subtask<Long>> tasks = pairs.stream()
            .map(pair -> scope.fork(() -> 
                countPaths(pair.from(), pair.to(), graph, new HashMap<>())))
            .toList();
        
        scope.join();
        scope.throwIfFailed();
        
        return tasks.stream()
            .mapToLong(Subtask::get)
            .reduce(1L, (a, b) -> a * b);
    }
}
```

#### Pattern 2: First Success Pattern
```java
public String findFirstValid(List<String> inputs) {
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
        inputs.forEach(input -> 
            scope.fork(() -> validateAndProcess(input)));
        
        scope.join();
        return scope.result();  // First successful result
    }
}
```

#### Pattern 3: Parallel Processing with Error Handling
```java
public List<Result> processAll(List<Input> inputs) {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        List<Subtask<Result>> tasks = inputs.stream()
            .map(input -> scope.fork(() -> process(input)))
            .toList();
        
        scope.join();
        scope.throwIfFailed();  // Throws if any task failed
        
        return tasks.stream()
            .map(Subtask::get)
            .toList();
    } catch (Exception e) {
        // All tasks automatically cancelled
        // Handle error appropriately
        throw new ProcessingException("Failed to process inputs", e);
    }
}
```

#### Pattern 4: With Custom Executor (Platform Threads)
```java
ExecutorService executor = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors());

try (var scope = new StructuredTaskScope.ShutdownOnFailure(executor)) {
    Subtask<Long> task1 = scope.fork(() -> compute1());
    Subtask<Long> task2 = scope.fork(() -> compute2());
    
    scope.join();
    scope.throwIfFailed();
    
    return task1.get() + task2.get();
} finally {
    executor.shutdown();
}
```

### Structured Concurrency vs CompletableFuture

| Feature | Structured Concurrency | CompletableFuture |
|--------|----------------------|-------------------|
| **Error Handling** | Automatic cancellation, clear propagation | Manual handling required |
| **Lifecycle** | Clear parent-child relationship | Flat structure |
| **Cancellation** | Automatic on failure | Manual cancellation needed |
| **Exception Handling** | `throwIfFailed()` pattern | Manual `get()` exception handling |
| **Scoped Values** | Supports `ScopedValue` | No scoped value support |
| **Java Version** | 21+ | 8+ |
| **Complexity** | Lower (less boilerplate) | Higher (more manual management) |

### Best Practices

1. **Always use try-with-resources**: Ensures proper cleanup
2. **Call `throwIfFailed()`**: Propagate exceptions properly
3. **Use appropriate scope type**: `ShutdownOnFailure` vs `ShutdownOnSuccess`
4. **Custom executor for CPU-bound**: Use `ExecutorService` with platform threads
5. **Avoid blocking in tasks**: Use virtual threads or async operations
6. **Handle exceptions appropriately**: Catch and handle at appropriate level

### Platform Threads vs Virtual Threads

**Platform Threads** (OS threads):
- Use for CPU-bound tasks
- Limited by number of cores
- Use custom `ExecutorService` with `StructuredTaskScope`

**Virtual Threads** (Java 21+):
- Use for I/O-bound tasks
- Millions can be created
- Default for `StructuredTaskScope` (if using virtual thread executor)

**For 2025 module**: Most tasks are CPU-bound, so platform threads with custom executor are appropriate.

### Example: Day 11 Part 2 with Structured Concurrency

```java
@Override
public Long solvePartTwo(final String fileName) {
    final Map<GraphNode, List<GraphNode>> graph = parseInput(fileName);
    
    ExecutorService executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors());
    
    try (var scope = new StructuredTaskScope.ShutdownOnFailure(executor)) {
        // First product: three path pairs
        Subtask<Long> p1_1 = scope.fork(() -> 
            countPaths(GraphNode.from("svr"), GraphNode.from("dac"), graph, new HashMap<>()));
        Subtask<Long> p1_2 = scope.fork(() -> 
            countPaths(GraphNode.from("dac"), GraphNode.from("fft"), graph, new HashMap<>()));
        Subtask<Long> p1_3 = scope.fork(() -> 
            countPaths(GraphNode.from("fft"), GraphNode.from("out"), graph, new HashMap<>()));
        
        // Second product: three path pairs
        Subtask<Long> p2_1 = scope.fork(() -> 
            countPaths(GraphNode.from("svr"), GraphNode.from("fft"), graph, new HashMap<>()));
        Subtask<Long> p2_2 = scope.fork(() -> 
            countPaths(GraphNode.from("fft"), GraphNode.from("dac"), graph, new HashMap<>()));
        Subtask<Long> p2_3 = scope.fork(() -> 
            countPaths(GraphNode.from("dac"), GraphNode.from("out"), graph, new HashMap<>()));
        
        scope.join();
        scope.throwIfFailed();
        
        long firstProduct = p1_1.get() * p1_2.get() * p1_3.get();
        long secondProduct = p2_1.get() * p2_2.get() * p2_3.get();
        
        return firstProduct + secondProduct;
    } catch (Exception e) {
        throw new RuntimeException("Failed to compute paths", e);
    } finally {
        executor.shutdown();
    }
}
```

---

## Complete Fork/Join Implementation Guide

### Basic Fork/Join Setup

```java
// Create custom ForkJoinPool (optional - can use commonPool())
ForkJoinPool pool = new ForkJoinPool(
    Runtime.getRuntime().availableProcessors(),
    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
    null,
    true  // asyncMode for better performance
);

// Execute task
RecursiveTask<Result> task = new MyRecursiveTask(data);
Result result = pool.invoke(task);
```

### Common Fork/Join Patterns

#### Pattern 1: Array/List Processing
```java
class ArrayProcessingTask extends RecursiveTask<Long> {
    private final long[] array;
    private final int start, end;
    private static final int THRESHOLD = 1000;
    
    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // Sequential processing
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += process(array[i]);
            }
            return sum;
        }
        
        // Split and fork
        int mid = (start + end) / 2;
        ArrayProcessingTask left = new ArrayProcessingTask(array, start, mid);
        ArrayProcessingTask right = new ArrayProcessingTask(array, mid, end);
        left.fork();
        return right.compute() + left.join();
    }
}
```

#### Pattern 2: Nested Loop Parallelization (O(n²) operations)
```java
class PairProcessingTask extends RecursiveTask<ObjectList<Edge>> {
    private final ObjectList<Point> points;
    private final int startI, endI;
    private static final int THRESHOLD = 100;
    
    @Override
    protected ObjectList<Edge> compute() {
        if (endI - startI <= THRESHOLD) {
            ObjectList<Edge> results = new ObjectArrayList<>();
            for (int i = startI; i < endI; i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    results.add(computeEdge(points.get(i), points.get(j)));
                }
            }
            return results;
        }
        
        int mid = (startI + endI) / 2;
        PairProcessingTask left = new PairProcessingTask(points, startI, mid);
        PairProcessingTask right = new PairProcessingTask(points, mid, endI);
        left.fork();
        ObjectList<Edge> rightResults = right.compute();
        ObjectList<Edge> leftResults = left.join();
        leftResults.addAll(rightResults);
        return leftResults;
    }
}
```

#### Pattern 3: Tree/Graph Traversal
```java
class TreeTraversalTask extends RecursiveTask<Long> {
    private final Node node;
    private final Map<Node, Long> memo; // ConcurrentHashMap
    
    @Override
    protected Long compute() {
        if (node.isLeaf()) {
            return processLeaf(node);
        }
        
        Long cached = memo.get(node);
        if (cached != null) return cached;
        
        List<TreeTraversalTask> subtasks = node.children().stream()
            .map(child -> new TreeTraversalTask(child, memo))
            .collect(Collectors.toList());
        
        invokeAll(subtasks);
        
        long result = subtasks.stream()
            .mapToLong(ForkJoinTask::join)
            .sum();
        
        memo.put(node, result);
        return result;
    }
}
```

#### Pattern 4: Stateful Processing with Merging
```java
class StatefulTask extends RecursiveTask<StateResult> {
    private final List<Operation> operations;
    private final int start, end;
    private final State initialState;
    private static final int THRESHOLD = 100;
    
    record StateResult(State finalState, long count) {}
    
    @Override
    protected StateResult compute() {
        if (end - start <= THRESHOLD) {
            State current = initialState;
            long count = 0;
            for (int i = start; i < end; i++) {
                current = operations.get(i).apply(current);
                if (current.isTargetState()) count++;
            }
            return new StateResult(current, count);
        }
        
        int mid = (start + end) / 2;
        StatefulTask left = new StatefulTask(operations, start, mid, initialState);
        StatefulTask right = new StatefulTask(operations, mid, end, initialState);
        
        left.fork();
        StateResult rightResult = right.compute();
        StateResult leftResult = left.join();
        
        // Merge: right needs to start from left's final state
        StateResult mergedRight = processWithState(
            operations, mid, end, leftResult.finalState());
        
        return new StateResult(
            mergedRight.finalState(),
            leftResult.count() + mergedRight.count()
        );
    }
}
```

### Fork/Join vs Parallel Streams Decision Matrix

| Factor | Fork/Join | Parallel Streams |
|--------|----------|-------------------|
| **Control** | Fine-grained | Coarse-grained |
| **Work Distribution** | Variable sizes | Uniform |
| **Threshold** | Customizable | Fixed by framework |
| **State Management** | Explicit | Implicit |
| **Complexity** | Higher | Lower |
| **Performance** | Optimal for irregular work | Good for regular work |
| **Use Case** | Recursive algorithms, divide-conquer | Transformations, filtering |

---

## Complete Parallel Collector Implementation Guide

### Standard Parallel Collectors

#### 1. Concurrent Map Collection
```java
// Thread-safe map collection
Map<String, Long> result = stream.parallel()
    .collect(Collectors.toConcurrentMap(
        keyMapper,
        valueMapper,
        (v1, v2) -> v1 + v2  // merge function for duplicates
    ));
```

#### 2. Concurrent Grouping
```java
// Group by classifier, thread-safe
Map<String, List<Item>> grouped = stream.parallel()
    .collect(Collectors.groupingByConcurrent(
        Item::getCategory,
        Collectors.toList()
    ));
```

#### 3. Concurrent Grouping with Downstream Collector
```java
// Group and count concurrently
Map<String, Long> counts = stream.parallel()
    .collect(Collectors.groupingByConcurrent(
        Item::getCategory,
        Collectors.counting()
    ));
```

### Custom Thread-Local Collectors

#### Pattern 1: List Collection
```java
Collector<T, ?, List<T>> listCollector = Collector.of(
    ArrayList::new,                    // Supplier
    List::add,                         // Accumulator
    (left, right) -> {                 // Combiner
        left.addAll(right);
        return left;
    },
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
);

List<T> result = stream.parallel().collect(listCollector);
```

#### Pattern 2: FastUtil Collection (Day 8 example)
```java
Collector<Edge, ?, ObjectList<Edge>> fastUtilCollector = Collector.of(
    ObjectArrayList::new,              // Supplier: FastUtil list
    ObjectArrayList::add,              // Accumulator
    (left, right) -> {                 // Combiner
        left.addAll(right);
        return left;
    },
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
);

ObjectList<Edge> edges = IntStream.range(0, n)
    .parallel()
    .boxed()
    .flatMap(i -> generateEdges(i))
    .collect(fastUtilCollector);
```

#### Pattern 3: Summing Collector
```java
Collector<Long, long[], Long> summingCollector = Collector.of(
    () -> new long[1],                 // Supplier: single-element array
    (acc, value) -> acc[0] += value,    // Accumulator
    (left, right) -> {                  // Combiner
        left[0] += right[0];
        return left;
    },
    acc -> acc[0],                      // Finisher
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
);

long sum = stream.parallel().collect(summingCollector);
```

#### Pattern 4: Complex Aggregation
```java
record Stats(long count, long sum, long max) {
    Stats combine(Stats other) {
        return new Stats(
            count + other.count,
            sum + other.sum,
            Math.max(max, other.max)
        );
    }
}

Collector<Long, Stats, Stats> statsCollector = Collector.of(
    () -> new Stats(0, 0, Long.MIN_VALUE),  // Supplier
    (stats, value) -> {                      // Accumulator (mutable)
        stats.count++;
        stats.sum += value;
        stats.max = Math.max(stats.max, value);
    },
    Stats::combine,                          // Combiner
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
);

Stats stats = stream.parallel().collect(statsCollector);
```

### Collector Characteristics Explained

- **CONCURRENT**: 
  - Accumulator can be called concurrently from multiple threads
  - Use when accumulator is thread-safe
  - Example: `ConcurrentHashMap.put()`, `AtomicLong.addAndGet()`

- **UNORDERED**:
  - Order of elements not preserved
  - Allows optimizations (e.g., parallel reduction)
  - Use when order doesn't matter

- **IDENTITY_FINISH**:
  - Finisher is identity function
  - Can be optimized away
  - Use when accumulator type equals result type

### When to Use Each Collector Type

**Built-in Concurrent Collectors** (`toConcurrentMap`, `groupingByConcurrent`):
- ✅ Standard map/grouping operations
- ✅ High contention scenarios
- ✅ Order doesn't matter
- ❌ Need ordered results
- ❌ Custom data structures

**Custom Thread-Local Collectors**:
- ✅ Custom data structures (FastUtil, etc.)
- ✅ Want to minimize contention
- ✅ Need efficient merging
- ❌ Simple standard operations (use built-in)
- ❌ Very small datasets (overhead not worth it)

**Standard Collectors** (non-concurrent):
- ✅ Sequential streams
- ✅ Order matters
- ✅ Low contention
- ❌ High contention parallel streams

---

## Performance Considerations

1. **Overhead**: Parallel streams have overhead - only beneficial for sufficient work per element
2. **Thread Pool**: Default `ForkJoinPool.commonPool()` may need tuning for CPU-bound tasks
3. **Memory**: Parallel processing may increase memory usage (multiple threads processing simultaneously)
4. **Cache Locality**: Sequential processing may have better cache performance
5. **Load Balancing**: Ensure work is evenly distributed across threads
6. **Fork/Join Threshold**: Too small = overhead, too large = underutilization
7. **Collector Merging**: Consider cost of combiner function - should be O(n) or better

---

## Testing Recommendations

After parallelization:
1. Verify correctness (results match sequential version)
2. Measure performance improvement (benchmark with JMH)
3. Test with various input sizes
4. Monitor thread pool utilization
5. Check for race conditions or deadlocks
