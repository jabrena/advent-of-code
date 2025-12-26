# Custom Data Structures

This document details custom data structures implemented or used in the Advent of Code solutions, including DSU (Union-Find), Records, and domain-specific classes.

## Disjoint Set Union (DSU) / Union-Find

**Description**: Efficient data structure for tracking disjoint sets with near-constant amortized time complexity O(α(n)), where α is the inverse Ackermann function.

**Implementation**: **2025 Day 8** (`DSU.java`)

**Time Complexity**:
- Find: O(α(n)) amortized (near constant)
- Union: O(α(n)) amortized (near constant)
- Component size: O(n) for all sizes

**Features**:
- Path compression optimization
- Union by size optimization
- Find and Union operations
- Component size tracking

**Usage Examples**:
- **2025 Day 8**: Finding connected components in point clustering problems

**Code Reference**:
```37:145:2025/src/main/java/info/jab/aoc2025/day8/DSU.java
public final class DSU {
    private final int[] parent;
    private final int[] size;
    private int count;

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                int temp = rootI;
                rootI = rootJ;
                rootJ = temp;
            }
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
            count--;
            return true;
        }
        return false;
    }

    public List<Integer> getComponentSizes() {
        boolean[] seen = new boolean[parent.length];
        java.util.ArrayList<Integer> sizes = new java.util.ArrayList<>();
        
        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            if (!seen[root]) {
                seen[root] = true;
                sizes.add(size[root]);
            }
        }
        
        return List.copyOf(sizes);
    }
}
```

### Optimizations

#### Path Compression
During find operations, nodes are directly connected to their root, flattening the tree structure for faster future lookups:

```java
public int find(int i) {
    if (parent[i] != i) {
        parent[i] = find(parent[i]);  // Path compression
    }
    return parent[i];
}
```

#### Union by Size
When merging sets, the smaller tree is always attached to the root of the larger tree, keeping trees balanced:

```java
if (size[rootI] < size[rootJ]) {
    // Swap to ensure rootI is the larger tree
    int temp = rootI;
    rootI = rootJ;
    rootJ = temp;
}
parent[rootJ] = rootI;
size[rootI] += size[rootJ];
```

### Use Cases

1. **Connected Components**: Finding all connected components in a graph
2. **Cycle Detection**: Detecting cycles in undirected graphs
3. **Kruskal's Algorithm**: Minimum spanning tree construction
4. **Equivalence Relations**: Tracking equivalence classes
5. **Clustering**: Grouping related elements

### When to Use DSU

- Need to track disjoint sets efficiently
- Frequent union and find operations
- Connected component problems
- Cycle detection in undirected graphs
- Clustering problems

---

## Records (Immutable Data Structures)

**Description**: Java records provide a concise way to define immutable data carriers. They automatically generate:
- Private final fields
- Public accessors
- Constructor
- `equals()`, `hashCode()`, and `toString()` methods

**Time Complexity**: O(1) for field access (same as regular classes)

**Usage Examples**:

### 2025 Day 12 - Shape Packing

- **ParsedData**: Holds parsed input data
  ```java
  public record ParsedData(Map<Integer, Shape> shapes, List<Region> regions) {
      public ParsedData {
          shapes = Map.copyOf(shapes);
          regions = List.copyOf(regions);
      }
  }
  ```

- **Region**: Represents a region with dimensions and requirements
  ```java
  public record Region(int width, int height, Map<Integer, Integer> requirements, long regionArea)
  ```

- **Shape**: Represents a shape with variants
  ```java
  public record Shape(int id, int area, List<ShapeVariant> variants)
  ```

- **ShapeVariant**: A rotation/reflection variant of a shape
  ```java
  public record ShapeVariant(Set<Point> points, int minX, int minY, int maxX, int maxY)
  ```

- **Point**: 2D coordinate
  ```java
  public record Point(int x, int y)
  ```

- **CacheKey**: Memoization key for backtracking
  ```java
  public record CacheKey(long gridHash, long[] grid, int[] remainingShapeIds)
  ```

### 2025 Day 11 - Graph Problems

- **GraphNode**: Represents a node in the graph
  ```java
  public record GraphNode(String name)
  ```

- **GraphEdge**: Represents an edge with source and destination
  ```java
  public record GraphEdge(GraphNode from, List<GraphNode> to)
  ```

- **PathPair**: Pair of nodes for path counting
  ```java
  public record PathPair(GraphNode from, GraphNode to)
  ```

### 2025 Day 9 - Rectangle Area

- **Point**: 2D coordinate
  ```java
  public record Point(int x, int y)
  ```

- **Edge**: Represents an edge between points
  ```java
  public record Edge(Point from, Point to)
  ```

### 2025 Day 8 - Point Clustering

