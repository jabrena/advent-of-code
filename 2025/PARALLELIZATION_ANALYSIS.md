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

#### Technique 2: **Parallel Path Exploration with CompletableFuture**
- **Approach**: When beam splits, explore both paths using `CompletableFuture`
- **Complexity**: Medium-High
- **Benefit**: High - path exploration is independent after splits
- **Implementation**: Use `ConcurrentHashMap` for memo, parallel recursive calls with `CompletableFuture`

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

### 8. **Fork/Join RecursiveTask** (High Complexity, High Control)
- **Days**: 1, 2, 6, 7, 8
- **Complexity**: Medium-High
- **Benefit**: Very High (fine-grained control, optimal work-stealing)
- **Implementation**: Custom `RecursiveTask<T>` extending `ForkJoinTask`
- **When to Use**:
  - Variable-sized work chunks
  - Recursive divide-and-conquer algorithms
  - Need fine-grained control over splitting
  - Work-stealing optimization needed

### 9. **Parallel Collectors** (Medium Complexity, High Performance)
- **Days**: 2, 5, 6, 8
- **Complexity**: Medium
- **Benefit**: High (eliminates contention, optimal parallel collection)
- **Implementation**: Custom `Collector` with thread-local accumulators
- **Types**:
  - **Thread-local collectors**: Each thread accumulates to local collection, merge at end
  - **Concurrent collectors**: Use `ConcurrentHashMap`, `ConcurrentLinkedQueue`
  - **Custom combiners**: Efficient merging strategies for specific data structures

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
1. **Day 1**: Fork/Join chunk-based processing (complex state merging)
2. **Day 7**: Fork/Join path exploration (requires careful synchronization)
3. **Day 12**: Already parallelized, minor optimizations only

---

## Implementation Examples Summary

### Quick Wins (Parallel Streams)
- Day 3, 9 Part 1, 10 Part 1: Add `.parallel()` or `.parallelStream()`
- Day 4 Part 1: `parallelStream()` for cell filtering
- Day 5 Part 1: `parallelStream()` for ID checking

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
