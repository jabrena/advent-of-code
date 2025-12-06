package info.jab.aoc;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Trampoline computation - represents either a completed computation (Done)
 * or a continuation (More) that needs further evaluation
 * Follows FP principles: eliminates stack overflow from deep recursion
 *
 * The trampoline pattern converts recursive calls into iterative loops,
 * making it safe for deep recursion in languages without tail-call optimization.
 *
 * @param <T> the result type of the computation
 */
@SuppressWarnings("java:S2326") // T is used in sealed permits and implementing records
public sealed interface Trampoline<T> permits Trampoline.Done, Trampoline.More {

    /**
     * Maximum number of iterations allowed to prevent DoS attacks.
     * This limit prevents infinite loops in trampoline computations.
     */
    int MAX_ITERATIONS = 1_000_000;

    /**
     * Evaluate the trampoline computation iteratively using Stream API
     * Converts recursive calls into iterative loops, avoiding stack overflow
     * Uses functional approach: no mutation, declarative style
     *
     * @param <T> the result type
     * @param trampoline the trampoline computation to evaluate
     * @return the final result after evaluating all continuations
     * @throws IllegalStateException if the computation exceeds the maximum iteration limit
     */
    static <T> T run(Trampoline<T> trampoline) {
        return Stream.iterate(
                trampoline,
                t -> t instanceof More<T>(var compute) ? compute.get() : t
        )
        .limit(MAX_ITERATIONS)
        .dropWhile(t -> t instanceof More<T>)
        .findFirst()
        .map(t -> ((Done<T>) t).result())
        .orElseThrow(() -> new IllegalStateException(
                "Trampoline computation exceeded maximum iteration limit of " + MAX_ITERATIONS));
    }

    /**
     * Represents a completed computation with a final result
     */
    record Done<T>(T result) implements Trampoline<T> {
    }

    /**
     * Represents a continuation that needs further evaluation
     * Uses Supplier for lazy evaluation to prevent immediate recursive calls
     */
    record More<T>(Supplier<Trampoline<T>> compute) implements Trampoline<T> {
    }
}

