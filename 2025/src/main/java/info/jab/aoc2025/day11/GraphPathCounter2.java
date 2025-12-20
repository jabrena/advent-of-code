package info.jab.aoc2025.day11;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

/**
 * Solver for counting paths in a graph using DataFrame-EC approach.
 * Uses DataFrame to store graph edges and process path pairs.
 *
 * This implementation uses DataFrame as the primary data abstraction:
 * - Single DataFrame for edges (from/to columns)
 * - Single DataFrame for path pairs (from/to columns)
 * - Single-pass processing with DataFrame collect
 * - Direct aggregation without intermediate collections
 * - Inline processing during DataFrame iteration
 */
public final class GraphPathCounter2 implements Solver<Long> {

    /**
     * Counts paths from "you" to "out" using DataFrame-EC approach.
     * Uses DataFrame for graph edge storage and neighbor lookups.
     *
     * @param fileName The input file name
     * @return The number of paths from "you" to "out"
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final DataFrame edgesDf = createEdgesDataFrame(fileName);
        return countPaths(GraphNode.from("you"), GraphNode.from("out"), edgesDf, new HashMap<>());
    }

    /**
     * Creates a DataFrame containing graph edges (from/to columns) from input file.
     * Flattens one-to-many relationships: one row per edge.
     *
     * @param fileName The input file name
     * @return A DataFrame with edge columns
     */
    private DataFrame createEdgesDataFrame(final String fileName) {
        final MutableList<String> fromNodes = Lists.mutable.empty();
        final MutableList<String> toNodes = Lists.mutable.empty();

        ResourceLines.list(fileName).forEach(line -> {
            if (!line.isBlank()) {
                final String[] parts = line.split(": ", 2);
                if (parts.length == 0 || parts[0].trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid line format: missing source node");
                }
                final String from = parts[0].trim();

                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    Stream.of(parts[1].trim().split("\\s+"))
                            .filter(node -> !node.isEmpty())
                            .forEach(to -> {
                                fromNodes.add(from);
                                toNodes.add(to);
                            });
                }
            }
        });

        return new DataFrame("Edges")
                .addStringColumn("from", fromNodes)
                .addStringColumn("to", toNodes);
    }

    /**
     * Finds neighbors of a node using DataFrame filtering.
     * Pure function used during DataFrame processing.
     *
     * @param edgesDf The DataFrame containing edges
     * @param node The node to find neighbors for
     * @return List of neighbor nodes
     */
    private List<GraphNode> findNeighbors(final DataFrame edgesDf, final GraphNode node) {
        final List<GraphNode> neighbors = new ArrayList<>();
        edgesDf.forEach((cursor) -> {
            if (cursor.getString("from").equals(node.value())) {
                neighbors.add(GraphNode.from(cursor.getString("to")));
            }
        });
        return neighbors;
    }

    /**
     * Counts the number of paths from the current node to the target node.
     * Uses DataFrame for neighbor lookups instead of Map.
     * Memoization preserved for algorithm efficiency.
     *
     * @param current The current node
     * @param target The target node
     * @param edgesDf The DataFrame containing edges
     * @param memo The memoization map
     * @return The number of paths from the current node to the target node
     */
    private Long countPaths(
                    final GraphNode current,
                    final GraphNode target,
                    final DataFrame edgesDf,
                    final Map<GraphNode, Long> memo) {
            if (current.equals(target)) {
                    return 1L;
            }

        return Optional.ofNullable(memo.get(current))
                .orElseGet(() -> {
                        final List<GraphNode> neighbors = findNeighbors(edgesDf, current);
                        long count = 0L;
                        for (final GraphNode neighbor : neighbors) {
                            count += countPaths(neighbor, target, edgesDf, memo);
                        }
                        memo.put(current, count);
                        return count;
                });
    }

    /**
     * Calculates sum of products for two path sequences using DataFrame-EC approach.
     * Uses DataFrame for path pair processing with direct aggregation.
     *
     * @param fileName The input file name
     * @return The sum of products for the two path sequences
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final DataFrame edgesDf = createEdgesDataFrame(fileName);

        // First path sequence: svr -> dac -> fft -> out
        final DataFrame firstPathPairsDf = createPathPairsDataFrame(
                List.of("svr", "dac", "fft", "out")
        );

        // Second path sequence: svr -> fft -> dac -> out
        final DataFrame secondPathPairsDf = createPathPairsDataFrame(
                List.of("svr", "fft", "dac", "out")
        );

        // Calculate first product using DataFrame collect
        final long[] firstProductHolder = {1L};
        firstPathPairsDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final GraphNode from = GraphNode.from(cursor.getString("from"));
                    final GraphNode to = GraphNode.from(cursor.getString("to"));
                    final long pathCount = countPaths(from, to, edgesDf, new HashMap<>());
                    firstProductHolder[0] *= pathCount;
                }
        );

        // Calculate second product using DataFrame collect
        final long[] secondProductHolder = {1L};
        secondPathPairsDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final GraphNode from = GraphNode.from(cursor.getString("from"));
                    final GraphNode to = GraphNode.from(cursor.getString("to"));
                    final long pathCount = countPaths(from, to, edgesDf, new HashMap<>());
                    secondProductHolder[0] *= pathCount;
                }
        );

        return firstProductHolder[0] + secondProductHolder[0];
    }

    /**
     * Creates a DataFrame containing path pairs from a sequence of nodes.
     * Converts sequential pairs: [a, b, c] -> [(a,b), (b,c)]
     *
     * @param nodeSequence The sequence of nodes
     * @return A DataFrame with path pair columns
     */
    private DataFrame createPathPairsDataFrame(final List<String> nodeSequence) {
        final MutableList<String> fromNodes = Lists.mutable.empty();
        final MutableList<String> toNodes = Lists.mutable.empty();

        for (int i = 0; i < nodeSequence.size() - 1; i++) {
            fromNodes.add(nodeSequence.get(i));
            toNodes.add(nodeSequence.get(i + 1));
        }

        return new DataFrame("PathPairs")
                .addStringColumn("from", fromNodes)
                .addStringColumn("to", toNodes);
    }
}