- **Point3D**: 3D coordinate
  ```java
  public record Point3D(int x, int y, int z)
  ```

- **Connection**: Connection between points
  ```java
  public record Connection(Point3D from, Point3D to)
  ```

### 2025 Day 1 - Dial Rotation

- **Rotation**: Represents a dial rotation with direction and distance
  ```java
  public record Rotation(Direction direction, int distance)
  ```

- **DialState**: Immutable state record representing dial position and zero count
  ```java
  public record DialState(int position, int zeroCount)
  ```

- **Sequence**: Internal record for rotation sequences
  ```java
  private record Sequence(Direction direction, int steps)
  ```

### 2025 Day 2 - Invalid ID Validation

- **Range**: Immutable record representing a range of IDs
  ```java
  public record Range(long start, long end)
  ```

### 2025 Day 3 - Max Joltage

- **Bank**: Record containing digit sequences using FastUtil IntArrayList
  ```java
  public record Bank(IntArrayList digits)
  ```

- **MaxDigitResult**: Internal result record
  ```java
  private record MaxDigitResult(int value, int index)
  ```

### 2025 Day 5 - Range Processing

- **Interval**: Immutable record representing a range with start and end values
  ```java
  public record Interval(long start, long end) {
      public boolean contains(long value);
      public long size();
      public boolean overlapsOrAdjacent(Interval other);
      public Interval merge(Interval other);
  }
  ```

- **RangeProblemInput**: Immutable record containing parsed ranges and IDs
  ```java
  public record RangeProblemInput(List<Interval> intervals, List<Long> ids)
  ```

- **RangeMergeState**: Immutable state record for functional range merging
  ```java
  record RangeMergeState(Interval current, List<Interval> merged, int index)
  ```

### 2025 Day 6 - Math Block Processing

- **ColumnRange**: Immutable record representing a column range for block processing
  ```java
  public record ColumnRange(int startCol, int endCol) {
      public ColumnRange {
          if (startCol < 0) {
              throw new IllegalArgumentException("startCol must be non-negative: " + startCol);
          }
          if (endCol < startCol) {
              throw new IllegalArgumentException("endCol must be >= startCol");
          }
      }
  }
  ```

- **MathOperator**: Sealed interface representing mathematical operators (sealed interface with records)
  ```java
  public sealed interface MathOperator 
      permits MathOperator.Addition, MathOperator.Multiplication, MathOperator.None {
      
      long apply(List<Long> numbers);
      String symbol();
      
      record Addition() implements MathOperator {
          @Override
          public long apply(List<Long> numbers) {
              return numbers.stream().mapToLong(Long::longValue).sum();
          }
          @Override
          public String symbol() { return "+"; }
      }
      
      record Multiplication() implements MathOperator {
          @Override
          public long apply(List<Long> numbers) {
              return numbers.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
          }
          @Override
          public String symbol() { return "*"; }
      }
      
      record None() implements MathOperator {
          @Override
          public long apply(List<Long> numbers) { return 0; }
          @Override
          public String symbol() { return " "; }
      }
  }
  ```

### 2025 Day 7 - Beam Path Counting

- **BeamAction**: Internal record for beam actions
  ```java
  private record BeamAction(int x, CellType cellType)
  ```

- **SplitResult**: Internal record for split results
  ```java
  private record SplitResult(Set<Integer> nextBeams, long splits)
  ```

### 2025 Day 8 - Point Clustering

- **Point3D**: 3D coordinate
  ```java
  public record Point3D(int x, int y, int z)
  ```

- **Edge**: Connection between points with distance
  ```java
  public record Edge(int i, int j, long distanceSquared)
  ```

### 2025 Day 9 - Max Rectangle Area

- **PointPair**: Internal record for point pairs
  ```java
  private record PointPair(Point p1, Point p2)
  ```

### 2025 Day 10 - Button Press Optimization

- **Part1Problem**: Problem input record
  ```java
  public record Part1Problem(long target, long[] buttons)
  ```

- **Part2Problem**: Problem input record
  ```java
  public record Part2Problem(int[] targets, int[][] buttons)
  ```

### 2025 Day 11 - Graph Path Counting

- **GraphNode**: Represents a node in the graph
  ```java
  public record GraphNode(String value)
  ```

- **GraphEdge**: Represents an edge with source and destination
  ```java
  public record GraphEdge(GraphNode from, List<GraphNode> to)
  ```

- **PathPair**: Represents a path pair for counting
  ```java
  public record PathPair(GraphNode from, GraphNode to)
  ```

### 2025 Day 12 - Shape Packing

- **Point**: 2D coordinate
  ```java
  public record Point(int x, int y)
  ```

- **Shape**: Immutable record representing a shape with variants
  ```java
  public record Shape(int id, int area, List<ShapeVariant> variants)
  ```

