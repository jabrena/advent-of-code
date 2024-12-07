package info.jab.aoc.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/5
 *
 * TODO Pending to refactor & simplify
 **/
public class Day5 implements Day<Integer> {

    //https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/collect/HashMultimap.html
    private HashMultimap<Integer, Integer> ordering = HashMultimap.create();

    private List<List<Integer>> prepareData(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<List<Integer>> pages = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                int first = Integer.parseInt(parts[0]);
                int second = Integer.parseInt(parts[1]);
                ordering.put(first, second);
            } else if (line.contains(",")) {
                pages.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
            }
        }
        return pages;
    }

    private int compare(Integer first, Integer second) {
        return ordering.get(first).contains(second) ? -1 : 1;
    }

    private Integer sum(List<List<Integer>> pages, Boolean part1) {
        Integer sum1 = 0;
        Integer sum2 = 0;
        for (List<Integer> pageSet : pages) {
            List<Integer> ordered = new ArrayList<>(pageSet);
            Collections.sort(ordered, this::compare);
            if (ordered.equals(pageSet)) {
                sum1 += pageSet.get(pageSet.size()/2);
            } else {
                sum2 += ordered.get(ordered.size()/2);
            }
        }

        if (part1) {
            return sum1;
        }
        return sum2;
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
