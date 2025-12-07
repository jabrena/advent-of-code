package info.jab.aoc2016.day12.instructions;

/**
 * Increment instruction: inc x
 * Increments the value in register x by 1
 */
public record Inc(int register) implements Instruction {
    @Override
    public int execute(int[] registers, int pc) {
        registers[register]++;
        return pc + 1;
    }
}

