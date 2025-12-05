package info.jab.aoc2016.day23;

/**
 * Execution result record
 * Represents the result of executing a single instruction step
 */
public record ExecutionResult(
        ProgramState nextState,
        boolean shouldContinue) {
    /**
     * Create an execution result that continues execution
     */
    public static ExecutionResult continueWith(ProgramState state) {
        return new ExecutionResult(state, true);
    }
}
