package info.jab.aoc2025.day11;

import java.util.List;

/**
 * Represents an edge in the graph from a source node to a list of destination nodes.
 * Immutable record following functional programming principles.
 */
public record GraphEdge(GraphNode from, List<GraphNode> to) { }
