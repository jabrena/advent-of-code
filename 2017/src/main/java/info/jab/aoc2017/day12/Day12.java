package info.jab.aoc2017.day12;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day12 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
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
        
        return visited.size();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Map<Integer, List<Integer>> graph = buildGraph(fileName);
        Set<Integer> allNodes = new HashSet<>(graph.keySet());
        Set<Integer> visited = new HashSet<>();
        int groupCount = 0;
        
        for (int startNode : allNodes) {
            if (!visited.contains(startNode)) {
                // BFS to find all nodes in this group
                Queue<Integer> queue = new ArrayDeque<>();
                queue.offer(startNode);
                visited.add(startNode);
                
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
                
                groupCount++;
            }
        }
        
        return groupCount;
    }

    private Map<Integer, List<Integer>> buildGraph(String fileName) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        List<String> lines = ResourceLines.list(fileName);
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(" <-> ");
            int nodeId = Integer.parseInt(parts[0].trim());
            String[] connectedNodes = parts[1].split(", ");
            
            graph.putIfAbsent(nodeId, new ArrayList<>());
            for (String connected : connectedNodes) {
                String trimmed = connected.trim();
                if (!trimmed.isEmpty()) {
                    int connectedId = Integer.parseInt(trimmed);
                    graph.get(nodeId).add(connectedId);
                    // Since connections are bidirectional, add reverse connection
                    graph.putIfAbsent(connectedId, new ArrayList<>());
                    graph.get(connectedId).add(nodeId);
                }
            }
        }
        
        return graph;
    }
}
