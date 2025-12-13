# Tree Data Structures

This document details tree data structures used in the Advent of Code solutions.

## Overview

Trees are hierarchical data structures used for representing file systems, organizational structures, and hierarchical relationships. In the Advent of Code solutions, trees are often represented using HashMap for efficient lookups rather than explicit tree nodes.

## File System Tree

**Description**: Tree structure represented using HashMap for efficient path lookups.

**Time Complexity**:
- Lookup: O(1) average case
- Insertion: O(1) average case
- Path traversal: O(depth)

**Space Complexity**: O(n) where n is the number of directories/files

### Implementation

**Usage Example**: **2022 Day 7** (`FileSystemRecreation.java`)

The file system is represented as a `Map<String, Long>` where:
- **Key**: Directory path (e.g., "/", "/a/", "/a/b/")
- **Value**: Total size of files in that directory (including subdirectories)

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

### How It Works

1. **Path Tracking**: Uses a `Stack<String>` to maintain the current directory path
2. **Directory Navigation**:
   - `$ cd ..`: Pop from stack (go up one level)
   - `$ cd <dir>`: Push new directory to stack
3. **File Processing**: When a file is encountered, add its size to all parent directories in the stack
4. **Result**: A map where each key is a directory path and value is the total size

### Example

Input:
```
$ cd /
$ ls
dir a
14848514 b.txt
$ cd a
$ ls
8504156 c.dat
```

Result:
```java
Map<String, Long> fsAsMap = {
    "/" -> 23352670,      // Total size of all files
    "/a/" -> 8504156      // Size of files in /a/
}
```

### Advantages

1. **Efficient Lookups**: O(1) average case for directory size lookups
2. **Simple Implementation**: No need for explicit tree node classes
3. **Easy Aggregation**: Can easily sum sizes across directories
4. **Path-based Access**: Direct access to any directory by path

### Use Cases

- File system navigation problems
- Directory size calculations
- Hierarchical data processing
- Path-based lookups

---

## Tree Traversal Patterns

### Depth-First Traversal

For explicit tree structures, depth-first traversal can be implemented:

```java
void dfs(TreeNode node, Map<String, Long> sizes) {
    long size = node.fileSize;
    for (TreeNode child : node.children) {
        dfs(child, sizes);
        size += sizes.get(child.path);
    }
    sizes.put(node.path, size);
}
```

### Breadth-First Traversal

For level-order processing:

```java
Queue<TreeNode> queue = new ArrayDeque<>();
queue.offer(root);

while (!queue.isEmpty()) {
    TreeNode current = queue.poll();
    // Process current node
    for (TreeNode child : current.children) {
        queue.offer(child);
    }
}
```

---

## Alternative Tree Representations

### Explicit Tree Nodes

While not used in the file system example, trees can be represented with explicit node classes:

```java
class TreeNode {
    String name;
    long size;
    List<TreeNode> children;
    TreeNode parent;
}
```

### Nested Maps

For more complex hierarchies:

```java
Map<String, Map<String, Object>> tree = new HashMap<>();
// tree.get("parent").get("child") accesses nested structure
```

---

## Tree vs. Graph

**Tree Characteristics**:
- Acyclic (no cycles)
- Connected (all nodes reachable)
- Exactly one path between any two nodes
- N-1 edges for N nodes

**When to Use Tree Representation**:
- Hierarchical relationships
- File systems
- Organizational structures
- Decision trees
- Expression trees

**When to Use Graph Representation**:
- Cycles possible
- Multiple paths between nodes
- General relationships

---

## Common Tree Problems in Advent of Code

1. **Directory Size Calculation**: Sum sizes of files in directories
2. **Path Finding**: Find paths from root to leaves
3. **Tree Traversal**: Process all nodes in order
4. **Size Filtering**: Find directories meeting certain criteria
5. **Hierarchical Aggregation**: Aggregate values up the tree

---

## Summary

Trees in Advent of Code solutions are primarily represented as:
- **HashMap-based**: Efficient path-based lookups
- **Stack-based Navigation**: For building paths during parsing
- **Implicit Structure**: Tree structure implied by paths, not explicit nodes

**Key Advantages**:
- O(1) average case lookups
- Simple implementation
- Easy aggregation
- Path-based access

**When to Use**:
- File system problems
- Hierarchical data
- Path-based lookups
- Directory size calculations

---

## References

- [Tree Data Structure - Wikipedia](https://en.wikipedia.org/wiki/Tree_(data_structure))
- [Back to Main Documentation](../data-structures/README.md)

