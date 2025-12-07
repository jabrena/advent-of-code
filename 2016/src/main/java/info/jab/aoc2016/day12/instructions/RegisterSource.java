package info.jab.aoc2016.day12.instructions;

/**
 * Source that represents a register value
 */
public record RegisterSource(int register) implements Source {
    @Override
    public int getValue(int[] registers) {
        return registers[register];
    }
}

