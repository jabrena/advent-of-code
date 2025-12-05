package info.jab.aoc2016.day23;

import java.util.List;
import java.util.Map;

/**
 * Immutable program state record
 * Represents the state of an Assembunny program execution
 */
public record ProgramState(
        List<String> instructions,
        Map<String, Integer> registers,
        int programCounter
) {
    /**
     * Create new state with updated registers
     */
    public ProgramState withRegisters(Map<String, Integer> newRegisters) {
        return new ProgramState(instructions, newRegisters, programCounter);
    }

    /**
     * Create new state with updated program counter
     */
    public ProgramState withProgramCounter(int newPc) {
        return new ProgramState(instructions, registers, newPc);
    }
}
