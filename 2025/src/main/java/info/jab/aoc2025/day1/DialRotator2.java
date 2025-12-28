package info.jab.aoc2025.day1;

import module java.base;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

public final class DialRotator2 implements Solver<Integer> {

    private static final int END = 100;
    private static final int INITIAL_POSITION = 50;

    private record State(int position, int zeros) {}
    private record Sequence(int direction, int steps) {}

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        return ResourceLines.list(fileName).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(this::parseSequence)
                .map(seq -> seq.direction() * seq.steps())
                .gather(Gatherers.scan(() -> INITIAL_POSITION, (dial, rotation) -> Math.floorMod(dial + rotation, END)))
                .filter(dial -> dial == 0)
                .mapToInt(dial -> 1)
                .sum();
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        return processSequences(
                ResourceLines.list(fileName).stream()
                        .filter(line -> !line.trim().isEmpty())
                        .map(this::parseSequence)
        ).zeros();
    }

    /**
     * Parses a line into a Sequence object.
     *
     * @param line Input line containing direction and distance (e.g., "L68", "R30")
     * @return Sequence object with direction (-1 for L, 1 for R) and steps
     */
    private Sequence parseSequence(final String line) {
        var direction = Direction.from(line.trim().charAt(0));
        var dir = switch (direction) {
            case LEFT -> -1;
            case RIGHT -> 1;
        };
        var dist = Integer.parseInt(line.trim().substring(1));
        return new Sequence(dir, dist);
    }

    /**
     * Processes sequences by accumulating state and counting zero crossings.
     *
     * @param sequences Stream of rotation sequences to process
     * @return Final state after processing all sequences
     */
    private State processSequences(final Stream<Sequence> sequences) {
        return sequences
                .gather(Gatherers.fold(
                        () -> new State(INITIAL_POSITION, 0),
                        (state, seq) -> {
                            var directionValue = seq.direction();
                            var steps = seq.steps();
                            var newPos = Math.floorMod(state.position + directionValue * steps, END);

                            // Count zero crossings during rotation
                            var remainder = Math.floorMod(-directionValue * state.position, END);
                            var first = remainder == 0 ? END : remainder;
                            var zeros = state.zeros;
                            if (steps >= first) {
                                zeros += 1 + (steps - first) / END;
                            }

                            return new State(newPos, zeros);
                        }
                ))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No result found"));
    }
}
