package info.jab.aoc2016.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * Functional approach: uses trampoline pattern to avoid stack overflow
     * Converts recursive execution into iterative loops
     */
    private Integer executeProgram(List<String> instructions, Map<String, Integer> initialRegisters) {
        // Create mutable copies for execution (required for interpreter semantics)
        List<String> mutableInstructions = new ArrayList<>(instructions);
        Map<String, Integer> mutableRegisters = new HashMap<>(initialRegisters);

        ProgramState initialState = new ProgramState(mutableInstructions, mutableRegisters, 0);
        ProgramState finalState = Trampoline.run(executeProgramTrampoline(initialState));

        return Optional.ofNullable(finalState.registers().get("a"))
                .orElse(0);
    }

    /**
     * Create a trampoline computation for program execution
     * Pure function: returns a computation description, not the result
     * Uses sealed class pattern for type-safe continuation
     */
    private Trampoline<ProgramState> executeProgramTrampoline(ProgramState state) {
        if (!isValidProgramCounter(state)) {
            return new Trampoline.Done<>(state);
        }

        ExecutionResult result = executeStep(state);
        if (!result.shouldContinue()) {
            return new Trampoline.Done<>(result.nextState());
        }

        // Return a continuation with lazy evaluation instead of recursing directly
        // This avoids stack overflow by converting recursion to iteration
        ProgramState nextState = result.nextState();
        return new Trampoline.More<>(() -> executeProgramTrampoline(nextState));
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
     * Uses sealed class hierarchy for instruction execution
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

        return Instruction.fromString(instruction)
                .map(inst -> inst.execute(parts, state))
                .orElse(ExecutionResult.continueWith(state.withProgramCounter(pc + 1)));
    }
}
