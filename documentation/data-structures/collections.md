# Java Collections Framework

This document details all Java Collections Framework data structures used in the Advent of Code solutions.

## List Implementations

### ArrayList

**Description**: Dynamic array implementation providing O(1) random access and O(1) amortized insertion at the end.

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(1)* (amortized at end), O(n) at arbitrary position
- Deletion: O(n)

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

**When to Use**:
- Need dynamic sizing with frequent random access
- Frequent insertions at the end
- Iterating through elements sequentially

---

### LinkedList

**Description**: Doubly-linked list implementation, efficient for insertions/deletions at arbitrary positions.

**Time Complexity**:
- Access: O(n)
- Search: O(n)
- Insertion: O(1) at known position
- Deletion: O(1) at known position

**Usage Examples**:
- **2025 Day 11** (`GraphPathVisualization.java`): Queue operations for BFS traversal
- **2024 Day 18** (`RamRun.java`): Queue for pathfinding algorithms
- **2016 Day 17** (`TwoStepsForward.java`): Queue for state space exploration

**Code Reference**:
```java
Queue<Integer> queue = new LinkedList<>();
```

**When to Use**:
- Need frequent insertions/deletions at arbitrary positions
- Rarely used in practice; prefer ArrayList or ArrayDeque for most cases
- Note: Often used as Queue implementation, but ArrayDeque is preferred

---

## Set Implementations

### HashSet

**Description**: Hash table-based set implementation providing O(1) average-case operations for add, remove, and contains.

**Time Complexity**:
- Access: O(1)* (average case)
- Search: O(1)* (average case)
- Insertion: O(1)* (average case)
- Deletion: O(1)* (average case)
- *Worst case: O(n) due to hash collisions

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

**When to Use**:
- Default choice for set operations
- Need O(1) average-case operations
- Don't need sorted order
- Tracking unique elements, visited nodes, etc.

---

### TreeSet

**Description**: Red-black tree-based set implementation providing O(log n) operations with sorted order.

**Time Complexity**:
- Access: O(log n)
- Search: O(log n)
- Insertion: O(log n)
- Deletion: O(log n)
- Traversal: O(n) in sorted order

**Usage Examples**:
- **2024 Day 9** (`DiskCompactor.java`): Managing gaps sorted by position for efficient file compaction
- **2024 Day 23** (`LanParty.java`): Storing sorted triangle sets

**Code Reference**:
```java
TreeSet<GapInfo> gapSet = new TreeSet<>(gapComparator);
Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor1, neighbor2));
```

**When to Use**:
- Need sorted order along with efficient operations
- Need to iterate in sorted order
- Need range queries (subSet, headSet, tailSet)
- Willing to trade O(log n) for sorted order

---

### LinkedHashSet

**Description**: Hash table and linked list implementation maintaining insertion order.

**Time Complexity**:
- Access: O(1)* (average case)
- Search: O(1)* (average case)
- Insertion: O(1)* (average case)
- Deletion: O(1)* (average case)

**Usage Examples**:
- Used for maintaining order while providing O(1) lookup performance
- When you need both HashSet performance and predictable iteration order

**When to Use**:
- Need O(1) lookup performance
- Need to maintain insertion order
- Need predictable iteration order

---

## Map Implementations

### HashMap

**Description**: Hash table-based map implementation providing O(1) average-case operations.

**Time Complexity**:
- Access: O(1)* (average case)
- Search: O(1)* (average case)
- Insertion: O(1)* (average case)
- Deletion: O(1)* (average case)
- *Worst case: O(n) due to hash collisions

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

**When to Use**:
- Default choice for key-value mappings
- Need O(1) average-case operations
- Don't need sorted keys
- Memoization, caching, graph representations

---

### TreeMap

**Description**: Red-black tree-based map implementation providing O(log n) operations with sorted keys.

**Time Complexity**:
- Access: O(log n)
- Search: O(log n)
- Insertion: O(log n)
- Deletion: O(log n)
- Traversal: O(n) in sorted key order

**Usage Examples**:
- Used when sorted key order is required along with efficient lookups
- Range queries on keys

**When to Use**:
- Need sorted key order
- Need range queries (subMap, headMap, tailMap)
- Willing to trade O(log n) for sorted keys

---

### LinkedHashMap

**Description**: Hash table and linked list implementation maintaining insertion order.

**Time Complexity**:
- Access: O(1)* (average case)
- Search: O(1)* (average case)
- Insertion: O(1)* (average case)
- Deletion: O(1)* (average case)

**Usage Examples**:
- Used for maintaining insertion order while providing O(1) lookup performance
- LRU cache implementation (using access order)

**When to Use**:
- Need O(1) lookup performance
- Need to maintain insertion/access order
- Implementing LRU cache

---

## Queue Implementations

### PriorityQueue

