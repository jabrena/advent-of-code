package info.jab.aoc2015.day9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class RouteOptimizer implements Solver<Integer> {

    private record RouteInfo(String from, String to, int distance) {
        public static RouteInfo parse(String line) {
            if (line == null || line.trim().isEmpty()) {
                throw new IllegalArgumentException("Line cannot be null or empty");
            }

            String[] parts = line.split(" to | = ");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid line format: " + line);
            }

            String from = parts[0].trim();
            String to = parts[1].trim();
            int distance;
            
            try {
                distance = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException _) {
                throw new IllegalArgumentException("Invalid distance in line: " + line);
            }

            return new RouteInfo(from, to, distance);
        }
    }

    private void addDistance(Map<String, Map<String, Integer>> distances, String from, String to, int distance) {
        distances.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
        distances.computeIfAbsent(to, k -> new HashMap<>()).put(from, distance);
    }

    private void parseLine(Map<String, Map<String, Integer>> distances, String line) {
        RouteInfo routeInfo = RouteInfo.parse(line);
        addDistance(distances, routeInfo.from(), routeInfo.to(), routeInfo.distance());
    }

    private int findOptimalRoute(
        Map<String, Map<String, Integer>> distances,
        String startCity,
        Set<String> visited,
        int currentDistance,
        boolean findMin) {
        
        if (visited.size() == distances.size()) {
            return currentDistance;
        }
        
        int optimalDistance = findMin ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        
        for (Map.Entry<String, Integer> entry : distances.get(startCity).entrySet()) {
            String nextCity = entry.getKey();
            if (!visited.contains(nextCity)) {
                Set<String> newVisited = new HashSet<>(visited);
                newVisited.add(nextCity);
                int newDistance = currentDistance + entry.getValue();
                
                int result = findOptimalRoute(distances, nextCity, newVisited, newDistance, findMin);
                
                if (findMin) {
                    optimalDistance = Math.min(optimalDistance, result);
                } else {
                    optimalDistance = Math.max(optimalDistance, result);
                }
            }
        }
        
        return optimalDistance;
    }

    private Pair<String, Integer> findOptimalRoute(Map<String, Map<String, Integer>> distances, boolean findMin) {
        // Fix first city to reduce permutations from n! to (n-1)!
        // Since it's a round trip, we can start from any city
        List<String> cities = new ArrayList<>(distances.keySet());
        if (cities.isEmpty()) {
            throw new IllegalStateException("No cities registered");
        }
        
        int optimalDistance = findMin ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        String optimalRoute = "";
        
        // Try each city as starting point
        for (String startCity : cities) {
            Set<String> visited = new HashSet<>();
            visited.add(startCity);
            
            int distance = findOptimalRoute(distances, startCity, visited, 0, findMin);
            
            if (findMin && distance < optimalDistance) {
                optimalDistance = distance;
                optimalRoute = startCity;
            } else if (!findMin && distance > optimalDistance) {
                optimalDistance = distance;
                optimalRoute = startCity;
            }
        }
        
        return Pair.of(optimalRoute, optimalDistance);
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var distances = new HashMap<String, Map<String, Integer>>();
        var lines = ResourceLines.list(fileName);
        lines.forEach(line -> parseLine(distances, line));
        
        Pair<String, Integer> shortestRoute = findOptimalRoute(distances, true);
        return shortestRoute.getRight();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var distances = new HashMap<String, Map<String, Integer>>();
        var lines = ResourceLines.list(fileName);
        lines.forEach(line -> parseLine(distances, line));
        
        Pair<String, Integer> longestRoute = findOptimalRoute(distances, false);
        return longestRoute.getRight();
    }
} 