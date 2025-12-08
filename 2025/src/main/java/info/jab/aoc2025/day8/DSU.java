package info.jab.aoc2025.day8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public int getCount() {
        return count;
    }

    public List<Integer> getComponentSizes() {
        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < parent.length; i++) {
            roots.add(find(i));
        }

        return roots.stream()
            .map(root -> size[root])
            .collect(Collectors.toList());
    }
}