**Description**: Heap-based priority queue providing O(log n) insertion and O(log n) removal of minimum element.

**Time Complexity**:
- Access (peek): O(1)
- Search: O(n)
- Insertion: O(log n)
- Deletion (poll): O(log n)

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

**When to Use**:
- Algorithms requiring priority-based processing
- Dijkstra's algorithm
- A* pathfinding
- State space search with priority ordering
- Any scenario where you need to process elements in priority order

---

### ArrayDeque

**Description**: Resizable array-based deque providing O(1) operations for add/remove at both ends.

**Time Complexity**:
- Access (both ends): O(1)
- Search: O(n)
- Insertion (both ends): O(1)
- Deletion (both ends): O(1)

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

**When to Use**:
- Preferred over Stack for LIFO operations
- Efficient FIFO queues
- BFS traversal
- Stack-based algorithms (iterative DFS, backtracking)
- When you need efficient operations at both ends

---

### Stack (Legacy)

**Description**: Legacy stack implementation (extends Vector). Prefer ArrayDeque for new code.

**Time Complexity**:
- Access (top): O(1)
- Search: O(n)
- Insertion (push): O(1)
- Deletion (pop): O(1)

**Usage Examples**:
- **2022 Day 7** (`FileSystemRecreation.java`): Maintaining directory path during file system traversal

**Code Reference**:
```java
Stack<String> paths = new Stack<>();
```

**When to Use**:
- Legacy code compatibility
- **Note**: Prefer ArrayDeque for new code as Stack is synchronized and extends Vector

---

## External Data Structure Libraries

### DataFrame (dataframe-ec)

**Description**: Tabular data structure from the `dataframe-ec` library (built on Eclipse Collections) providing columnar data manipulation with functional operations.

**Library**: `io.github.vmzakharov:dataframe-ec`

**Time Complexity**:
- Column access: O(1)
- Row iteration: O(n)
- Filter/collect operations: O(n)
- Row insertion: O(1)* (amortized)

**Usage Examples**:
- **2025 Day 1** (`DialRotator2.java`): Pure DataFrame-oriented solution with single-pass processing and direct aggregation
- **2025 Day 2** (`InvalidIdValidator3.java`): Lazy on-demand processing with DataFrame collect for range expansion and validation

**Code Reference**:
```java
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import org.eclipse.collections.api.factory.Lists;

// Create DataFrame from data (single DataFrame)
final DataFrame df = new DataFrame("Rotations")
        .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

// Single-pass processing with direct aggregation
// Accumulator pattern: [position, zeroCount]
final int[] accumulator = {INITIAL_POSITION, 0};

df.collect(
        () -> null,
        (acc, cursor) -> {
            final String rotationStr = cursor.getString("rotation");
            
            // Inline validation and processing
            if (isValidRotation(rotationStr)) {
                // Parse direction and distance directly without intermediate objects
                final char directionChar = parseDirection(rotationStr);
                final int distance = parseDistance(rotationStr);
                final Direction direction = Direction.from(directionChar);

                // Apply rotation and accumulate zero count directly
                accumulator[0] = rotateDial(accumulator[0], direction, distance);
                if (accumulator[0] == 0) {
                    accumulator[1]++;
                }
            }
        }
);

return accumulator[1];
```

**When to Use**:
- Need tabular data representation with named columns
- Want functional-style data manipulation operations
- Processing structured data with filtering and aggregation
- When working with Eclipse Collections ecosystem
- Alternative to Stream API for data processing pipelines
- Need single-pass processing with direct aggregation (avoiding intermediate collections)

**Key Features**:
- Column-based data storage
- Type-safe column access (`getString()`, `getInt()`, `getLong()`, etc.)
- Functional operations: `collect()`, `forEach()`, `selectBy()`
- Integration with Eclipse Collections
- Single-pass processing pattern for optimal performance

**Performance Best Practices**:
- **Single DataFrame**: Create one DataFrame and process it in a single pass
- **Direct Aggregation**: Use accumulator arrays/objects for direct accumulation instead of building intermediate collections
- **Inline Parsing**: Parse and process data inline during DataFrame iteration to avoid multiple passes
- **Avoid Intermediate Collections**: Eliminate `MutableList`, `MutableIntList`, etc. when possible - use direct accumulation instead
- **Single-Pass Pattern**: Combine parsing, validation, and processing in one `collect()` call for O(n) time complexity

---

## Eclipse Collections Primitive Structures

### LongIntHashMap

**Description**: Primitive map implementation from Eclipse Collections providing O(1) average-case operations for long keys and int values, avoiding boxing overhead.

**Library**: `org.eclipse.collections:collections`

**Time Complexity**:
- Access: O(1)* (average case)
- Search: O(1)* (average case)
- Insertion: O(1)* (average case)
- Deletion: O(1)* (average case)
- *Worst case: O(n) due to hash collisions

