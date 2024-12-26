package info.jab.aoc;

/**
 * Advent of Code defines a set of problems every December.
 * Every day, the website publish 2 problems to solve.
 * This interface define the problems to solve per Day.
 *
 * @param <T> Type
 */
public interface Solver<T> {
    T solvePartOne(String fileName);

    T solvePartTwo(String fileName);
}
