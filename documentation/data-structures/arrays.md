# Arrays

This document details array data structures (primitive and multi-dimensional) used in the Advent of Code solutions.

## Overview

Arrays are fixed-size data structures providing O(1) random access. They are the foundation for many other data structures and are used extensively for efficient memory usage and performance.

## Primitive Arrays

### One-Dimensional Arrays

**Description**: Fixed-size arrays providing O(1) random access.

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(n) (requires shifting)
- Deletion: O(n) (requires shifting)

**Memory**: Contiguous memory allocation, very cache-friendly.

**Usage Examples**:

#### Integer Arrays (`int[]`)

- **2024 Day 9** (`DiskCompactor.java`): Disk representation and cumulative positions
  ```java
  int[] parts = new int[input.length()];
  int[] disk = new int[totalBlocks];
  int[] cumulativePositions = new int[parts.length + 1];
  ```

- **2024 Day 20** (`RaceCondition.java`): Position coordinates
  ```java
  int[] startPosition = new int[]{point1.y(), point1.x()};
  int[] endPosition = new int[]{point2.y(), point2.x()};
  ```

- **2025 Day 8** (`DSU.java`): Parent and size arrays in Union-Find
  ```java
  private final int[] parent;
  private final int[] size;
  ```

#### Long Arrays (`long[]`)

- **2025 Day 12** (`CacheKey.java`, `ShapePacking.java`): Grid state representation using bitmasks
  ```java
  private final long[] grid;
  ```
  
- **2025 Day 10** (`RationalMatrix.java`): Interleaved memory layout for rational numbers (numerator/denominator pairs)
  ```java
  // Interleaved: [num0, den0, num1, den1, ...]
  private final long[] data;
  ```

#### Character Arrays (`char[]`)

- **2019 Day 4** (`SecureContainerTest.java`): Password digit manipulation
  ```java
  char[] passwordDigits = String.valueOf(password).toCharArray();
  ```

**Code Reference**:
```java
int[] parts = new int[input.length()];
int[] disk = new int[totalBlocks];
int[] parent = new int[n];
int[] size = new int[n];
char[] passwordDigits = String.valueOf(password).toCharArray();
long[] grid = new long[gridSize]; // Bitmask representation
```

**When to Use**:
- Fixed-size data with known dimensions
- Need O(1) random access
- Performance-critical code
- Memory-efficient storage
- Building blocks for other data structures (DSU, etc.)

**Advantages**:
- Fast random access
- Memory efficient (contiguous allocation)
- Cache-friendly
- Simple and direct

**Disadvantages**:
- Fixed size (cannot resize)
- Expensive insertions/deletions (requires shifting)
- No built-in bounds checking (in Java)

---

### Multi-Dimensional Arrays

**Description**: Arrays of arrays for 2D/3D data structures.

**Time Complexity**:
- Access: O(1)
- Search: O(n×m) for 2D, O(n×m×k) for 3D
- Insertion: O(n×m) (requires shifting)
- Deletion: O(n×m) (requires shifting)

**Memory**: Row-major or column-major layout depending on access pattern.

**Usage Examples**:

#### Two-Dimensional Integer Arrays (`int[][]`)

- **2024 Day 20** (`RaceCondition.java`): Distance matrix for Dijkstra's algorithm
  ```java
  int[][] distances = new int[grid.maxY()][grid.maxX()];
  for (int i = 0; i < grid.maxX(); i++) {
      for (int j = 0; j < grid.maxY(); j++) {
          distances[i][j] = Integer.MAX_VALUE;
      }
  }
  ```

#### Two-Dimensional Character Arrays (`char[][]`)

- **2016 Day 8** (`TwoFactorAuthentication.java`): 2D screen representation
  ```java
  char[][] screen = new char[height][width];
  ```

#### Two-Dimensional Boolean Arrays (`boolean[][]`)

- **2015 Day 6** (`LightCounter.java`): Light grid manipulation
  ```java
  boolean[][] lights = new boolean[height][width];
  ```

