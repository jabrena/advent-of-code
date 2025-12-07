package info.jab.aoc2016.day12.instructions;

/**
 * Jump if not zero instruction: jnz x y
 * Jumps by offset y if x is not zero
 * Source x can be a register or a literal value
 */
public record Jnz(Source source, int offset) implements Instruction {
    @Override
    public int execute(int[] registers, int pc) {
        int value = source.getValue(registers);
        return value != 0 ? pc + offset : pc + 1;
    }
}

