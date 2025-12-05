package info.jab.aoc2016.day23.instructions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for instruction execution helpers
 * Contains pure functions used by instruction implementations
 * Follows FP principles: immutable transformations, no side effects
 */
final class InstructionUtils {

    private InstructionUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Get value from source (either a number or register)
     * Pure function: no side effects, deterministic
     */
    static int getValue(String source, Map<String, Integer> registers) {
        return parseInteger(source)
                .orElseGet(() -> registers.getOrDefault(source, 0));
    }

    /**
     * Check if string is a number
     * Pure function: no side effects
     */
    static boolean isNumber(String str) {
        return parseInteger(str).isPresent();
    }

    /**
     * Parse integer from string - returns Optional for null safety
     * Pure function: no side effects
     */
    static Optional<Integer> parseInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Update register value - creates new map (immutable transformation)
     * Pure function: returns new map instead of mutating
     */
    static Map<String, Integer> updateRegister(Map<String, Integer> registers, String register, int value) {
        Map<String, Integer> newRegisters = new HashMap<>(registers);
        newRegisters.put(register, value);
        return newRegisters;
    }

    /**
     * Toggle an instruction according to the rules
     * Pure function: deterministic transformation
     */
    static String toggleInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        Optional<Instruction> inst = Instruction.fromString(parts[0]);

        return switch (parts.length) {
            case 2 -> inst.map(i -> switch (i) {
                        case Increment _ -> "dec " + parts[1];
                        default -> "inc " + parts[1];
                    })
                    .orElse("inc " + parts[1]);
            case 3 -> inst.map(i -> switch (i) {
                        case JumpNotZero _ -> "cpy " + parts[1] + " " + parts[2];
                        default -> "jnz " + parts[1] + " " + parts[2];
                    })
                    .orElse("jnz " + parts[1] + " " + parts[2]);
            default -> instruction;
        };
    }
}

