# Grid/Matrix Data Structures

This document details grid and matrix data structures used in the Advent of Code solutions.

## Overview

Grids and matrices are fundamental data structures for 2D spatial problems, pathfinding, cellular automata, and game boards. The solutions use both primitive 2D arrays and custom grid classes.

## 2D Arrays

**Description**: Two-dimensional arrays for grid/matrix representation.

**Time Complexity**:
- Access: O(1)
- Search: O(n×m) for n×m grid
- Traversal: O(n×m)
- Space: O(n×m)

**Memory Layout**: Row-major order (rows stored contiguously)

### Integer 2D Arrays (`int[][]`)

**Usage Examples**:

#### 2024 Day 20 - Distance Matrix

Distance matrix for Dijkstra's algorithm:

```java
int[][] distances = new int[grid.maxY()][grid.maxX()];
for (int i = 0; i < grid.maxX(); i++) {
    for (int j = 0; j < grid.maxY(); j++) {
        distances[i][j] = Integer.MAX_VALUE;
    }
}
```

**Use Case**: Storing shortest distances from source to each cell in pathfinding algorithms.

#### 2015 Day 6 - Light Grid

Light grid manipulation:

```java
int[][] lights = new int[height][width];
// Or
boolean[][] lights = new boolean[height][width];
```

**Use Case**: Tracking on/off state of lights in a grid.

### Character 2D Arrays (`char[][]`)

**Usage Examples**:

#### 2016 Day 8 - Screen Representation

2D screen representation:

```java
char[][] screen = new char[height][width];
```

**Use Case**: Representing a 2D display screen with characters.

### Boolean 2D Arrays (`boolean[][]`)

**Usage Examples**:

#### 2015 Day 6 - Light Grid

Boolean grid for light states:

```java
boolean[][] lights = new boolean[height][width];
```

**Use Case**: Efficient storage of binary states (on/off, visited/unvisited).

---

## Grid Classes

**Description**: Custom grid classes from third-party library (`com.putoet.grid.Grid`).

**Features**:
- Grid traversal utilities
- Neighbor checking (4-directional, 8-directional)
- Boundary checking
- Pathfinding support
- Element finding and filtering

**Usage Examples**:
- Used extensively for 2D grid problems across multiple years
- Provides utilities for grid traversal, neighbor checking, and pathfinding

**Code Pattern**:
```java
var grid = new Grid(GridUtils.of(list));
var point = grid.findFirst(c -> c == 'S').orElseThrow();
```

---

## Common Grid Operations

### Accessing Grid Cells

```java
// 2D Array
char value = grid[row][col];

// Grid Class
char value = grid.get(col, row);  // Note: x, y order
```

### Iterating Over Grid

```java
// Row-major iteration
for (int i = 0; i < grid.length; i++) {
    for (int j = 0; j < grid[i].length; j++) {
        // Process grid[i][j]
    }
}

// Using Grid class
grid.forEach((point, value) -> {
    // Process each cell
});
```

### Checking Neighbors

```java
// 4-directional neighbors
int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
for (int[] dir : directions) {
    int newRow = row + dir[0];
    int newCol = col + dir[1];
    if (isValid(newRow, newCol, height, width)) {
        // Process neighbor
    }
}

// 8-directional neighbors (including diagonals)
int[][] directions = {
    {-1, -1}, {-1, 0}, {-1, 1},
    {0, -1},           {0, 1},
    {1, -1},  {1, 0},   {1, 1}
};
```

### Boundary Checking

```java
boolean isValid(int row, int col, int height, int width) {
    return row >= 0 && row < height && col >= 0 && col < width;
}
```

---

## Grid Problem Patterns

### Pathfinding in Grids

**Algorithms Used**:
- **BFS**: Unweighted shortest path
- **Dijkstra's**: Weighted shortest path
- **A***: Heuristic-based pathfinding

**Example** (2024 Day 20):
```java
int[][] distances = new int[grid.maxY()][grid.maxX()];
PriorityQueue<int[]> unvisited = new PriorityQueue<>(
    Comparator.comparingLong(a -> distances[a[0]][a[1]])
);
// Dijkstra's algorithm implementation
```

### Cellular Automata

**Example**: Conway's Game of Life (2015 Day 18)

```java
boolean[][] current = new boolean[height][width];
boolean[][] next = new boolean[height][width];

for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
        int neighbors = countNeighbors(current, i, j);
        next[i][j] = applyRules(current[i][j], neighbors);
    }
}
```

### Grid Manipulation

**Examples**:
- Rotating grids
- Flipping grids
- Extracting sub-grids
- Grid transformations

### Visibility Calculations

**Example** (2022 Day 8): Tree visibility in 2D grid

```java
// Check visibility from each direction
boolean visible =
    visibleFromTop(grid, row, col) ||
    visibleFromBottom(grid, row, col) ||
    visibleFromLeft(grid, row, col) ||
    visibleFromRight(grid, row, col);
```

---

## Bitmask Grid Representation

**Description**: Grid state represented as `long[]` arrays where each bit represents a cell state. Provides O(1) bitwise operations for placement checks and updates, better cache locality than `boolean[][]`.

**Time Complexity**:
- Placement check: O(1) (bitwise AND)
- Placement/removal: O(1) (bitwise OR/AND)
- Space: O((width × height + 63) / 64) longs

**Memory Layout**: Each `long` represents up to 64 cells, with bit index calculated as `bitIndex = y * width + x`.

**Usage Examples**:

#### 2025 Day 12 - Shape Packing