- **ShapeVariant**: Represents a variant of a shape with normalized coordinates and precomputed bitmasks
  ```java
  public record ShapeVariant(
      List<Point> points,
      int width,
      int height,
      int[] bitOffsets,
      long[] bitmaskChunks,
      int[] bitmaskLongIndices)
  ```

- **Region**: Immutable record representing a region to pack shapes into
  ```java
  public record Region(int width, int height, Map<Integer, Integer> requirements, long regionArea)
  ```

- **ParsedData**: Immutable record containing parsed shapes and regions
  ```java
  public record ParsedData(Map<Integer, Shape> shapes, List<Region> regions)
  ```

- **CacheKey**: Cache key for memoization in shape packing backtracking
  ```java
  public final class CacheKey {
      private final long[] grid;
      private final long gridHash;
      private final int[] remainingShapeIds;
  }
  ```

### 2024 Day 9 - Disk Compaction

- **GapInfo**: Information about a gap in the disk
  ```java
  public record GapInfo(int position, int size, int gapId)
  ```

### 2015 Day 21 - RPG Equipment

- **Character**: RPG character stats
  ```java
  public record Character(int hitPoints, int damage, int armor)
  ```

- **Item**: Equipment item
  ```java
  public record Item(String name, int cost, int damage, int armor)
  ```

- **Equipment**: Complete equipment set
  ```java
  public record Equipment(Item weapon, Item armor, Item ring1, Item ring2)
  ```

**Code Reference**:
```16:23:2025/src/main/java/info/jab/aoc2025/day12/ParsedData.java
public record ParsedData(Map<Integer, Shape> shapes, List<Region> regions) {
    public ParsedData {
        shapes = Map.copyOf(shapes);
        regions = List.copyOf(regions);
    }
```

### Benefits of Records

1. **Immutability**: Fields are final by default
2. **Conciseness**: Less boilerplate code
3. **Value Semantics**: Automatic `equals()` and `hashCode()`
4. **Pattern Matching**: Compatible with pattern matching (Java 14+)
5. **Functional Programming**: Aligns with functional programming principles

### When to Use Records

- Immutable data carriers
- Value objects
- DTOs (Data Transfer Objects)
- Configuration objects
- State snapshots
- Any class that is primarily data with minimal behavior

---

## Custom Classes

**Description**: Domain-specific classes for problem modeling that don't fit into standard collections or records.

### Graph Nodes and Edges

- **GraphNode**: Represents a node in the graph (2025 Day 11)
- **GraphEdge**: Represents an edge with source and destination (2025 Day 11)

### Point Classes

- **Point**: 2D coordinate (various days)
- **Point3D**: 3D coordinate (2025 Day 8)

### Range Classes

- **Range**: Represents an interval (2025 Day 2)
  ```java
  public record Range(long start, long end)
  ```

- **Interval**: Represents a range with start and end values (2025 Day 5)
  ```java
  public record Interval(long start, long end) {
      public boolean contains(long value);
      public long size();
      public boolean overlapsOrAdjacent(Interval other);
      public Interval merge(Interval other);
      public static Interval from(String line);
  }
  ```

- **RangeProblemInput**: Immutable record containing parsed ranges and IDs (2025 Day 5)
  ```java
  public record RangeProblemInput(List<Interval> ranges, List<Long> ids)
  ```

- **RangeMergeState**: Immutable state record for functional range merging (2025 Day 5)
  ```java
  record RangeMergeState(Interval current, List<Interval> merged, int index) {
      boolean hasNext(List<Interval> sortedRanges);
      RangeMergeState next(List<Interval> sortedRanges);
      List<Interval> complete();
  }
  ```

### State Classes

Various state representations for game simulations and state space search:
- Game states
- Program states
- Simulation states

### Example Custom Class Pattern

```java
public final class CustomClass {
    private final int field1;
    private final String field2;
    
    public CustomClass(int field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
    
    // Getters, equals, hashCode, toString
}
```

**When to Use Custom Classes Instead of Records**:
- Need mutable state
- Complex initialization logic
- Inheritance required
- Need to encapsulate behavior with data
- Legacy code compatibility

---

## Summary

| Structure | Use Case | Complexity | Immutability |
|-----------|----------|------------|--------------|
| DSU | Disjoint sets, connected components | O(α(n)) | Mutable |
| Records | Immutable data carriers | O(1) access | Immutable |
| Custom Classes | Domain-specific modeling | Varies | Configurable |

---

## References

- [Disjoint Set Union (Union-Find) - Wikipedia](https://en.wikipedia.org/wiki/Disjoint-set_data_structure)
- [Java Records - Oracle Documentation](https://docs.oracle.com/javase/specs/jls/se14/html/jls-8.html#jls-8.10)
- [Back to Main Documentation](../data-structures/README.md)

