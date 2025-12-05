package info.jab.aoc2016.day23.instructions;

import info.jab.aoc2016.day23.ExecutionResult;
import info.jab.aoc2016.day23.ProgramState;

/**
 * Jump not zero instruction: jnz x y
 * If x is not zero, jump by offset y
 * Pure function: computes next program counter based on current state
 */
final class JumpNotZero implements Instruction {

    @Override
    public ExecutionResult execute(String[] parts, ProgramState state) {
        if (parts.length < 3) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String condition = parts[1];
        String offset = parts[2];
        int conditionValue = InstructionUtils.getValue(condition, state.registers());

        int nextPc = conditionValue != 0
                ? state.programCounter() + InstructionUtils.getValue(offset, state.registers())
                : state.programCounter() + 1;

        return ExecutionResult.continueWith(state.withProgramCounter(nextPc));
    }
}

