package info.jab.aoc2025.day6;

import java.util.List;

/**
 * Sealed interface representing mathematical operators for block calculations.
 * Each operator is implemented as an immutable record following functional programming principles.
 * This design enables exhaustive pattern matching and type-safe operator handling.
 */
public sealed interface MathOperator permits MathOperator.Addition, MathOperator.Multiplication, MathOperator.None {

    /**
     * Applies the operator to a list of numbers.
     *
     * @param numbers The list of numbers to operate on
     * @return The result of applying the operator
     */
    long apply(List<Long> numbers);

    /**
     * Returns the symbol representation of the operator.
     *
     * @return The symbol string
     */
    String symbol();

    /**
     * Addition operator: sums all numbers.
     */
    record Addition() implements MathOperator {
        @Override
        public long apply(final List<Long> numbers) {
            return numbers.stream().mapToLong(Long::longValue).sum();
        }

        @Override
        public String symbol() {
            return "+";
        }
    }

    /**
     * Multiplication operator: multiplies all numbers.
     */
    record Multiplication() implements MathOperator {
        @Override
        public long apply(final List<Long> numbers) {
            return numbers.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
        }

        @Override
        public String symbol() {
            return "*";
        }
    }

    /**
     * No operator: returns zero.
     */
    record None() implements MathOperator {
        @Override
        public long apply(final List<Long> numbers) {
            return 0;
        }

        @Override
        public String symbol() {
            return " ";
        }
    }

    // Constants for convenience (since records create new instances, we provide singletons)
    MathOperator ADDITION = new Addition();
    MathOperator MULTIPLICATION = new Multiplication();
    MathOperator NONE = new None();

    /**
     * Parses a string to find the corresponding operator.
     *
     * @param text The text to parse
     * @return The operator found, or NONE if none found
     */
    static MathOperator from(final String text) {
        if (text == null || text.isEmpty()) {
            return NONE;
        }
        if (text.contains(ADDITION.symbol())) {
            return ADDITION;
        }
        if (text.contains(MULTIPLICATION.symbol())) {
            return MULTIPLICATION;
        }
        return NONE;
    }
}
