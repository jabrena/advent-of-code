package info.jab.aoc2025.day11;

/**
 * Represents a pair of nodes for path counting.
 * Immutable record following functional programming principles.
 */
public record PathPair(GraphNode from, GraphNode to) { }
