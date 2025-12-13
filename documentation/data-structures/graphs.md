# Graph Data Structures

This document details graph data structures and representations used in the Advent of Code solutions.

## Overview

Graphs are used extensively in Advent of Code problems for modeling relationships, connections, and paths. The solutions primarily use adjacency list representations for efficiency.

## Adjacency List Representation

**Description**: Graph represented as `Map<Node, List<Node>>` or `Map<Node, Set<Node>>`.

**Time Complexity**:
- Add vertex: O(1)
- Add edge: O(1)
- Check edge: O(degree(v)) for list, O(1) for set
- Iterate neighbors: O(degree(v))
- Space: O(V + E)

**Advantages**:
- Space efficient for sparse graphs
- Fast iteration over neighbors
- Easy to add/remove edges

**Disadvantages**:
- Slower edge existence check (O(degree(v)) for list)
- Less cache-friendly than adjacency matrix

### Directed Graphs

**Usage Examples**:

#### 2025 Day 11 - Graph Path Counting

Directed graph for counting paths between nodes:

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

**Representation**:
```java
Map<GraphNode, List<GraphNode>> graph = new HashMap<>();
// graph.get(node) returns list of neighbors
```

**Use Case**: Counting paths from source to target using memoization:
```java
private Long countPaths(
    final GraphNode current,
    final GraphNode target,
    final Map<GraphNode, List<GraphNode>> graph,
    final Map<GraphNode, Long> memo) {
    // Memoized recursive path counting
}
```

### Undirected Graphs

**Usage Examples**:

#### 2024 Day 23 - Lan Party

Undirected graph for finding triangles and cliques:

```java
private Map<String, Set<String>> getInputData2(String fileName) {
    return ResourceLines.list(fileName).stream()
            .map(line -> line.split("-"))
            .collect(
                    HashMap::new,
                    (map, parts) -> {
                        // Add edge in both directions (undirected)
                        map.computeIfAbsent(parts[0], k -> new HashSet<>()).add(parts[1]);
                        map.computeIfAbsent(parts[1], k -> new HashSet<>()).add(parts[0]);
                    },
                    // Merge function
            );
}
```

**Representation**:
```java
Map<String, Set<String>> graph = new HashMap<>();
// Each edge stored in both directions
```

**Use Cases**:
- Finding triangles (3-cliques)
- Finding maximal cliques using Bron-Kerbosch algorithm
- Network analysis

#### 2017 Day 12 - Graph Connectivity

Graph connectivity using integer nodes:

```java
Map<Integer, List<Integer>> graph = buildGraph(fileName);
Set<Integer> visited = new HashSet<>();
Queue<Integer> queue = new ArrayDeque<>();

queue.offer(0);
visited.add(0);

while (!queue.isEmpty()) {
    int current = queue.poll();
    List<Integer> neighbors = graph.getOrDefault(current, new ArrayList<>());
    for (int neighbor : neighbors) {
        if (!visited.contains(neighbor)) {
            visited.add(neighbor);
            queue.offer(neighbor);
        }
    }
}
```

**Use Case**: BFS to find all nodes connected to node 0.

---

## Graph Node and Edge Classes

### GraphNode

**Description**: Represents a node/vertex in the graph.

**Implementation** (2025 Day 11):
```java
public record GraphNode(String name) {
    public static GraphNode from(String name) {
        return new GraphNode(name);
    }
}
```

**Usage**:
- Unique identification of graph vertices
- Used as keys in adjacency maps
- Supports value semantics (equals, hashCode)

### GraphEdge

**Description**: Represents an edge with source and destination.

**Implementation** (2025 Day 11):
```java
public record GraphEdge(GraphNode from, List<GraphNode> to) {
    // Represents edges from 'from' to all nodes in 'to'
}
```

**Usage**:
- Parsing graph input
- Building adjacency lists
- Edge representation in algorithms

---

## Graph Algorithms Used

### Breadth-First Search (BFS)

**Use Cases**:
- Shortest path in unweighted graphs
- Connected component finding
- Level-order traversal

**Example** (2017 Day 12):
```java
Queue<Integer> queue = new ArrayDeque<>();
Set<Integer> visited = new HashSet<>();
queue.offer(start);
visited.add(start);

while (!queue.isEmpty()) {
    int current = queue.poll();
    for (int neighbor : graph.get(current)) {
        if (!visited.contains(neighbor)) {
            visited.add(neighbor);
            queue.offer(neighbor);
        }
    }
}
```

