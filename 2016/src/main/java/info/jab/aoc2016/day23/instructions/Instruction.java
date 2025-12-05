package info.jab.aoc2016.day23.instructions;

import info.jab.aoc2016.day23.ExecutionResult;
import info.jab.aoc2016.day23.ProgramState;

import java.util.Optional;

/**
 * Sealed interface for Assembunny instructions
 * Each instruction type is represented by a sealed subclass that encapsulates its execution logic
 * Follows DOP principles: separates code from data, data-oriented design
 */
public sealed interface Instruction
        permits Copy, Increment, Decrement, JumpNotZero, Toggle, Multiply {

    /**
     * Execute this instruction with the given parts and program state
     * Returns an ExecutionResult containing the next state and continuation flag
     */
    ExecutionResult execute(String[] parts, ProgramState state);

    /**
     * Factory method to create an Instruction from a string representation
     */
    static Optional<Instruction> fromString(String instruction) {
        return switch (instruction.toLowerCase()) {
            case "cpy" -> Optional.of(new Copy());
            case "inc" -> Optional.of(new Increment());
            case "dec" -> Optional.of(new Decrement());
            case "jnz" -> Optional.of(new JumpNotZero());
            case "tgl" -> Optional.of(new Toggle());
            case "mul" -> Optional.of(new Multiply());
            default -> Optional.empty();
        };
    }

    /**
     * Get the instruction name as a lowercase string
     */
    default String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }
}

