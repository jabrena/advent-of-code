package info.jab.aoc2015.day19;

/**
 * Represents a molecule replacement rule that transforms one molecule pattern to another.
 * The 'from' pattern is replaced with the 'to' pattern.
 * Immutable record following functional programming principles.
 */
public record ReplacementRule(String from, String to) {}
