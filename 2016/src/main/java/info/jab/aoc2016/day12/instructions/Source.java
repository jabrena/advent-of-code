package info.jab.aoc2016.day12.instructions;

/**
 * Represents a source value that can be either a register or a literal
 * Follows functional programming: immutable data structure
 */
public sealed interface Source permits RegisterSource, LiteralSource {
    int getValue(int[] registers);
}

