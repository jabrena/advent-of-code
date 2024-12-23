package info.jab.aoc.day23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

import com.google.common.collect.Sets;

public class LanParty {

    private Map<String, Set<String>> getInputData(String fileName) {
        var list = ResourceLines.list(fileName);

        // Parse the connections into a graph
        Map<String, Set<String>> graph = new HashMap<>();
        for (String connection : list) {
            String[] parts = connection.split("-");
            String a = parts[0];
            String b = parts[1];

            graph.putIfAbsent(a, new HashSet<>());
            graph.putIfAbsent(b, new HashSet<>());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }
        return graph;
    }

    private Map<String, Set<String>> getInputData2(String fileName) {
        return ResourceLines.list(fileName).stream()
                .map(line -> line.split("-"))
                .collect(
                        HashMap::new,
                        (map, parts) -> {
                            map.computeIfAbsent(parts[0], k -> new HashSet<>()).add(parts[1]);
                            map.computeIfAbsent(parts[1], k -> new HashSet<>()).add(parts[0]);
                        },
                        (map1, map2) -> map2.forEach((key, value) -> map1.merge(key, value, (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }))
                );
    }

    //Imperative
    private Set<Set<String>> findTriangles(Map<String, Set<String>> graph) {
        Set<Set<String>> triangles = new HashSet<>();
        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                for (String mutualNeighbor : graph.get(neighbor)) {
                    if (graph.get(node).contains(mutualNeighbor) && !node.equals(mutualNeighbor)) {
                        // Add the triangle as a sorted set to avoid duplicates
                        Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor, mutualNeighbor));
                        triangles.add(triangle);
                    }
                }
            }
        }
        return triangles;
    }

    // Using Java Streams API, Awful, not easy to follow
    // Method to find all triangles in the graph
    private Set<Set<String>> findTriangles2(Map<String, Set<String>> graph) {
        return graph.entrySet().stream()
                .flatMap(entry -> {
                    String node = entry.getKey();
                    return entry.getValue().stream()
                            .flatMap(neighbor -> graph.get(neighbor).stream()
                                    .filter(mutualNeighbor -> graph.get(node).contains(mutualNeighbor) && !node.equals(mutualNeighbor))
                                    .map(mutualNeighbor -> new TreeSet<>(Arrays.asList(node, neighbor, mutualNeighbor))));
                })
                .collect(Collectors.toSet());
    }

    //Using Guava & functional style
    private Set<Set<String>> findTriangles3(Map<String, Set<String>> graph) {
        return graph.entrySet().stream()
                .flatMap(entry -> {
                    String node = entry.getKey();
                    Set<String> neighbors = entry.getValue();

                    return Sets.cartesianProduct(neighbors, neighbors).stream()
                            .filter(pair -> !pair.get(0).equals(pair.get(1))) // Exclude self-loops
                            .filter(pair -> graph.get(pair.get(0)).contains(pair.get(1))) // Ensure mutual connection
                            .map(pair -> new TreeSet<>(Arrays.asList(node, pair.get(0), pair.get(1)))); // Form a triangle
                })
                .collect(Collectors.toSet());
    }

    //Using Guava & Imperative style
    private Set<Set<String>> findTriangles4(Map<String, Set<String>> graph) {
        Set<Set<String>> triangles = new HashSet<>();
        for (String node : graph.keySet()) {
            Set<String> neighbors = graph.get(node);
            for (List<String> pair : Sets.cartesianProduct(neighbors, neighbors)) {
                String neighbor1 = pair.get(0);
                String neighbor2 = pair.get(1);

                if (!neighbor1.equals(neighbor2) && graph.get(neighbor1).contains(neighbor2)) {
                    Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor1, neighbor2));
                    triangles.add(triangle);
                }
            }
        }
        return triangles;
    }

    public String solvePartOne(String fileName) {

        //Load data
        Map<String, Set<String>> graph = getInputData2(fileName);

        // Find all triangles
        Set<Set<String>> triangles = findTriangles4(graph);

        // Filter triangles where at least one name starts with 't'
        return "" + triangles.stream()
            .filter(triangle -> triangle.stream().anyMatch(name -> name.startsWith("t")))
            .count();
    }

    public String solvePartTwo(String fileName) {

        //Load data
        Map<String, Set<String>> graph = getInputData2(fileName);

        // Find all maximal cliques using Bron-Kerbosch algorithm
        Set<String> largestClique = new HashSet<>();
        findCliquesIterative(new HashSet<>(), new HashSet<>(graph.keySet()), new HashSet<>(), graph, largestClique);
        //findCliquesRecursive(new HashSet<>(), new HashSet<>(graph.keySet()), new HashSet<>(), graph, largestClique);

        // Sort the largest clique and create the password
        return largestClique.stream()
            .sorted()
            .reduce((a, b) -> a + "," + b)
            .orElseThrow();
    }

    /**
     * Returns a new set containing all elements of the given set and the specified element.
     *
     * @param set The original set.
     * @param element The element to add to the set.
     * @return A new set containing all elements of the original set and the specified element.
     */
    private Set<String> union(Set<String> set, String element) {
        Set<String> result = new HashSet<>(set);
        result.add(element);
        return result;
    }

    /**
     * Returns the intersection of two sets.
     *
     * @param set1 The first set.
     * @param set2 The second set.
     * @return A new set containing only the elements that are present in both sets.
     */
    private Set<String> intersection(Set<String> set1, Set<String> set2) {
        Set<String> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    /**
     * Helper class to store the state of the Bron-Kerbosch algorithm (used for finding maximal cliques).
     */
    private static record State(Set<String> r, Set<String> p, Set<String> x) { }

    /**
     * Finds all maximal cliques in the graph using an iterative version of the Bron-Kerbosch algorithm.
     * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
     *
     * @param r The current clique being built.
     * @param p The set of potential candidates to extend the clique.
     * @param x The set of nodes that have already been excluded.
     * @param graph The graph represented as a map of nodes and their neighbors.
     * @param largestClique The set to store the largest clique found.
     */
    private void findCliquesIterative(
        Set<String> r,
        Set<String> p,
        Set<String> x,
        Map<String, Set<String>> graph,
        Set<String> largestClique) {

        Stack<State> stack = new Stack<>();
        stack.push(new State(new HashSet<>(r), new HashSet<>(p), new HashSet<>(x)));

        while (!stack.isEmpty()) {
            State currentState = stack.pop();
            Set<String> currentR = currentState.r;
            Set<String> currentP = currentState.p;
            Set<String> currentX = currentState.x;

            if (currentP.isEmpty() && currentX.isEmpty()) {
                if (currentR.size() > largestClique.size()) {
                    largestClique.clear();
                    largestClique.addAll(currentR);
                }
                continue;
            }

            Iterator<String> iterator = new HashSet<>(currentP).iterator();
            while (iterator.hasNext()) {
                String node = iterator.next();
                Set<String> neighbors = graph.get(node);
                stack.push(new State(
                        union(currentR, node),
                        intersection(currentP, neighbors),
                        intersection(currentX, neighbors)
                ));
                currentP.remove(node);
                currentX.add(node);
            }
        }
    }

    /**
     * Finds all maximal cliques in the graph using the recursive Bron-Kerbosch algorithm.
     * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
     *
     * @param r The current clique being built.
     * @param p The set of potential candidates to extend the clique.
     * @param x The set of nodes that have already been excluded.
     * @param graph The graph represented as a map of nodes and their neighbors.
     * @param largestClique The set to store the largest clique found.
     */
    private void findCliquesRecursive(
        Set<String> r,
        Set<String> p,
        Set<String> x,
        Map<String, Set<String>> graph,
        Set<String> largestClique) {

        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(r);
            }
            return;
        }

        Set<String> pCopy = new HashSet<>(p);
        for (String node : pCopy) {
            Set<String> neighbors = graph.get(node);
            findCliquesRecursive(
                    union(r, node),
                    intersection(p, neighbors),
                    intersection(x, neighbors),
                    graph,
                    largestClique
            );
            p.remove(node);
            x.add(node);
        }
    }
}
