package info.jab.aoc;

/**
 * Advent of Code defines a set of problems every December.
 * Every day, the website publish 2 problems to solve.
 * This interface define the problems to solve per Day.
 *
 * @param <X> Type
 * @param <Y> Parameter 1
 * @param <Z> Parameter 2
 */
public interface Solver2<X, Y, Z> {
    X solvePartOne(Y parameter1, Z parameter2);
    X solvePartTwo(Y parameter1, Z parameter2);
}
