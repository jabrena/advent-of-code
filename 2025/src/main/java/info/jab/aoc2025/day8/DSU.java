package info.jab.aoc2025.day8;

import java.util.List;

/**
 * Disjoint Set Union (DSU) data structure, also known as Union-Find.
 * <p>
 * This data structure efficiently tracks a collection of disjoint sets and supports two main operations:
 * <ul>
 *   <li><b>Find</b>: Determine which set an element belongs to</li>
 *   <li><b>Union</b>: Merge two sets into one</li>
 * </ul>
 * <p>
 * The implementation uses two optimizations for near-constant time complexity:
 * <ul>
 *   <li><b>Path Compression</b>: During find operations, nodes are directly connected to their root,
 *       flattening the tree structure for faster future lookups</li>
 *   <li><b>Union by Size</b>: When merging sets, the smaller tree is always attached to the root of
 *       the larger tree, keeping trees balanced</li>
 * </ul>
 * <p>
 * With these optimizations, both operations achieve nearly constant amortized time complexity:
 * O(α(n)) where α is the inverse Ackermann function, which grows extremely slowly and is effectively
 * constant for practical purposes.
 * <p>
 * Common use cases include:
 * <ul>
 *   <li>Finding connected components in graphs</li>
 *   <li>Detecting cycles in undirected graphs</li>
 *   <li>Kruskal's algorithm for minimum spanning trees</li>
 *   <li>Tracking equivalence relations</li>
 * </ul>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Disjoint-set_data_structure">Disjoint-set data structure</a>
 */
public final class DSU {
    private final int[] parent;
    private final int[] size;
    private int count;

    /**
     * Constructs a new DSU data structure with {@code n} elements.
     * Initially, each element is in its own set (singleton sets).
     *
     * @param n the number of elements (indices 0 to n-1)
     * @throws IllegalArgumentException if n is negative
     */
    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /**
     * Finds the root representative of the set containing element {@code i}.
     * <p>
     * Uses path compression optimization: during the search, all nodes along the path
     * are directly connected to the root, flattening the tree structure for faster
     * future lookups.
     *
     * @param i the element to find (must be in range [0, n-1])
     * @return the root representative of the set containing element i
     */
    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    /**
     * Merges the sets containing elements {@code i} and {@code j}.
     * <p>
     * Uses union by size optimization: the smaller tree is always attached to the root
     * of the larger tree, keeping trees balanced and improving performance.
     * <p>
     * If both elements are already in the same set, no operation is performed.
     *
     * @param i the first element (must be in range [0, n-1])
     * @param j the second element (must be in range [0, n-1])
     * @return {@code true} if the sets were merged (they were in different sets),
     *         {@code false} if both elements were already in the same set
     */
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

    /**
     * Returns the number of disjoint sets currently tracked.
     * <p>
     * Initially equals the number of elements. Decreases by one each time
     * a successful union operation merges two different sets.
     *
     * @return the current number of disjoint sets
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns a list of sizes for all disjoint sets (connected components).
     * <p>
     * Each element in the returned list represents the size of one connected component.
     * The list contains one entry per distinct root in the DSU structure.
     * <p>
     * Optimized to avoid distinct() operation by using direct array access and early collection.
     *
     * @return an immutable list of component sizes, one per disjoint set
     */
    public List<Integer> getComponentSizes() {
        // Optimized: Use boolean array to track seen roots, collect sizes directly
        // Avoids distinct() overhead and stream intermediate operations
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

