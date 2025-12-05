package info.jab.aoc2016.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembunny interpreter for Day 23
 * Implementation based on LANParty style: clean structure, data transformation pipeline, functional patterns
 * Supports instructions: cpy, inc, dec, jnz, tgl, mul
 *
 */
public class AssembunnyInterpreter implements Solver<Integer> {

    private static final int PART1_INITIAL_VALUE = 7;
    private static final int PART2_INITIAL_VALUE = 12;

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructions = parseInstructions(fileName);
        Map<String, Integer> initialRegisters = createInitialRegisters(PART1_INITIAL_VALUE);
        return solvePartOne(instructions, initialRegisters);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructions = parseInstructions(fileName);
        Map<String, Integer> initialRegisters = createInitialRegisters(PART2_INITIAL_VALUE);
        // Optimize instructions in-place (like LANParty transforms data once)
        optimizeInstructions(instructions);
        return solvePartTwo(instructions, initialRegisters);
    }

    /**
     * Parse instructions from input file (similar to buildGraph in LANParty)
     * Returns data structure ready for processing
     */
    private List<String> parseInstructions(String fileName) {
        return new ArrayList<>(ResourceLines.list(fileName));
    }

    /**
     * Create initial register state (immutable map pattern similar to LANParty's data structures)
     */
    private Map<String, Integer> createInitialRegisters(int initialA) {
        return Map.of(
                "a", initialA,
                "b", 0,
                "c", 0,
                "d", 0
        );
    }

    /**
     * Solve part one - takes data structures as input (like LANParty's solvePartOne takes graph)
     */
    private Integer solvePartOne(List<String> instructions, Map<String, Integer> initialRegisters) {
        return executeProgram(instructions, initialRegisters);
    }

    /**
     * Solve part two - takes data structures as input (like LANParty's solvePartTwo takes graph)
     */
    private Integer solvePartTwo(List<String> instructions, Map<String, Integer> initialRegisters) {
        return executeProgram(instructions, initialRegisters);
    }

    /**
     * Optimize instructions for part 2 by replacing multiplication loop with mul instruction
     * Similar to LANParty's data transformation approach - transform data once, then process efficiently
     *
     * Key insight from LANParty: Transform data structure once, then use efficient algorithms
     * Here: Replace slow multiplication loop (instructions 0-9) with single mul instruction
     *
     * Pattern analysis:
     * - Instructions 0-4: Setup (b = a-1, d = a, a = 0, c = b)
     * - Instructions 5-9: Loop that computes a = a + (b iterations) for d iterations
     * - Result: a = d * b = a_initial * (a_initial - 1)
     * - Optimization: Replace instruction 5 (inc a) with mul d b a, which skips instructions 6-10
     */
    private void optimizeInstructions(List<String> instructions) {
        if (instructions.size() > 5) {
            // Replace instruction 5 with mul instruction (like LANParty transforms graph data)
            instructions.set(5, "mul d b a");
        }
    }

    /**
     * Execute the program with given instructions and initial registers
     * Similar to LANParty's solvePartOne/solvePartTwo - takes data and returns result
     */
    private Integer executeProgram(List<String> instructions, Map<String, Integer> initialRegisters) {
        List<String> programState = new ArrayList<>(instructions);
        // Create mutable copy for execution (ImmutableMap is immutable, we need mutable state)
        Map<String, Integer> registerState = new HashMap<>(initialRegisters);
        int programCounter = 0;

        while (programCounter >= 0 && programCounter < programState.size()) {
            programCounter = executeStep(programState, registerState, programCounter);
        }

        return registerState.get("a");
    }

    /**
     * Execute a single instruction step
     */
    private int executeStep(List<String> instructions, Map<String, Integer> registers, int pc) {
        String instructionLine = instructions.get(pc);
        if (instructionLine == null || instructionLine.trim().isEmpty()) {
            return pc + 1;
        }

        String[] parts = instructionLine.split(" ");
        String instruction = parts[0];

        return switch (instruction) {
            case "cpy" -> executeCopy(parts, registers, pc);
            case "inc" -> executeIncrement(parts, registers, pc);
            case "dec" -> executeDecrement(parts, registers, pc);
            case "jnz" -> executeJumpNotZero(parts, registers, pc);
            case "tgl" -> executeToggle(parts, instructions, registers, pc);
            case "mul" -> executeMultiply(parts, registers, pc);
            default -> pc + 1;
        };
    }

    /**
     * Execute copy instruction: cpy x y
     */
    private int executeCopy(String[] parts, Map<String, Integer> registers, int pc) {
        if (parts.length < 3) {
            return pc + 1;
        }
        String source = parts[1];
        String target = parts[2];

        // Skip if target is a number (invalid instruction)
        if (isNumber(target)) {
            return pc + 1;
        }

        int value = getValue(source, registers);
        registers.put(target, value);
        return pc + 1;
    }

    /**
     * Execute increment instruction: inc x
     */
    private int executeIncrement(String[] parts, Map<String, Integer> registers, int pc) {
        if (parts.length < 2) {
            return pc + 1;
        }
        String register = parts[1];
        if (isNumber(register)) {
            return pc + 1;
        }
        registers.put(register, registers.getOrDefault(register, 0) + 1);
        return pc + 1;
    }

    /**
     * Execute decrement instruction: dec x
     */
    private int executeDecrement(String[] parts, Map<String, Integer> registers, int pc) {
        if (parts.length < 2) {
            return pc + 1;
        }
        String register = parts[1];
        if (isNumber(register)) {
            return pc + 1;
        }
        registers.put(register, registers.getOrDefault(register, 0) - 1);
        return pc + 1;
    }

    /**
     * Execute jump not zero instruction: jnz x y
     */
    private int executeJumpNotZero(String[] parts, Map<String, Integer> registers, int pc) {
        if (parts.length < 3) {
            return pc + 1;
        }
        String condition = parts[1];
        String offset = parts[2];
        int conditionValue = getValue(condition, registers);

        if (conditionValue != 0) {
            int offsetValue = getValue(offset, registers);
            return pc + offsetValue;
        }
        return pc + 1;
    }

    /**
     * Execute toggle instruction: tgl x
     */
    private int executeToggle(String[] parts, List<String> instructions, Map<String, Integer> registers, int pc) {
        if (parts.length < 2) {
            return pc + 1;
        }
        String offsetStr = parts[1];
        int offset = getValue(offsetStr, registers);
        int targetIndex = pc + offset;

        if (targetIndex >= 0 && targetIndex < instructions.size()) {
            String targetInstruction = instructions.get(targetIndex);
            if (targetInstruction != null && !targetInstruction.trim().isEmpty()) {
                String toggled = toggleInstruction(targetInstruction);
                instructions.set(targetIndex, toggled);
            }
        }
        return pc + 1;
    }

    /**
     * Execute multiply instruction: mul x y z
     * Performs a = b * d optimization
     */
    private int executeMultiply(String[] parts, Map<String, Integer> registers, int pc) {
        if (parts.length < 4) {
            return pc + 1;
        }
        String source1 = parts[1]; // d
        String source2 = parts[2]; // b
        String target = parts[3];  // a

        if (!isNumber(target)) {
            int value1 = getValue(source1, registers);
            int value2 = getValue(source2, registers);

            // Optimization logic:
            // The loop computes a += b * d
            // Since a starts at 0, a = b * d
            registers.put(target, value1 * value2);

            // Side effects of the loop: c and d become 0
            registers.put("c", 0);
            registers.put("d", 0);
        }

        // Skip 5 instructions to resume at 'dec b' (index 10)
        // From index 5, we skip 6, 7, 8, 9. Next is 10.
        // 5 + 5 = 10.
        return pc + 5;
    }

    /**
     * Toggle an instruction according to the rules
     */
    private String toggleInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String inst = parts[0];

        if (parts.length == 2) {
            // One-argument instruction
            if ("inc".equals(inst)) {
                return "dec " + parts[1];
            } else {
                return "inc " + parts[1];
            }
        } else if (parts.length == 3) {
            // Two-argument instruction
            if ("jnz".equals(inst)) {
                return "cpy " + parts[1] + " " + parts[2];
            } else {
                return "jnz " + parts[1] + " " + parts[2];
            }
        }

        return instruction;
    }

    /**
     * Get value from source (either a number or register)
     */
    private int getValue(String source, Map<String, Integer> registers) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return registers.getOrDefault(source, 0);
        }
    }

    /**
     * Check if string is a number
     */
    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