**Memory**: More memory-efficient than `HashMap<Long, Integer>` by avoiding boxing overhead.

**Usage Examples**:
- **2025 Day 10** (`Part1Solver.java`): Meet-in-the-middle algorithm storing XOR state → minimum presses mapping
  ```java
  import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;
  
  LongIntHashMap leftMap = new LongIntHashMap(1 << leftSize);
  leftMap.put(0L, 0);
  int existing = leftMap.getIfAbsent(current, Integer.MAX_VALUE);
  ```

**Code Reference**:
```java
import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

// Pre-sized to avoid resizing
LongIntHashMap map = new LongIntHashMap(initialCapacity);

// Operations
map.put(key, value);
int value = map.get(key);
int value = map.getIfAbsent(key, defaultValue);
boolean contains = map.containsKey(key);
```

**When to Use**:
- Need primitive long → int mappings
- Performance-critical code where boxing overhead matters
- Pre-sized maps for predictable memory usage
- When working with Eclipse Collections ecosystem

**Advantages**:
- No boxing overhead (faster than `HashMap<Long, Integer>`)
- Memory efficient (primitive storage)
- Pre-sizing support for predictable performance
- Same O(1) average complexity as HashMap

**Disadvantages**:
- External dependency (Eclipse Collections)
- Less flexible than generic HashMap (fixed key/value types)

---

### MutableLongList / LongArrayList

**Description**: Primitive list implementations from Eclipse Collections providing O(1) average-case operations for long values, avoiding boxing overhead.

**Library**: `org.eclipse.collections:collections`

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(1)* (amortized at end)
- Deletion: O(n)

**Memory**: More memory-efficient than `ArrayList<Long>` by avoiding boxing overhead.

**Usage Examples**:
- **2025 Day 5** (`Range2.java`): Using `List<Interval>` for immutable range collections, `List<Long>` for IDs in `RangeProblemInput`
  ```java
  import org.eclipse.collections.api.list.primitive.MutableLongList;
  import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
  
  MutableLongList startList = new LongArrayList();
  MutableLongList endList = new LongArrayList();
  ```

**Code Reference**:
```java
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

MutableLongList list = new LongArrayList();
list.add(value);
long value = list.get(index);
int size = list.size();
```

**When to Use**:
- Need primitive long lists
- Performance-critical code where boxing overhead matters
- When working with Eclipse Collections ecosystem
- Processing large sequences of primitive values

**Advantages**:
- No boxing overhead (faster than `ArrayList<Long>`)
- Memory efficient (primitive storage)
- Rich functional API from Eclipse Collections
- Same O(1) average complexity as ArrayList

**Disadvantages**:
- External dependency (Eclipse Collections)
- Less flexible than generic ArrayList (fixed element type)

---

## FastUtil Library

FastUtil provides high-performance primitive collections and object collections with better memory layout and cache locality than standard Java collections.

**Library**: `it.unimi.dsi:fastutil`

### ObjectArrayList / ObjectList

**Description**: FastUtil's object list implementation providing better memory layout and cache locality than `ArrayList` for object collections.

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(1)* (amortized at end)
- Deletion: O(n)

**Memory**: Better memory layout with contiguous storage, improved cache locality compared to `ArrayList`.

**Usage Examples**:
- **2025 Day 1** (`DialRotator3.java`): Efficient iteration over rotation strings
  ```java
  import it.unimi.dsi.fastutil.objects.ObjectArrayList;
  import it.unimi.dsi.fastutil.objects.ObjectList;
  
  final ObjectList<String> lines = new ObjectArrayList<>(allLines);
  ```
- **2025 Day 8** (`PointCluster3.java`): Point and edge collections for optimized sorting and iteration
  ```java
  import it.unimi.dsi.fastutil.objects.ObjectArrayList;
  import it.unimi.dsi.fastutil.objects.ObjectList;
  
  final ObjectList<Point3D> points = new ObjectArrayList<>(lines.size());
  final ObjectList<Edge> edges = new ObjectArrayList<>(estimatedSize);
  ```

**Code Reference**:
```java
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

ObjectList<String> list = new ObjectArrayList<>(initialCapacity);
list.add(element);
String element = list.get(index);
int size = list.size();
```

**When to Use**:
- Performance-critical object collections
- Need better cache locality than standard ArrayList
- Working with FastUtil ecosystem
- Large collections where memory layout matters

**Advantages**:
- Better memory layout and cache locality
- Optimized for iteration and sorting
- Rich functional API
- Same O(1) average complexity as ArrayList

**Disadvantages**:
- External dependency (FastUtil)
- Less familiar API than standard collections

---

### LongArrayList / LongList

**Description**: FastUtil's primitive long list implementation avoiding boxing overhead.

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(1)* (amortized at end)
- Deletion: O(n)

