package info.jab.aoc2025.day11;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for counting paths in a graph.
 */
public final class GraphPathCounter implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        final Map<GraphNode, List<GraphNode>> graph = parseInput(fileName);
        return countPaths(GraphNode.from("you"), GraphNode.from("out"), graph, new HashMap<>());
    }

    /**
     * Parses the input file into a map of graph nodes to their neighbors.
     * Optimized: Uses imperative loops instead of streams to reduce overhead.
     * @param fileName the name of the input file
     * @return a map of graph nodes to their neighbors
     */
    private Map<GraphNode, List<GraphNode>> parseInput(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final Map<GraphNode, List<GraphNode>> graph = new HashMap<>();

        for (final String line : lines) {
            if (line.isBlank()) continue;

            final GraphEdge edge = parseLine(line);
            graph.put(edge.from(), edge.to());
        }

        return graph;
    }

    /**
     * Parses a line of input into a graph edge.
     * Optimized: Uses indexOf() and manual parsing instead of split() and Optional to reduce allocations.
     * @param line the line of input to parse
     * @return a graph edge
     */
    private GraphEdge parseLine(final String line) {
        final int colonIdx = line.indexOf(": ");
        if (colonIdx == -1) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }

        final GraphNode from = GraphNode.from(line.substring(0, colonIdx).trim());

        final String toPart = line.substring(colonIdx + 2).trim();
        if (toPart.isEmpty()) {
            return new GraphEdge(from, List.of());
        }

        // Parse neighbors manually instead of split + stream
        final List<GraphNode> toList = new ArrayList<>();
        int start = 0;
        for (int i = 0; i <= toPart.length(); i++) {
            if (i == toPart.length() || Character.isWhitespace(toPart.charAt(i))) {
                if (i > start) {
                    toList.add(GraphNode.from(toPart.substring(start, i)));
                }
                start = i + 1;
            }
        }

        return new GraphEdge(from, toList);
    }

    /**
     * Counts the number of paths from the current node to the target node.
     * Optimized: Eliminates Optional overhead by using direct null checks and imperative loops.
     * @param current the current node
     * @param target the target node
     * @param graph the graph
     * @param memo the memoization map
     * @return the number of paths from the current node to the target node
     */
    private Long countPaths(
                    final GraphNode current,
                    final GraphNode target,
                    final Map<GraphNode, List<GraphNode>> graph,
                    final Map<GraphNode, Long> memo) {
            if (current.equals(target)) {
                    return 1L;
            }

        // Direct memo lookup instead of Optional
        final Long cached = memo.get(current);
        if (cached != null) {
            return cached;
        }

        final List<GraphNode> neighbors = graph.get(current);
        if (neighbors == null || neighbors.isEmpty()) {
            memo.put(current, 0L);
            return 0L;
        }

        long count = 0;
        for (final GraphNode neighbor : neighbors) {
            count += countPaths(neighbor, target, graph, memo);
        }

        memo.put(current, count);
        return count;
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final Map<GraphNode, List<GraphNode>> graph = parseInput(fileName);

        // Each path calculation needs its own memo because memoization is keyed only by current node,
        // not by (current, target) pair. Reusing memo would cause incorrect cached values.
        final long firstProduct =
                countPaths(GraphNode.from("svr"), GraphNode.from("dac"), graph, new HashMap<>()) *
                countPaths(GraphNode.from("dac"), GraphNode.from("fft"), graph, new HashMap<>()) *
                countPaths(GraphNode.from("fft"), GraphNode.from("out"), graph, new HashMap<>());

        final long secondProduct =
                countPaths(GraphNode.from("svr"), GraphNode.from("fft"), graph, new HashMap<>()) *
                countPaths(GraphNode.from("fft"), GraphNode.from("dac"), graph, new HashMap<>()) *
                countPaths(GraphNode.from("dac"), GraphNode.from("out"), graph, new HashMap<>());

        return firstProduct + secondProduct;
    }
}
