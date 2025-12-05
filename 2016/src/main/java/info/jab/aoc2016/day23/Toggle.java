package info.jab.aoc2016.day23;

import java.util.Optional;

/**
 * Toggle instruction: tgl x
 * Toggles the instruction at offset x from current position
 * Mutates instructions list (required by interpreter semantics)
 */
final class Toggle implements Instruction {

    @Override
    public ExecutionResult execute(String[] parts, ProgramState state) {
        if (parts.length < 2) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String offsetStr = parts[1];
        int offset = InstructionUtils.getValue(offsetStr, state.registers());
        int targetIndex = state.programCounter() + offset;

        if (targetIndex >= 0 && targetIndex < state.instructions().size()) {
            String targetInstruction = state.instructions().get(targetIndex);
            Optional.ofNullable(targetInstruction)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .ifPresent(instruction -> {
                        String toggled = InstructionUtils.toggleInstruction(instruction);
                        state.instructions().set(targetIndex, toggled);
                    });
        }

        return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
    }
}

