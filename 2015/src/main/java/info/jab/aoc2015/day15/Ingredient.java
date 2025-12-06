package info.jab.aoc2015.day15;

/**
 * Represents a cookie ingredient with its properties.
 * Immutable record following functional programming principles.
 */
public record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {}
