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

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/5
 *
 **/
public class Day5 implements Day<Integer> {

    private Map<Integer, Set<Integer>> ordering = new HashMap<>();

    private List<List<Integer>> prepareData(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<List<Integer>> pages = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                int first = Integer.parseInt(parts[0]);
                int second = Integer.parseInt(parts[1]);
                ordering.computeIfAbsent(first, k -> new HashSet<>()).add(second);
            } else if (line.contains(",")) {
                pages.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
            }
        }
        return pages;
    }

    private int compare(Integer first, Integer second) {
        // Check if second is in the set of first's dependencies
        return ordering.getOrDefault(first, Collections.emptySet()).contains(second) ? -1 : 1;
    }

    private Integer sum(List<List<Integer>> pages, Boolean part1) {
        Integer sum1 = 0;
        Integer sum2 = 0;
        for (List<Integer> pageSet : pages) {
            List<Integer> ordered = new ArrayList<>(pageSet);
            Collections.sort(ordered, this::compare);
            if (ordered.equals(pageSet)) {
                sum1 += pageSet.get(pageSet.size() / 2);
            } else {
                sum2 += ordered.get(ordered.size() / 2);
            }
        }

        return part1 ? sum1 : sum2;
    }

    @Override
    public Integer getPart1Result(String fileName) {
        List<List<Integer>> pages = prepareData(fileName);
        return sum(pages, true);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        List<List<Integer>> pages = prepareData(fileName);
        return sum(pages, false);
    }
}
