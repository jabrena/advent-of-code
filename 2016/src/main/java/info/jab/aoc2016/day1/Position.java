package info.jab.aoc2016.day1;

/**
 * Represents a position in 2D space.
 */
public record Position(int x, int y) {
    int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
    
    Position move(Direction direction, int steps) {
        return new Position(x + direction.dx * steps, y + direction.dy * steps);
    }
}

