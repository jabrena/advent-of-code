package info.jab.aoc2016.day12.instructions;

/**
 * Decrement instruction: dec x
 * Decrements the value in register x by 1
 */
public record Dec(int register) implements Instruction {
    @Override
    public int execute(int[] registers, int pc) {
        registers[register]--;
        return pc + 1;
    }
}

