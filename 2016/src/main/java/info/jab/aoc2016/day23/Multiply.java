package info.jab.aoc2016.day23;

import java.util.Map;

/**
 * Multiply instruction: mul x y z
 * Multiplies x and y, stores result in z
 * Optimization instruction that replaces a multiplication loop
 * Pure transformation: creates new register map
 */
final class Multiply implements Instruction {

    @Override
    public ExecutionResult execute(String[] parts, ProgramState state) {
        if (parts.length < 4) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String source1 = parts[1]; // d
        String source2 = parts[2]; // b
        String target = parts[3];  // a

        if (InstructionUtils.isNumber(target)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 5));
        }

        int value1 = InstructionUtils.getValue(source1, state.registers());
        int value2 = InstructionUtils.getValue(source2, state.registers());

        // Optimization logic: a = b * d, and side effects: c = 0, d = 0
        Map<String, Integer> newRegisters = InstructionUtils.updateRegister(state.registers(), target, value1 * value2);
        newRegisters = InstructionUtils.updateRegister(newRegisters, "c", 0);
        newRegisters = InstructionUtils.updateRegister(newRegisters, "d", 0);

        // Skip 5 instructions to resume at 'dec b' (index 10)
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 5)
        );
    }
}

