package info.jab.aoc2016.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Assembunny interpreter for Day 23
 * Functional programming implementation with immutable data structures and pure transformations
 * Supports instructions: cpy, inc, dec, jnz, tgl, mul
 */
public class AssembunnyInterpreterV2 implements Solver<Integer> {

    private static final int PART1_INITIAL_VALUE = 7;
    private static final int PART2_INITIAL_VALUE = 12;

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        Map<String, Integer> initialRegisters = createInitialRegisters(PART1_INITIAL_VALUE);
        return executeProgram(instructions, initialRegisters);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        Map<String, Integer> initialRegisters = createInitialRegisters(PART2_INITIAL_VALUE);
        List<String> optimizedInstructions = optimizeInstructions(instructions);
        return executeProgram(optimizedInstructions, initialRegisters);
    }

    /**
     * Create initial register state - returns immutable map
     * Pure function: deterministic output based on input
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
     * Optimize instructions for part 2 by replacing multiplication loop with mul instruction
     * Pure function: returns new list instead of mutating input
     *
     * Pattern analysis:
     * - Instructions 0-4: Setup (b = a-1, d = a, a = 0, c = b)
     * - Instructions 5-9: Loop that computes a = a + (b iterations) for d iterations
     * - Result: a = d * b = a_initial * (a_initial - 1)
     * - Optimization: Replace instruction 5 (inc a) with mul d b a, which skips instructions 6-10
     */
    private List<String> optimizeInstructions(List<String> instructions) {
        if (instructions.size() <= 5) {
            return instructions;
        }

        return IntStream.range(0, instructions.size())
                .mapToObj(i -> i == 5 ? "mul d b a" : instructions.get(i))
                .toList();
    }

    /**
     * Execute the program with given instructions and initial registers
     * Functional approach: uses immutable state transformations
     */
    private Integer executeProgram(List<String> instructions, Map<String, Integer> initialRegisters) {
        // Create mutable copies for execution (required for interpreter semantics)
        List<String> mutableInstructions = new ArrayList<>(instructions);
        Map<String, Integer> mutableRegisters = new HashMap<>(initialRegisters);

        ProgramState state = new ProgramState(mutableInstructions, mutableRegisters, 0);

        while (isValidProgramCounter(state)) {
            ExecutionResult result = executeStep(state);
            state = result.nextState();
            if (!result.shouldContinue()) {
                break;
            }
        }

        return Optional.ofNullable(state.registers().get("a"))
                .orElse(0);
    }

    /**
     * Check if program counter is valid
     * Pure function: no side effects
     */
    private boolean isValidProgramCounter(ProgramState state) {
        int pc = state.programCounter();
        return pc >= 0 && pc < state.instructions().size();
    }

    /**
     * Execute a single instruction step
     * Returns new state instead of mutating
     */
    private ExecutionResult executeStep(ProgramState state) {
        int pc = state.programCounter();
        String instructionLine = Optional.ofNullable(state.instructions().get(pc))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .orElse("");

        if (instructionLine.isEmpty()) {
            return ExecutionResult.continueWith(state.withProgramCounter(pc + 1));
        }

        String[] parts = instructionLine.split(" ");
        String instruction = parts[0];

        return switch (instruction) {
            case "cpy" -> executeCopy(parts, state);
            case "inc" -> executeIncrement(parts, state);
            case "dec" -> executeDecrement(parts, state);
            case "jnz" -> executeJumpNotZero(parts, state);
            case "tgl" -> executeToggle(parts, state);
            case "mul" -> executeMultiply(parts, state);
            default -> ExecutionResult.continueWith(state.withProgramCounter(pc + 1));
        };
    }

    /**
     * Execute copy instruction: cpy x y
     * Pure transformation: creates new register map
     */
    private ExecutionResult executeCopy(String[] parts, ProgramState state) {
        if (parts.length < 3) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String source = parts[1];
        String target = parts[2];

        // Skip if target is a number (invalid instruction)
        if (isNumber(target)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        int value = getValue(source, state.registers());
        Map<String, Integer> newRegisters = updateRegister(state.registers(), target, value);
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 1)
        );
    }

    /**
     * Execute increment instruction: inc x
     * Pure transformation: creates new register map
     */
    private ExecutionResult executeIncrement(String[] parts, ProgramState state) {
        if (parts.length < 2) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String register = parts[1];
        if (isNumber(register)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        int currentValue = state.registers().getOrDefault(register, 0);
        Map<String, Integer> newRegisters = updateRegister(state.registers(), register, currentValue + 1);
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 1)
        );
    }

    /**
     * Execute decrement instruction: dec x
     * Pure transformation: creates new register map
     */
    private ExecutionResult executeDecrement(String[] parts, ProgramState state) {
        if (parts.length < 2) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String register = parts[1];
        if (isNumber(register)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        int currentValue = state.registers().getOrDefault(register, 0);
        Map<String, Integer> newRegisters = updateRegister(state.registers(), register, currentValue - 1);
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 1)
        );
    }

    /**
     * Execute jump not zero instruction: jnz x y
     * Pure function: computes next program counter based on current state
     */
    private ExecutionResult executeJumpNotZero(String[] parts, ProgramState state) {
        if (parts.length < 3) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String condition = parts[1];
        String offset = parts[2];
        int conditionValue = getValue(condition, state.registers());

        int nextPc = conditionValue != 0
                ? state.programCounter() + getValue(offset, state.registers())
                : state.programCounter() + 1;

        return ExecutionResult.continueWith(state.withProgramCounter(nextPc));
    }

    /**
     * Execute toggle instruction: tgl x
     * Mutates instructions list (required by interpreter semantics)
     */
    private ExecutionResult executeToggle(String[] parts, ProgramState state) {
        if (parts.length < 2) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String offsetStr = parts[1];
        int offset = getValue(offsetStr, state.registers());
        int targetIndex = state.programCounter() + offset;

        if (targetIndex >= 0 && targetIndex < state.instructions().size()) {
            String targetInstruction = state.instructions().get(targetIndex);
            Optional.ofNullable(targetInstruction)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .ifPresent(instruction -> {
                        String toggled = toggleInstruction(instruction);
                        state.instructions().set(targetIndex, toggled);
                    });
        }

        return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
    }

    /**
     * Execute multiply instruction: mul x y z
     * Pure transformation: creates new register map
     */
    private ExecutionResult executeMultiply(String[] parts, ProgramState state) {
        if (parts.length < 4) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 1));
        }

        String source1 = parts[1]; // d
        String source2 = parts[2]; // b
        String target = parts[3];  // a

        if (isNumber(target)) {
            return ExecutionResult.continueWith(state.withProgramCounter(state.programCounter() + 5));
        }

        int value1 = getValue(source1, state.registers());
        int value2 = getValue(source2, state.registers());

        // Optimization logic: a = b * d, and side effects: c = 0, d = 0
        Map<String, Integer> newRegisters = updateRegister(state.registers(), target, value1 * value2);
        newRegisters = updateRegister(newRegisters, "c", 0);
        newRegisters = updateRegister(newRegisters, "d", 0);

        // Skip 5 instructions to resume at 'dec b' (index 10)
        return ExecutionResult.continueWith(
                state.withRegisters(newRegisters)
                     .withProgramCounter(state.programCounter() + 5)
        );
    }

    /**
     * Toggle an instruction according to the rules
     * Pure function: deterministic transformation
     */
    private String toggleInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String inst = parts[0];

        return switch (parts.length) {
            case 2 -> switch (inst) {
                case "inc" -> "dec " + parts[1];
                default -> "inc " + parts[1];
            };
            case 3 -> switch (inst) {
                case "jnz" -> "cpy " + parts[1] + " " + parts[2];
                default -> "jnz " + parts[1] + " " + parts[2];
            };
            default -> instruction;
        };
    }

    /**
     * Get value from source (either a number or register)
     * Pure function: no side effects, deterministic
     */
    private int getValue(String source, Map<String, Integer> registers) {
        return parseInteger(source)
                .orElseGet(() -> registers.getOrDefault(source, 0));
    }

    /**
     * Check if string is a number
     * Pure function: no side effects
     */
    private boolean isNumber(String str) {
        return parseInteger(str).isPresent();
    }

    /**
     * Parse integer from string - returns Optional for null safety
     * Pure function: no side effects
     */
    private Optional<Integer> parseInteger(String str) {
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
    private Map<String, Integer> updateRegister(Map<String, Integer> registers, String register, int value) {
        Map<String, Integer> newRegisters = new HashMap<>(registers);
        newRegisters.put(register, value);
        return newRegisters;
    }
}