### Depth-First Search (DFS)

**Use Cases**:
- Path finding
- Cycle detection
- Topological sorting

### Dijkstra's Algorithm

**Use Cases**:
- Shortest path in weighted graphs
- Single-source shortest paths

**Example** (2024 Day 20):
```java
PriorityQueue<int[]> unvisited = new PriorityQueue<>(
    Comparator.comparingLong(a -> distances[a[0]][a[1]])
);
int[][] distances = new int[grid.maxY()][grid.maxX()];
// Initialize distances to Integer.MAX_VALUE
// Dijkstra's algorithm implementation
```

### Path Counting with Memoization

**Use Cases**:
- Counting paths in DAGs
- Dynamic programming on graphs

**Example** (2025 Day 11):
```java
private Long countPaths(
    final GraphNode current,
    final GraphNode target,
    final Map<GraphNode, List<GraphNode>> graph,
    final Map<GraphNode, Long> memo) {
    
    if (current.equals(target)) {
        return 1L;
    }
    
    return Optional.ofNullable(memo.get(current))
        .orElseGet(() -> {
            final long count = Optional.ofNullable(graph.get(current))
                .map(neighbors -> neighbors.stream()
                    .mapToLong(neighbor -> countPaths(neighbor, target, graph, memo))
                    .sum())
                .orElse(0L);
            memo.put(current, count);
            return count;
        });
}
```

### Clique Finding

**Use Cases**:
- Finding triangles (3-cliques)
- Finding maximal cliques (Bron-Kerbosch algorithm)

**Example** (2024 Day 23):
```java
// Finding triangles
Set<Set<String>> triangles = new HashSet<>();
for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
    String node = entry.getKey();
    Set<String> neighbors = entry.getValue();
    for (List<String> pair : Sets.cartesianProduct(neighbors, neighbors)) {
        String neighbor1 = pair.get(0);
        String neighbor2 = pair.get(1);
        if (!neighbor1.equals(neighbor2) && graph.get(neighbor1).contains(neighbor2)) {
            Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor1, neighbor2));
            triangles.add(triangle);
        }
    }
}
```

---

## Graph Representation Comparison

| Representation | Space | Add Edge | Check Edge | Iterate Neighbors |
|----------------|-------|----------|------------|-------------------|
| Adjacency List (List) | O(V + E) | O(1) | O(degree(v)) | O(degree(v)) |
| Adjacency List (Set) | O(V + E) | O(1) | O(1) | O(degree(v)) |
| Adjacency Matrix | O(V²) | O(1) | O(1) | O(V) |

**Choice**: Adjacency list is preferred for sparse graphs (most Advent of Code problems), while adjacency matrix is better for dense graphs.

---

## Common Patterns

### Building a Graph from Input

```java
Map<Node, List<Node>> graph = new HashMap<>();
for (String line : input) {
    String[] parts = line.split("-");
    Node from = Node.from(parts[0]);
    Node to = Node.from(parts[1]);
    
    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    // For undirected: also add reverse edge
    graph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
}
```

### Iterating Over All Edges

```java
for (Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
    Node from = entry.getKey();
    for (Node to : entry.getValue()) {
        // Process edge (from, to)
    }
}
```

### Checking if Edge Exists

```java
// For List-based adjacency list
boolean hasEdge = graph.getOrDefault(from, Collections.emptyList()).contains(to);

// For Set-based adjacency list
boolean hasEdge = graph.getOrDefault(from, Collections.emptySet()).contains(to);
```

---

## Summary

Graphs are represented primarily using:
- **Adjacency Lists**: `Map<Node, List<Node>>` or `Map<Node, Set<Node>>`
- **Custom Node/Edge Classes**: Records for type safety and value semantics
- **Algorithms**: BFS, DFS, Dijkstra's, path counting, clique finding

**When to Use**:
- Modeling relationships and connections
- Pathfinding problems
- Network analysis
- Dependency resolution
- Any problem with "connected" or "related" concepts

---

## References

- [Graph Theory - Wikipedia](https://en.wikipedia.org/wiki/Graph_theory)
- [Back to Main Documentation](../data-structures/README.md)

