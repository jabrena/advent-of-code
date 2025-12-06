package info.jab.aoc2015.day21;

/**
 * Represents an RPG character with hit points, damage, and armor.
 * Immutable record following functional programming principles.
 */
public record Character(int hitPoints, int damage, int armor) {}