**Code Reference**:
```java
int[][] distances = new int[grid.maxY()][grid.maxX()];
char[][] screen = new char[height][width];
boolean[][] lights = new boolean[height][width];
```

**When to Use**:
- 2D/3D grid representations
- Matrix operations
- Distance/cost matrices
- Game boards, screens, maps
- Any problem requiring 2D spatial data

**Advantages**:
- Fast random access to any cell
- Simple indexing: `array[row][col]`
- Memory efficient for dense data
- Cache-friendly for row-major access

**Disadvantages**:
- Fixed dimensions
- Memory overhead for sparse data
- Column-major access less cache-friendly

---

## Specialized Array Patterns

### Bitmask Arrays (`long[]`)

**Description**: Using `long[]` arrays to represent 2D grids as bitmasks, where each bit represents a cell state. Each `long` can represent up to 64 cells, providing efficient bitwise operations.

**Time Complexity**:
- Placement check: O(1) (bitwise AND)
- Placement/removal: O(1) (bitwise OR/AND)
- Grid comparison: O(n/64) where n is number of cells

**Memory**: More memory-efficient than `boolean[][]` for large grids, with better cache locality.

**Usage Examples**:
- **2025 Day 12** (`ShapePacking.java`, `CacheKey.java`): Grid state representation for shape packing backtracking
  ```java
  // Grid represented as bitmask array
  private final long[] grid;
  
  // Each long represents up to 64 cells
  // Index calculation: longIndex = (y * width + x) / 64
  // Bit offset: bitOffset = (y * width + x) % 64
  
  // Check if cell is occupied
  boolean occupied = (grid[longIndex] & (1L << bitOffset)) != 0;
  
  // Place shape (set bits)
  grid[longIndex] |= (1L << bitOffset);
  
  // Remove shape (clear bits)
  grid[longIndex] &= ~(1L << bitOffset);
  ```

**Code Reference**:
```java
// Initialize bitmask grid
int width = 10;
int height = 10;
int gridSize = (width * height + 63) / 64; // Ceiling division
long[] grid = new long[gridSize];

// Check if cell (x, y) is occupied
int index = y * width + x;
int longIndex = index / 64;
int bitOffset = index % 64;
boolean occupied = (grid[longIndex] & (1L << bitOffset)) != 0;

// Set cell (x, y)
grid[longIndex] |= (1L << bitOffset);

// Clear cell (x, y)
grid[longIndex] &= ~(1L << bitOffset);
```

**When to Use**:
- Large 2D grids with boolean cell states
- Need fast bitwise operations for placement/removal checks
- Memory-efficient representation for sparse or dense grids
- Cache-friendly operations (better than `boolean[][]`)

**Advantages**:
- O(1) bitwise operations (faster than array access)
- Memory efficient (64 cells per long)
- Better cache locality than `boolean[][]`
- Fast grid state comparison using bitwise operations

**Disadvantages**:
- More complex indexing calculations
- Fixed to 64 cells per long (less flexible than boolean arrays)
- Requires bit manipulation knowledge

---

### Interleaved Arrays (`long[]`)

**Description**: Using a single `long[]` array to store pairs of values in interleaved memory layout (e.g., numerator/denominator pairs). Adjacent values are stored next to each other for better cache locality.

**Time Complexity**:
- Access: O(1) (with index calculation)
- Update: O(1)

**Memory**: Better cache locality than separate arrays for related pairs.

**Usage Examples**:
- **2025 Day 10** (`RationalMatrix.java`): Storing rational numbers (numerator/denominator pairs) in interleaved layout
  ```java
  // Interleaved memory layout: [num0, den0, num1, den1, ...]
  // Better cache locality - numerator and denominator are adjacent
  private final long[] data;
  
  // Index calculation: baseIdx = row * (cols+1) + col
  // Then data[baseIdx*2] = numerator, data[baseIdx*2+1] = denominator
  
  // Access numerator
  long num = data[baseIdx * 2];
  
  // Access denominator
  long den = data[baseIdx * 2 + 1];
  
  // Update both
  data[baseIdx * 2] = newNum;
  data[baseIdx * 2 + 1] = newDen;
  ```

