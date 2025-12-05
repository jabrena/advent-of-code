package info.jab.aoc2016.day23.instructions;

import info.jab.aoc2016.day23.ExecutionResult;
import info.jab.aoc2016.day23.ProgramState;

import java.util.Map;

/**
 * Increment instruction: inc x
 * Increments the value in register x by 1
 * Pure transformation: creates new register map
 */
final class Increment implements Instruction {

    @Override
    public ExecutionResult execute(String[] parts, ProgramState state) {
        if (parts.length < 2) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String register = parts[1];
        if (InstructionUtils.isNumber(register)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        int currentValue = state.registers().getOrDefault(register, 0);
        Map<String, Integer> newRegisters = InstructionUtils.updateRegister(state.registers(), register, currentValue + 1);
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 1)
        );
    }
}

