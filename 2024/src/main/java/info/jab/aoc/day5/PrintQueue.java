package info.jab.aoc.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class PrintQueue implements Solver<Integer> {

    private Map<Integer, Set<Integer>> buildOrdering(String fileName) {
        Map<Integer, Set<Integer>> ordering = new HashMap<>();
        var lines = ResourceLines.list(fileName);

        for (String line : lines) {
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                int first = Integer.parseInt(parts[0]);
                int second = Integer.parseInt(parts[1]);
                ordering.computeIfAbsent(first, k -> new HashSet<>()).add(second);
            }
        }
        return ordering;
    }

    private List<List<Integer>> preparePages(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<List<Integer>> pages = new ArrayList<>();

        for (String line : lines) {
            if (line.contains(",")) {
                pages.add(Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toList());
            }
        }
        return pages;
    }

    private int compare(Map<Integer, Set<Integer>> ordering, Integer first, Integer second) {
        // Check if second is in the set of first's dependencies
        return ordering.getOrDefault(first, Collections.emptySet()).contains(second) ? -1 : 1;
    }

    private Integer sum(List<List<Integer>> pages, Map<Integer, Set<Integer>> ordering, Boolean part1) {
        Integer sum1 = 0;
        Integer sum2 = 0;

        for (List<Integer> pageSet : pages) {
            List<Integer> ordered = new ArrayList<>(pageSet);
            Collections.sort(ordered, (a, b) -> compare(ordering, a, b));

            if (ordered.equals(pageSet)) {
                sum1 += pageSet.get(pageSet.size() / 2);
            } else {
                sum2 += ordered.get(ordered.size() / 2);
            }
        }

        return part1 ? sum1 : sum2;
    }

    @Override
    public Integer solvePartOne(String fileName) {
        Map<Integer, Set<Integer>> ordering = buildOrdering(fileName);
        List<List<Integer>> pages = preparePages(fileName);
        return sum(pages, ordering, true);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        Map<Integer, Set<Integer>> ordering = buildOrdering(fileName);
        List<List<Integer>> pages = preparePages(fileName);
        return sum(pages, ordering, false);
    }
}
