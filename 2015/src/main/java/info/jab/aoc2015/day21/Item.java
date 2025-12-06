package info.jab.aoc2015.day21;

/**
 * Represents an RPG item (weapon, armor, or ring).
 * Immutable record following functional programming principles.
 */
public record Item(String name, int cost, int damage, int armor) {}
