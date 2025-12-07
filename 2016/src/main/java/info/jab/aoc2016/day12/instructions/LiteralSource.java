package info.jab.aoc2016.day12.instructions;

/**
 * Source that represents a literal integer value
 */
public record LiteralSource(int value) implements Source {
    @Override
    public int getValue(int[] registers) {
        return value;
    }
}

