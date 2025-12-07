package info.jab.aoc2016.day12.instructions;

/**
 * Sealed interface for Assembunny instructions
 * Each instruction type encapsulates its execution logic
 * Follows DOP principles: separates code from data
 */
public sealed interface Instruction permits Cpy, Inc, Dec, Jnz {
    /**
     * Execute this instruction with the given registers and program counter
     * @param registers the register array (a, b, c, d)
     * @param pc the current program counter
     * @return the new program counter after execution
     */
    int execute(int[] registers, int pc);
}

