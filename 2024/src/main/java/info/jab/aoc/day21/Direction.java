package info.jab.aoc.day21;

import com.putoet.grid.Point;

enum Direction {
    UP(0, -1, '^'),
    DOWN(0, 1, 'v'),
    LEFT(-1, 0, '<'),
    RIGHT(1, 0, '>');

    private final int dx; // Change in x-coordinate
    private final int dy; // Change in y-coordinate
    private final char symbol; // Character representation of the direction

    Direction(int dx, int dy, char symbol) {
        this.dx = dx;
        this.dy = dy;
        this.symbol = symbol;
    }

    /**
     * Converts the direction to its character representation.
     * @return The character representation of the direction.
     */
    char toChar() {
        return symbol;
    }

    /**
     * Moves a point in the specified direction.
     * @param pos The starting point.
     * @return The new point after moving in the direction.
     */
    Point move(Point pos) {
        return pos.add(new Point(dx, dy));
    }
}
