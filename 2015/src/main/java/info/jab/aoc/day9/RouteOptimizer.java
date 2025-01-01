package info.jab.aoc.day9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;

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
            } catch (NumberFormatException e) {
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

    private record RouteResult(String route, int distance) {}

    private RouteResult findRouteFromCity(Map<String, Map<String, Integer>> distances, String startCity) {
        Set<String> visited = new HashSet<>();
        visited.add(startCity);
        return findRouteRecursive(distances, startCity, visited, 0, startCity);
    }

    private List<List<String>> generateAllPermutations(List<String> cities) {
        List<List<String>> result = new ArrayList<>();
        if (cities.isEmpty()) {
            return result;
        }
        
        // Initialize with the first permutation
        result.add(new ArrayList<>(List.of(cities.get(0))));
        
        // For each remaining city
        for (int i = 1; i < cities.size(); i++) {
            String currentCity = cities.get(i);
            List<List<String>> currentPermutations = new ArrayList<>();
            
            // For each existing permutation
            for (List<String> permutation : result) {
                // Insert the current city in each possible position
                for (int j = 0; j <= permutation.size(); j++) {
                    List<String> newPermutation = new ArrayList<>(permutation);
                    newPermutation.add(j, currentCity);
                    currentPermutations.add(newPermutation);
                }
            }
            result = currentPermutations;
        }
        return result;
    }

    private RouteResult calculateRouteDistance(List<String> route, Map<String, Map<String, Integer>> distances) {
        int totalDistance = 0;
        StringBuilder routePath = new StringBuilder(route.get(0));

        for (int i = 0; i < route.size() - 1; i++) {
            String currentCity = route.get(i);
            String nextCity = route.get(i + 1);
            totalDistance += distances.get(currentCity).get(nextCity);
            routePath.append(" -> ").append(nextCity);
        }

        return new RouteResult(routePath.toString(), totalDistance);
    }

    private RouteResult findRouteFromCity2(Map<String, Map<String, Integer>> distances, String startCity) {
        List<String> cities = new ArrayList<>(distances.keySet());
        cities.remove(startCity);
        cities.add(0, startCity);
        
        return generateAllPermutations(cities).stream()
            .map(route -> calculateRouteDistance(route, distances))
            .min(Comparator.comparing(RouteResult::distance))
            .orElseThrow(() -> new IllegalStateException("No valid route found"));
    }

    private Pair<String, Integer> findShortestRoute(Map<String, Map<String, Integer>> distances) {
        return distances.keySet().stream()
                .map(startCity -> findRouteFromCity2(distances, startCity))
                .min(Comparator.comparing(RouteResult::distance))
                .map(result -> Pair.of(result.route(), result.distance()))
                .orElseThrow(() -> new IllegalStateException("No cities registered"));
    }

    private RouteResult findRouteRecursive(
        Map<String, Map<String, Integer>> distances, 
        String currentCity, 
        Set<String> visited, 
        int currentDistance, String route) {
            
        if (visited.size() == distances.size()) {
            return new RouteResult(route, currentDistance);
        }

        return distances.get(currentCity).entrySet().stream()
                .filter(entry -> !visited.contains(entry.getKey()))
                .map(entry -> {
                    Set<String> newVisited = new HashSet<>(visited);
                    newVisited.add(entry.getKey());
                    return findRouteRecursive(
                        distances,
                        entry.getKey(),
                        newVisited,
                        currentDistance + entry.getValue(),
                        route + " -> " + entry.getKey()
                    );
                })
                .min(Comparator.comparing(RouteResult::distance))
                .orElse(new RouteResult(route, Integer.MAX_VALUE));
    }

    @Override
    public Integer solvePartOne(String fileName) {
        //TODO Avoid the mutation is more complex to follow that current approach
        var distances = new HashMap<String, Map<String, Integer>>();
        var lines = ResourceLines.list(fileName);
        lines.forEach(line -> parseLine(distances, line));
        
        Pair<String, Integer> shortestRoute = findShortestRoute(distances);
        return shortestRoute.getRight();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
} 