**Code Reference**:
```java
// Initialize interleaved array for pairs
int numPairs = 100;
long[] data = new long[numPairs * 2]; // Each pair needs 2 slots

// Access pair at index i
int baseIdx = i;
long first = data[baseIdx * 2];
long second = data[baseIdx * 2 + 1];

// Update pair
data[baseIdx * 2] = newFirst;
data[baseIdx * 2 + 1] = newSecond;
```

**When to Use**:
- Need to store pairs of related values
- Cache locality is important (adjacent access patterns)
- Memory efficiency matters
- Frequently access both values together

**Advantages**:
- Better cache locality (adjacent pairs)
- Single array allocation (simpler memory management)
- Efficient for sequential access patterns
- Reduces memory fragmentation

**Disadvantages**:
- More complex indexing (multiply by 2)
- Less intuitive than separate arrays
- Fixed to pairs (less flexible)

---

## Common Patterns

### Array Initialization

```java
// Fixed size
int[] arr = new int[10];

// With initial values
int[] arr = {1, 2, 3, 4, 5};

// 2D array
int[][] matrix = new int[rows][cols];

// Jagged array (rows can have different lengths)
int[][] jagged = new int[3][];
jagged[0] = new int[5];
jagged[1] = new int[3];
jagged[2] = new int[7];
```

### Array Traversal

```java
// Standard for loop
for (int i = 0; i < arr.length; i++) {
    // Process arr[i]
}

// Enhanced for loop
for (int value : arr) {
    // Process value
}

// 2D traversal
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        // Process matrix[i][j]
    }
}
```

### Array Copying

```java
// Shallow copy
int[] copy = Arrays.copyOf(original, original.length);

// Range copy
int[] subarray = Arrays.copyOfRange(original, start, end);

// 2D deep copy
int[][] copy = new int[original.length][];
for (int i = 0; i < original.length; i++) {
    copy[i] = Arrays.copyOf(original[i], original[i].length);
}
```

---

## Performance Considerations

### Cache Locality

Arrays provide excellent cache locality due to contiguous memory allocation. Accessing elements sequentially is very fast:

```java
// Good: Sequential access (cache-friendly)
for (int i = 0; i < arr.length; i++) {
    sum += arr[i];
}

// Less optimal: Random access (may cause cache misses)
for (int i : randomIndices) {
    sum += arr[i];
}
```

### Memory Layout

- **1D Arrays**: Single contiguous block of memory
- **2D Arrays**: Array of references, each pointing to a row array
- **Jagged Arrays**: Each row can have different length

### When Arrays Are Preferred Over Collections

1. **Fixed Size**: When the size is known and won't change
2. **Performance**: Critical performance requirements
3. **Primitive Types**: Avoids boxing overhead (use `int[]` instead of `List<Integer>`)
4. **Memory**: More memory-efficient for dense data
5. **Simplicity**: Direct indexing without method calls

---

## Comparison with Collections

| Aspect | Arrays | ArrayList |
|--------|--------|-----------|
| Size | Fixed | Dynamic |
| Memory | Contiguous | May be fragmented |
| Primitive Support | Direct | Requires boxing |
| Performance | Faster | Slightly slower |
| Bounds Checking | Manual | Automatic |
| Methods | Limited | Rich API |

---

## Summary

Arrays are fundamental data structures providing:
- **O(1) random access**
- **Memory efficiency**
- **Cache-friendly layout**
- **Direct primitive support**

They are the building blocks for many algorithms and are essential for:
- Grid/matrix problems
- Distance/cost matrices
- Efficient data storage
- Performance-critical code

---

## References

- [Java Arrays Documentation](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [Back to Main Documentation](../data-structures/README.md)

