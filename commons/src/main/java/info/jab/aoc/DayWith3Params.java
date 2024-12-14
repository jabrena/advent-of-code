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
 */
public interface DayWith3Params<X, Y, Z, V> {
    X getPart1Result(Y parameter1, Z parameter2, V parameter3);

    X getPart2Result(Y parameter1, Z parameter2, V parameter3);
}
