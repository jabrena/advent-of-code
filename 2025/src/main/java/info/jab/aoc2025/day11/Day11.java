package info.jab.aoc2025.day11;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        var graph = parseInput(fileName);
        return countPaths("you", "out", graph, new HashMap<>());
    }

    @Override
    public Long getPart2Result(String fileName) {
        var graph = parseInput(fileName);
        
        long svrToDac = countPaths("svr", "dac", graph, new HashMap<>());
        long dacToFft = countPaths("dac", "fft", graph, new HashMap<>());
        long fftToOut = countPaths("fft", "out", graph, new HashMap<>());
        
        long svrToFft = countPaths("svr", "fft", graph, new HashMap<>());
        long fftToDac = countPaths("fft", "dac", graph, new HashMap<>());
        long dacToOut = countPaths("dac", "out", graph, new HashMap<>());
        
        return (svrToDac * dacToFft * fftToOut) + (svrToFft * fftToDac * dacToOut);
    }

    private Map<String, List<String>> parseInput(String fileName) {
        Map<String, List<String>> graph = new HashMap<>();
        List<String> lines = ResourceLines.list(fileName);
        for (String line : lines) {
            String[] parts = line.split(": ");
            String from = parts[0];
            List<String> toList;
            if (parts.length > 1) {
                String[] to = parts[1].split(" ");
                toList = Arrays.asList(to);
            } else {
                toList = List.of();
            }
            graph.put(from, toList);
        }
        return graph;
    }

    private Long countPaths(String current, String target, Map<String, List<String>> graph, Map<String, Long> memo) {
        if (current.equals(target)) {
            return 1L;
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long count = 0;
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                count += countPaths(neighbor, target, graph, memo);
            }
        }
        
        memo.put(current, count);
        return count;
    }
}
