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
     * @param fileName the name of the input file
     * @return a map of graph nodes to their neighbors
     */
    private Map<GraphNode, List<GraphNode>> parseInput(final String fileName) {
            return ResourceLines.list(fileName).stream()
                            .filter(line -> !line.isBlank())
                            .map(this::parseLine)
                            .collect(Collectors.toUnmodifiableMap(
                                            GraphEdge::from,
                                            GraphEdge::to,
                                            (existing, replacement) -> existing));
    }

    /**
     * Parses a line of input into a graph edge.
     * @param line the line of input to parse
     * @return a graph edge
     */
    private GraphEdge parseLine(final String line) {
            final String[] parts = line.split(": ", 2);
            if (parts.length == 0 || parts[0].trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid line format: missing source node");
            }
            final GraphNode from = GraphNode.from(parts[0]);
            final List<GraphNode> toList = Optional.of(parts)
                            .filter(p -> p.length > 1 && !p[1].trim().isEmpty())
                            .map(p -> Stream.of(p[1].trim().split("\\s+"))
                                            .filter(node -> !node.isEmpty())
                                            .map(GraphNode::from)
                                            .toList())
                            .orElse(List.of());
            return new GraphEdge(from, toList);
    }

    /**
     * Counts the number of paths from the current node to the target node.
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

        return Optional.ofNullable(memo.get(current))
                .orElseGet(() -> {
                        final long count = Optional.ofNullable(graph.get(current))
                                .map(neighbors -> neighbors.stream()
                                        .mapToLong(neighbor -> countPaths(neighbor, target, graph, memo))
                                        .sum())
                                .orElse(0L);
                        memo.put(current, count);
                        return count;
                });
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final Map<GraphNode, List<GraphNode>> graph = parseInput(fileName);

        final long firstProduct = List.of(
                        new PathPair(GraphNode.from("svr"), GraphNode.from("dac")),
                        new PathPair(GraphNode.from("dac"), GraphNode.from("fft")),
                        new PathPair(GraphNode.from("fft"), GraphNode.from("out")))
                .stream()
                .mapToLong(pair -> countPaths(pair.from(), pair.to(), graph, new HashMap<>()))
                .reduce(1L, (a, b) -> a * b);

        final long secondProduct = List.of(
                        new PathPair(GraphNode.from("svr"), GraphNode.from("fft")),
                        new PathPair(GraphNode.from("fft"), GraphNode.from("dac")),
                        new PathPair(GraphNode.from("dac"), GraphNode.from("out")))
                .stream()
                .mapToLong(pair -> countPaths(pair.from(), pair.to(), graph, new HashMap<>()))
                .reduce(1L, (a, b) -> a * b);

        return firstProduct + secondProduct;
    }
}
