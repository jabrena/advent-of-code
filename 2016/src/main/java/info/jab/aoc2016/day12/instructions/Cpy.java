package info.jab.aoc2016.day12.instructions;

/**
 * Copy instruction: cpy x y
 * Copies value from source (x) to target register (y)
 * Source can be a register or a literal value
 */
public record Cpy(Source source, int targetRegister) implements Instruction {
    @Override
    public int execute(int[] registers, int pc) {
        int value = source.getValue(registers);
        registers[targetRegister] = value;
        return pc + 1;
    }
}