Bitmask representation for efficient grid state tracking in backtracking:

```95:106:2025/src/main/java/info/jab/aoc2025/day12/ShapePacking.java
        // Use bitmask representation for efficient operations
        long[] grid = new long[(totalCells + 63) / 64];
        // Incremental grid hash: starts at 0, updated as bits are placed/removed
        long gridHash = 0L;
        Map<CacheKey, Boolean> memo = new HashMap<>();

        // Precompute minimum area needed for remaining shapes (for constraint propagation)
        long[] minAreaRemaining = new long[shapeIds.size() + 1];
        for (int i = shapeIds.size() - 1; i >= 0; i--) {
            minAreaRemaining[i] = minAreaRemaining[i + 1] + shapes.get(shapeIds.get(i)).area();
        }
```

**Bitwise Operations**:
```java
// Calculate bit index and long index
int bitIndex = y * width + x;
int longIndex = bitIndex / 64;
int bitOffset = bitIndex % 64;

// Check if cell is occupied (O(1))
boolean isOccupied = (grid[longIndex] & (1L << bitOffset)) != 0;

// Place cell (O(1))
grid[longIndex] |= (1L << bitOffset);

// Remove cell (O(1))
grid[longIndex] &= ~(1L << bitOffset);

// Check shape overlap (O(1) per long chunk)
boolean canPlace = (grid[longIndex] & shapeBitmask[chunkIndex]) == 0;
```

**Precomputed Bit Offsets**:
Shapes precompute bit offsets and bitmask chunks for efficient placement:

```70:88:2025/src/main/java/info/jab/aoc2025/day12/ShapeVariant.java
        // Precompute bit offsets: offset = py * width + px
        int[] bitOffsets = new int[normalized.size()];
        for (int i = 0; i < normalized.size(); i++) {
            Point p = normalized.get(i);
            bitOffsets[i] = p.y() * calculatedWidth + p.x();
        }

        // Precompute bitmask chunks for fast overlap checks
        int totalCells = calculatedWidth * calculatedHeight;
        int numLongs = (totalCells + 63) / 64;
        long[] bitmaskChunks = new long[numLongs];
        int[] bitmaskLongIndices = new int[normalized.size()];

        for (int i = 0; i < normalized.size(); i++) {
            int bitIndex = bitOffsets[i];
            int longIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;
            bitmaskChunks[longIndex] |= (1L << bitOffset);
            bitmaskLongIndices[i] = longIndex;
        }
```

**Advantages**:
- O(1) placement checks using bitwise AND
- O(1) placement/removal using bitwise OR/AND
- Better cache locality than `boolean[][]`
- Reduced memory overhead (64 cells per long)
- Fast overlap detection for shape placement
- Efficient for backtracking algorithms

**When to Use**:
- Binary state tracking (occupied/empty, visited/unvisited)
- Performance-critical placement operations
- Large grids where memory efficiency matters
- Backtracking algorithms with frequent state checks
- Shape packing and placement problems

**Comparison with boolean[][]**:

| Aspect | `boolean[][]` | `long[]` bitmask |
|--------|---------------|------------------|
| Space | O(n×m) booleans | O((n×m)/64) longs |
| Placement check | O(points) iteration | O(1) bitwise AND |
| Cache locality | Good | Better (contiguous longs) |
| Memory overhead | Higher | Lower |
| Best for | Small grids, simple operations | Large grids, frequent checks |

---

## Performance Considerations

### Cache Locality

Row-major iteration is cache-friendly:

```java
// Good: Row-major (cache-friendly)
for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
        process(grid[i][j]);
    }
}

// Less optimal: Column-major (may cause cache misses)
for (int j = 0; j < width; j++) {
    for (int i = 0; i < height; i++) {
        process(grid[i][j]);
    }
}
```

### Memory Efficiency

- **Dense Grids**: Use 2D arrays (efficient for full grids)
- **Sparse Grids**: Consider `Map<Point, Value>` for memory efficiency
- **Binary Grids**: Use `boolean[][]` instead of `int[][]` for space savings

---

## Grid Representation Comparison

| Representation | Space | Access | Best For |
|----------------|-------|--------|----------|
| 2D Array | O(n×m) | O(1) | Dense grids, fixed size |
| Map<Point, Value> | O(k) where k = non-empty cells | O(1)* | Sparse grids |
| Grid Class | O(n×m) | O(1) | Rich utilities needed |
| Bitmask (`long[]`) | O((n×m)/64) | O(1) bitwise | Binary state grids, frequent checks |

---

## Common Grid Problems

1. **Pathfinding**: Finding paths from start to end
2. **Cellular Automata**: Game of Life, pattern evolution
3. **Visibility**: Line of sight, tree visibility
4. **Coverage**: Area coverage, flood fill
5. **Pattern Matching**: Word search, pattern detection
6. **Simulation**: Game boards, state evolution

---

## Summary

Grids are represented using:
- **2D Arrays**: `int[][]`, `char[][]`, `boolean[][]` for dense grids
- **Grid Classes**: Custom utilities for complex operations
- **Maps**: For sparse grid representations
- **Bitmask Arrays** (`long[]`): For binary state grids with O(1) bitwise operations

**Key Operations**:
- Neighbor checking (4-directional, 8-directional)
- Boundary validation
- Pathfinding algorithms
- Grid traversal

**When to Use**:
- 2D spatial problems
- Pathfinding
- Cellular automata
- Game boards
- Grid-based puzzles

---

## References

- [Back to Main Documentation](../data-structures/README.md)