**Memory**: More memory-efficient than `ArrayList<Long>` by avoiding boxing overhead.

**Usage Examples**:
- **2025 Day 3** (`Bank.java`): Storing digit sequences
  ```java
  import it.unimi.dsi.fastutil.longs.LongArrayList;
  import it.unimi.dsi.fastutil.longs.LongList;
  
  final LongList digits = new LongArrayList(line.length());
  ```
- **2025 Day 5** (`Range3.java`): Processing ID ranges
  ```java
  import it.unimi.dsi.fastutil.longs.LongArrayList;
  import it.unimi.dsi.fastutil.longs.LongList;
  
  final LongList result = new LongArrayList(ids.size());
  ```

**Code Reference**:
```java
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

LongList list = new LongArrayList(initialCapacity);
list.add(value);
long value = list.getLong(index);
int size = list.size();
```

**When to Use**:
- Need primitive long lists
- Performance-critical code where boxing overhead matters
- Processing large sequences of long values
- When working with FastUtil ecosystem

**Advantages**:
- No boxing overhead (faster than `ArrayList<Long>`)
- Memory efficient (primitive storage)
- Rich functional API
- Same O(1) average complexity as ArrayList

**Disadvantages**:
- External dependency (FastUtil)
- Less flexible than generic ArrayList (fixed element type)

---

### IntArrayList / IntList

**Description**: FastUtil's primitive int list implementation avoiding boxing overhead.

**Time Complexity**:
- Access: O(1)
- Search: O(n)
- Insertion: O(1)* (amortized at end)
- Deletion: O(n)

**Memory**: More memory-efficient than `ArrayList<Integer>` by avoiding boxing overhead.

**Usage Examples**:
- **2025 Day 3** (`Bank.java`): Storing digit sequences
  ```java
  import it.unimi.dsi.fastutil.ints.IntArrayList;
  import it.unimi.dsi.fastutil.ints.IntList;
  
  final IntList digits = new IntArrayList(line.length());
  ```
- **2025 Day 8** (`DSU.java`): Component size tracking
  ```java
  import it.unimi.dsi.fastutil.ints.IntArrayList;
  import it.unimi.dsi.fastutil.ints.IntList;
  
  IntList sizes = new IntArrayList();
  ```

**Code Reference**:
```java
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

IntList list = new IntArrayList(initialCapacity);
list.add(value);
int value = list.getInt(index);
int size = list.size();
```

**When to Use**:
- Need primitive int lists
- Performance-critical code where boxing overhead matters
- Processing large sequences of int values
- When working with FastUtil ecosystem

**Advantages**:
- No boxing overhead (faster than `ArrayList<Integer>`)
- Memory efficient (primitive storage)
- Rich functional API
- Same O(1) average complexity as ArrayList

**Disadvantages**:
- External dependency (FastUtil)
- Less flexible than generic ArrayList (fixed element type)

---

## Summary

| Collection | Best For | Time Complexity | Order |
|------------|----------|-----------------|-------|
| ArrayList | Random access, dynamic sizing | O(1) access, O(1)* insert at end | Insertion order |
| LinkedList | Insertions/deletions at arbitrary positions | O(n) access, O(1) insert/delete | Insertion order |
| HashSet | Fast set operations | O(1)* average | No order |
| TreeSet | Sorted set operations | O(log n) | Sorted order |
| LinkedHashSet | Set with insertion order | O(1)* average | Insertion order |
| HashMap | Fast key-value mappings | O(1)* average | No order |
| TreeMap | Sorted key-value mappings | O(log n) | Sorted key order |
| LinkedHashMap | Map with insertion order | O(1)* average | Insertion/access order |
| PriorityQueue | Priority-based processing | O(log n) insert/remove | Priority order |
| ArrayDeque | Queue/Stack operations | O(1) at both ends | Insertion order |
| Stack | Legacy LIFO (avoid in new code) | O(1) push/pop | LIFO order |
| DataFrame | Tabular data manipulation | O(n) iteration, O(1) column access | Column order |
| LongIntHashMap | Primitive long→int mappings | O(1)* average | No order |
| MutableLongList | Primitive long lists | O(1) access, O(1)* insert at end | Insertion order |
| ObjectArrayList | Object collections with better cache locality | O(1) access, O(1)* insert at end | Insertion order |
| LongArrayList | Primitive long lists (FastUtil) | O(1) access, O(1)* insert at end | Insertion order |
| IntArrayList | Primitive int lists (FastUtil) | O(1) access, O(1)* insert at end | Insertion order |

*Average case; worst case may be O(n) for hash-based structures.

---

## References

- [Java Collections Framework Documentation](https://docs.oracle.com/javase/tutorial/collections/)
- [Back to Main Documentation](../data-structures/README.md)

