package info.jab.aoc;

/**
 * Advent of Code defines a set of problems every December.
 * Every day, the website publish 2 problems to solve.
 * This interface define the problems to solve per Day.
 *
 * @param <X> Type
 * @param <Y> Parameter 1
 * @param <Z> Parameter 2
 * @param <V> Parameter 3
 * @param <W> Parameter 4
 */
public interface Day4<X, Y, Z, V, W> {
    X getPart1Result(Y parameter1, Z parameter2, V parameter3, W parameter4);

    X getPart2Result(Y parameter1, Z parameter2, V parameter3, W parameter4);
}

