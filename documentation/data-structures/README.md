# Data Structures Used in Advent of Code Solutions

This document catalogs all data structures used across the Advent of Code problem solutions in this repository. The data structures are organized by category with examples from the codebase.

## Table of Contents

1. [Java Collections Framework](collections.md)
2. [Arrays](arrays.md)
3. [Custom Data Structures](custom-structures.md)
4. [Graph Data Structures](graphs.md)
5. [Tree Data Structures](trees.md)
6. [Grid/Matrix Data Structures](grids.md)
7. [Summary by Year](#summary-by-year)
8. [Data Structure Selection Guidelines](#data-structure-selection-guidelines)

---

## Quick Reference

### Java Collections Framework
- **Lists**: [ArrayList](collections.md#arraylist), [LinkedList](collections.md#linkedlist)
- **Sets**: [HashSet](collections.md#hashset), [TreeSet](collections.md#treeset), [LinkedHashSet](collections.md#linkedhashset)
- **Maps**: [HashMap](collections.md#hashmap), [TreeMap](collections.md#treemap), [LinkedHashMap](collections.md#linkedhashmap)
- **Queues**: [PriorityQueue](collections.md#priorityqueue), [ArrayDeque](collections.md#arraydeque), [Stack](collections.md#stack-legacy)
- **External Libraries**: [DataFrame](collections.md#dataframe-dataframe-ec) (dataframe-ec)

See [Collections Documentation](collections.md) for detailed information.

### Arrays
- **One-Dimensional**: [Primitive arrays](arrays.md#one-dimensional-arrays) (`int[]`, `char[]`, `long[]`)
- **Multi-Dimensional**: [2D/3D arrays](arrays.md#multi-dimensional-arrays) (`int[][]`, `char[][]`)

See [Arrays Documentation](arrays.md) for detailed information.

### Custom Data Structures
- **DSU (Union-Find)**: [Disjoint Set Union](custom-structures.md#disjoint-set-union-dsu--union-find) implementation
- **Records**: [Immutable data structures](custom-structures.md#records-immutable-data-structures) (Java records)
- **Custom Classes**: [Domain-specific classes](custom-structures.md#custom-classes)

See [Custom Structures Documentation](custom-structures.md) for detailed information.

### Graph Data Structures
- **Adjacency Lists**: [Graph representation](graphs.md#adjacency-list-representation) using Maps
- **Graph Algorithms**: BFS, DFS, Dijkstra's, path counting, clique finding

See [Graphs Documentation](graphs.md) for detailed information.

### Tree Data Structures
- **File System Trees**: [HashMap-based tree representation](trees.md#file-system-tree)

See [Trees Documentation](trees.md) for detailed information.

### Grid/Matrix Data Structures
- **2D Arrays**: [Grid representations](grids.md#2d-arrays) using primitive arrays
- **Grid Classes**: [Custom grid utilities](grids.md#grid-classes)

See [Grids Documentation](grids.md) for detailed information.

---

## Summary by Year

### 2015
- **HashMap**: Circuit evaluation, route optimization
- **HashSet**: Tracking visited cities, state management
- **ArrayList**: Instruction lists, ingredient lists
- **LinkedList/Queue**: Circuit evaluation (topological sort)
- **PriorityQueue**: Wizard simulator state search
- **2D Arrays**: Light grid manipulation

### 2016
- **HashMap**: Graph representations, node mappings
- **HashSet**: Position tracking, visited states
- **ArrayList**: Instruction lists, position lists
- **ArrayDeque/Queue**: BFS pathfinding, state exploration
- **LinkedList**: Queue operations
- **2D Arrays**: Screen manipulation, grid computing

### 2017
- **HashMap**: Graph adjacency lists, register storage
- **HashSet**: Visited node tracking, cycle detection
- **ArrayList**: Instruction lists, state lists
- **ArrayDeque/Queue**: BFS traversal, grid operations
- **2D Arrays**: Grid manipulation

### 2018
- **HashMap**: Various data mappings
- **ArrayList**: Data processing
- **2D Arrays**: Grid operations

### 2019
- **ArrayList**: Program execution
- **char[]**: Password manipulation

### 2022
- **HashMap**: File system representation
- **Stack**: Directory path tracking
- **ArrayList**: Data processing

### 2023
- **HashMap**: Data mappings
- **HashSet**: Set operations
- **ArrayList**: Data processing
- **2D Arrays**: Grid operations

### 2024
- **HashMap**: File/gap mappings, graph representations, memoization
- **HashSet**: Visited tracking, cheat spot tracking
- **TreeSet**: Sorted gap management
- **ArrayList**: Path storage, position lists
- **PriorityQueue**: Dijkstra's algorithm
- **ArrayDeque**: Queue/Stack operations
- **2D Arrays**: Distance matrices, grid representations
- **Records**: `GapInfo` and other immutable data structures

### 2025
- **HashMap**: Graph adjacency lists, memoization, shape/region mappings, cache keys
- **HashSet**: Unique variant tracking, visited sets, beam position tracking
- **ArrayList**: Shape point collection, path lists, instruction lists, range processing
- **PriorityQueue**: Point clustering (max-heap for top-k connections)
- **ArrayDeque**: Queue operations
- **DSU**: Custom Union-Find implementation with path compression and union by size
- **Records**: Extensive use of records for immutable data:
  - **Day 1**: `Rotation`, `DialState`, `Sequence`
  - **Day 2**: `Range`
  - **Day 3**: `Bank`, `MaxDigitResult`
  - **Day 5**: `Interval`, `RangeProblemInput`, `RangeMergeState`
  - **Day 6**: `ColumnRange`, `MathOperator` (sealed interface with records)
  - **Day 7**: `BeamAction`, `SplitResult`
  - **Day 8**: `Point3D`, `Edge`
  - **Day 9**: `PointPair`
  - **Day 10**: `Part1Problem`, `Part2Problem`
  - **Day 11**: `GraphNode`, `GraphEdge`, `PathPair`
  - **Day 12**: `Point`, `Shape`, `ShapeVariant`, `Region`, `ParsedData`
- **Arrays**: 
  - **Bitmask arrays** (`long[]`): Grid state representation for shape packing (Day 12), O(1) bitwise operations
  - **Interleaved arrays** (`long[]`): Rational matrix storage for better cache locality (Day 10)
  - **Primitive arrays**: Parent/size arrays in DSU (`int[]`), character arrays for ID validation (`char[]`)
- **FastUtil Library**:
  - **ObjectArrayList/ObjectList**: Object collections with better cache locality (Day 1, Day 8)
  - **LongArrayList/LongList**: Primitive long lists avoiding boxing overhead (Day 3, Day 5)
  - **IntArrayList/IntList**: Primitive int lists avoiding boxing overhead (Day 3, Day 8)
- **Eclipse Collections**:
  - **LongIntHashMap**: Primitive map for meet-in-the-middle algorithm (Day 10)
  - **MutableLongList/LongArrayList**: Primitive lists for range processing (Day 5)

---

## Data Structure Selection Guidelines

### When to Use Each Structure

1. **ArrayList**: When you need dynamic sizing with frequent random access
2. **LinkedList**: When you need frequent insertions/deletions at arbitrary positions (rarely used in practice)
3. **HashMap**: Default choice for key-value mappings with O(1) average operations
4. **HashSet**: Default choice for set operations with O(1) average operations
5. **TreeSet/TreeMap**: When you need sorted order along with efficient operations
6. **PriorityQueue**: For algorithms requiring priority-based processing (Dijkstra, A*, etc.)
7. **ArrayDeque**: Preferred over Stack for LIFO operations, also efficient for FIFO queues
8. **Arrays**: For fixed-size data with known dimensions, especially for 2D grids
9. **DSU**: For connected component problems, cycle detection, equivalence relations
10. **Records**: For immutable data carriers with clear structure
11. **DataFrame**: For tabular data processing with functional operations, when working with structured columnar data
12. **Bitmask Arrays** (`long[]`): For efficient 2D grid representation with O(1) bitwise operations, binary state tracking
13. **Interleaved Arrays** (`long[]`): For storing pairs of related values with better cache locality (numerator/denominator, x/y coordinates)
14. **Eclipse Collections Primitive Maps**: For performance-critical primitive key-value mappings (LongIntHashMap)
15. **Eclipse Collections Primitive Lists**: For performance-critical primitive lists (MutableLongList, LongArrayList)
16. **FastUtil ObjectArrayList**: For object collections with better memory layout and cache locality than ArrayList
17. **FastUtil Primitive Lists**: For performance-critical primitive lists avoiding boxing overhead (LongArrayList, IntArrayList)

### Performance Characteristics

| Data Structure | Access | Search | Insertion | Deletion | Notes |
|---------------|--------|--------|-----------|----------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | Fixed size |
| ArrayList | O(1) | O(n) | O(1)* | O(n) | *Amortized at end |
| LinkedList | O(n) | O(n) | O(1) | O(1) | At known position |
| HashMap | O(1)* | O(1)* | O(1)* | O(1)* | *Average case |
| HashSet | O(1)* | O(1)* | O(1)* | O(1)* | *Average case |
| TreeSet | O(log n) | O(log n) | O(log n) | O(log n) | Sorted |
| TreeMap | O(log n) | O(log n) | O(log n) | O(log n) | Sorted keys |
| PriorityQueue | O(1) | O(n) | O(log n) | O(log n) | Min at root |
| ArrayDeque | O(1) | O(n) | O(1) | O(1) | Both ends |
| DSU | O(α(n)) | O(α(n)) | O(α(n)) | - | Near constant |
| DataFrame | O(1) | O(n) | O(1)* | O(n) | Columnar access |

*α(n) is the inverse Ackermann function, effectively constant for practical purposes.
*DataFrame row insertion is O(1) amortized at end.

---

## References

- [Java Collections Framework Documentation](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 57: Minimize the scope of local variables](https://www.oracle.com/technical-resources/articles/java/architect-lambdas-part1.html)
- [Disjoint Set Union (Union-Find) - Wikipedia](https://en.wikipedia.org/wiki/Disjoint-set_data_structure)
- [Big O Notation Documentation](../big-o-notation/README.md)
