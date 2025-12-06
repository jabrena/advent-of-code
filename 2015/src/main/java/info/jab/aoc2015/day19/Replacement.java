package info.jab.aoc2015.day19;

/**
 * Represents a molecule replacement rule.
 * Immutable record following functional programming principles.
 */
public record Replacement(String from, String to) {}
