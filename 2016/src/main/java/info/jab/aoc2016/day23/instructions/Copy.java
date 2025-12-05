package info.jab.aoc2016.day23.instructions;

import info.jab.aoc2016.day23.ExecutionResult;
import info.jab.aoc2016.day23.ProgramState;

import java.util.Map;

/**
 * Copy instruction: cpy x y
 * Copies value from source (x) to target register (y)
 * Pure transformation: creates new register map
 */
final class Copy implements Instruction {

    @Override
    public ExecutionResult execute(String[] parts, ProgramState state) {
        if (parts.length < 3) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String source = parts[1];
        String target = parts[2];

        // Skip if target is a number (invalid instruction)
        if (InstructionUtils.isNumber(target)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        int value = InstructionUtils.getValue(source, state.registers());
        Map<String, Integer> newRegisters = InstructionUtils.updateRegister(state.registers(), target, value);
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 1)
        );
    }
}

