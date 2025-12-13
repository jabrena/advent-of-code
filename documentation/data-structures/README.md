# Data Structures Used in Advent of Code Solutions

This document catalogs all data structures used across the Advent of Code problem solutions in this repository. The data structures are organized by category with examples from the codebase.

## Table of Contents

1. [Java Collections Framework](#java-collections-framework)
2. [Arrays](#arrays)
3. [Custom Data Structures](#custom-data-structures)
4. [Graph Data Structures](#graph-data-structures)
5. [Tree Data Structures](#tree-data-structures)
6. [Grid/Matrix Data Structures](#gridmatrix-data-structures)
7. [Summary by Year](#summary-by-year)

---

## Java Collections Framework

### List Implementations

#### ArrayList
**Description**: Dynamic array implementation providing O(1) random access and O(1) amortized insertion at the end.

**Usage Examples**:
- **2024 Day 20** (`RaceCondition.java`): Storing path information and position lists
- **2025 Day 12** (`Shape.java`): Collecting shape points during parsing
- **2017 Day 12** (`Day12.java`): Building graph adjacency lists
- **2016 Day 22** (`GridComputing.java`): Managing grid states and positions

**Code Reference**:
```java
ArrayList<String> startList = new ArrayList<>();
List<Point> points = new ArrayList<>();
```

#### LinkedList
**Description**: Doubly-linked list implementation, efficient for insertions/deletions at arbitrary positions.

**Usage Examples**:
- **2025 Day 11** (`GraphPathVisualization.java`): Queue operations for BFS traversal
- **2024 Day 18** (`RamRun.java`): Queue for pathfinding algorithms
- **2016 Day 17** (`TwoStepsForward.java`): Queue for state space exploration

**Code Reference**:
```java
Queue<Integer> queue = new LinkedList<>();
```

### Set Implementations

#### HashSet
**Description**: Hash table-based set implementation providing O(1) average-case operations for add, remove, and contains.

**Usage Examples**:
- **2024 Day 20** (`RaceCondition.java`): Tracking visited nodes and cheat spots
- **2024 Day 23** (`LanParty.java`): Graph adjacency representation and triangle/clique finding
- **2025 Day 12** (`Shape.java`): Storing unique shape variants
- **2017 Day 12** (`Day12.java`): Tracking visited nodes in BFS
- **2015 Day 9** (`RouteOptimizer.java`): Tracking visited cities in TSP

**Code Reference**:
```java
HashSet<String> visited = new HashSet<>();
Set<String> neighbors = new HashSet<>();
```

#### TreeSet
**Description**: Red-black tree-based set implementation providing O(log n) operations with sorted order.

**Usage Examples**:
- **2024 Day 9** (`DiskCompactor.java`): Managing gaps sorted by position for efficient file compaction
- **2024 Day 23** (`LanParty.java`): Storing sorted triangle sets

**Code Reference**:
```java
TreeSet<GapInfo> gapSet = new TreeSet<>(gapComparator);
Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor1, neighbor2));
```

#### LinkedHashSet
**Description**: Hash table and linked list implementation maintaining insertion order.

**Usage Examples**:
- Used for maintaining order while providing O(1) lookup performance

### Map Implementations

#### HashMap
**Description**: Hash table-based map implementation providing O(1) average-case operations.

**Usage Examples**:
- **2024 Day 9** (`DiskCompactor.java`): Mapping file IDs to positions and gap information
- **2024 Day 20** (`RaceCondition.java`): Caching positions and path indices
- **2025 Day 12** (`ShapePacking.java`): Memoization cache for backtracking
- **2025 Day 11** (`GraphPathCounter.java`): Graph adjacency lists and memoization
- **2022 Day 7** (`FileSystemRecreation.java`): File system path to size mapping
- **2017 Day 12** (`Day12.java`): Graph representation as adjacency list

**Code Reference**:
```java
Map<Integer, int[]> starting = new HashMap<>();
Map<String, Integer> pathIndex = new HashMap<>();
Map<GraphNode, List<GraphNode>> graph = new HashMap<>();
```

#### TreeMap
**Description**: Red-black tree-based map implementation providing O(log n) operations with sorted keys.

**Usage Examples**:
- Used when sorted key order is required along with efficient lookups

#### LinkedHashMap
**Description**: Hash table and linked list implementation maintaining insertion order.

**Usage Examples**:
- Used for maintaining insertion order while providing O(1) lookup performance

### Queue Implementations

#### PriorityQueue
**Description**: Heap-based priority queue providing O(log n) insertion and O(log n) removal of minimum element.

**Usage Examples**:
- **2024 Day 20** (`RaceCondition.java`): Dijkstra's algorithm for shortest path finding
- **2025 Day 8** (`PointCluster.java`): Clustering points with priority-based processing
- **2015 Day 22** (`WizardSimulator.java`): State space search with priority ordering

**Code Reference**:
```java
PriorityQueue<int[]> unvisited = new PriorityQueue<>(
    Comparator.comparingLong(a -> distances[a[0]][a[1]])
);
```

#### ArrayDeque
**Description**: Resizable array-based deque providing O(1) operations for add/remove at both ends.

**Usage Examples**:
- **2024 Day 18** (`RamRun.java`): Queue for BFS pathfinding
- **2024 Day 23** (`LanParty.java`): Stack for iterative Bron-Kerbosch algorithm
- **2017 Day 12** (`Day12.java`): Queue for BFS traversal
- **2017 Day 14** (`Day14.java`): Queue operations for grid traversal
- **2016 Day 22** (`GridComputing.java`): Queue for state space exploration

**Code Reference**:
```java
Queue<Integer> queue = new ArrayDeque<>();
Deque<State> stack = new ArrayDeque<>();
```

#### Stack (Legacy)
**Description**: Legacy stack implementation (extends Vector). Prefer ArrayDeque for new code.

**Usage Examples**:
- **2022 Day 7** (`FileSystemRecreation.java`): Maintaining directory path during file system traversal

**Code Reference**:
```java
Stack<String> paths = new Stack<>();
```

---

## Arrays

### Primitive Arrays

#### One-Dimensional Arrays
**Description**: Fixed-size arrays providing O(1) random access.

**Usage Examples**:
- **2024 Day 9** (`DiskCompactor.java`): `int[]` for disk representation and cumulative positions
- **2024 Day 20** (`RaceCondition.java`): `int[]` for position coordinates
- **2025 Day 8** (`DSU.java`): `int[]` for parent and size arrays in Union-Find
- **2025 Day 12** (`CacheKey.java`): `long[]` for grid state representation
- **2019 Day 4** (`SecureContainerTest.java`): `char[]` for password digit manipulation

**Code Reference**:
```java
int[] parts = new int[input.length()];
int[] disk = new int[totalBlocks];
int[] parent = new int[n];
int[] size = new int[n];
char[] passwordDigits = String.valueOf(password).toCharArray();
```

#### Multi-Dimensional Arrays
**Description**: Arrays of arrays for 2D/3D data structures.

**Usage Examples**:
- **2024 Day 20** (`RaceCondition.java`): `int[][]` for distance matrix in Dijkstra's algorithm
- **2016 Day 8** (`TwoFactorAuthentication.java`): `char[][]` for 2D screen representation
- **2015 Day 6** (`LightCounter.java`): `int[][]` or `boolean[][]` for 2D grid manipulation

**Code Reference**:
```java
int[][] distances = new int[grid.maxY()][grid.maxX()];
char[][] screen = new char[height][width];
```

---

## Custom Data Structures

### Disjoint Set Union (DSU) / Union-Find

**Description**: Efficient data structure for tracking disjoint sets with near-constant amortized time complexity O(α(n)).

**Implementation**: **2025 Day 8** (`DSU.java`)

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

### Records (Immutable Data Structures)

**Description**: Java records provide a concise way to define immutable data carriers.

**Usage Examples**:
- **2025 Day 12**: `ParsedData`, `Region`, `Shape`, `ShapeVariant`, `Point`, `CacheKey`
- **2025 Day 11**: `GraphNode`, `GraphEdge`, `PathPair`
- **2025 Day 9**: `Point`, `Edge`
- **2025 Day 8**: `Point3D`, `Connection`
- **2024 Day 9**: `GapInfo`
- **2015 Day 21**: `Character`, `Item`, `Equipment`

**Code Reference**:
```16:23:2025/src/main/java/info/jab/aoc2025/day12/ParsedData.java
public record ParsedData(Map<Integer, Shape> shapes, List<Region> regions) {
    public ParsedData {
        shapes = Map.copyOf(shapes);
        regions = List.copyOf(regions);
    }
```

### Custom Classes

**Description**: Domain-specific classes for problem modeling.

**Examples**:
- **Graph Nodes/Edges**: `GraphNode`, `GraphEdge` (2025 Day 11)
- **Points**: `Point`, `Point3D` (various days)
- **Ranges**: `Range` (2025 Day 5, Day 2)
- **State Classes**: Various state representations for game simulations

---

## Graph Data Structures

### Adjacency List Representation

**Description**: Graph represented as `Map<Node, List<Node>>` or `Map<Node, Set<Node>>`.

**Usage Examples**:
- **2025 Day 11** (`GraphPathCounter.java`): Directed graph for path counting
- **2024 Day 23** (`LanParty.java`): Undirected graph for triangle and clique finding
- **2017 Day 12** (`Day12.java`): Graph connectivity using `Map<Integer, List<Integer>>`

**Code Reference**:
```28:36:2025/src/main/java/info/jab/aoc2025/day11/GraphPathCounter.java
private Map<GraphNode, List<GraphNode>> parseInput(final String fileName) {
        return ResourceLines.list(fileName).stream()
                        .filter(line -> !line.isBlank())
                        .map(this::parseLine)
                        .collect(Collectors.toUnmodifiableMap(
                                        GraphEdge::from,
                                        GraphEdge::to,
                                        (existing, replacement) -> existing));
}
```

### Graph Node and Edge Classes

**Description**: Custom classes representing graph vertices and edges.

**Examples**:
- `GraphNode`: Represents a node in the graph (2025 Day 11)
- `GraphEdge`: Represents an edge with source and destination (2025 Day 11)

---

## Tree Data Structures

### File System Tree

**Description**: Tree structure represented using HashMap for efficient path lookups.

**Usage Examples**:
- **2022 Day 7** (`FileSystemRecreation.java`): File system represented as `Map<String, Long>` with Stack for path tracking

**Code Reference**:
```25:48:2022/src/main/java/info/jab/aoc2022/day7/FileSystemRecreation.java
public static final Function<List<String>, Map<String, Long>> from = data -> {
        Map<String, Long> fsAsMap = new HashMap<>();
        var paths = new Stack<String>();
        for (var line : data) {
            //Case: Empty
            if (line.isEmpty()) {
                // Skip empty lines
            } else if (line.startsWith("$ cd")) {
                //Case $ cd
                if (line.equals("$ cd ..")) {
                    paths.pop();
                } else {
                    var dirName = SPACE_SEPARATOR_PATTERN.split(line)[2];
                    paths.add(paths.isEmpty() ? dirName : paths.peek() + dirName + "/");
                }
            } else if (!line.equals("$ ls") && !line.startsWith("dir")) {
                //Case: file (not $ ls and not dir)
                long weight = Long.parseLong(SPACE_SEPARATOR_PATTERN.split(line)[0]);
                paths.forEach(p -> fsAsMap.put(p, fsAsMap.getOrDefault(p, 0L) + weight));
            }
        }

        return fsAsMap;
    };
```

---

## Grid/Matrix Data Structures

### 2D Arrays

**Description**: Two-dimensional arrays for grid/matrix representation.

**Usage Examples**:
- **2024 Day 20** (`RaceCondition.java`): Distance matrix for Dijkstra's algorithm
- **2016 Day 8** (`TwoFactorAuthentication.java`): Screen representation
- **2015 Day 6** (`LightCounter.java`): Light grid manipulation

### Grid Classes

**Description**: Custom grid classes from third-party library (`com.putoet.grid.Grid`).

**Usage Examples**:
- Used extensively for 2D grid problems across multiple years
- Provides utilities for grid traversal, neighbor checking, and pathfinding

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
- **HashMap**: Graph adjacency lists, memoization, shape/region mappings
- **HashSet**: Unique variant tracking, visited sets
- **ArrayList**: Shape point collection, path lists
- **PriorityQueue**: Point clustering
- **ArrayDeque**: Queue operations
- **DSU**: Custom Union-Find implementation
- **Records**: Extensive use of records for immutable data (`ParsedData`, `Region`, `Shape`, `GraphNode`, `GraphEdge`, etc.)
- **Arrays**: Grid state representation, parent/size arrays in DSU

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

*α(n) is the inverse Ackermann function, effectively constant for practical purposes.

---

## References

- [Java Collections Framework Documentation](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 57: Minimize the scope of local variables](https://www.oracle.com/technical-resources/articles/java/architect-lambdas-part1.html)
- [Disjoint Set Union (Union-Find) - Wikipedia](https://en.wikipedia.org/wiki/Disjoint-set_data_structure)
- [Big O Notation Documentation](../big-o-notation/README.md)

