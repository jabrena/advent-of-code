package info.jab.aoc2016.day2;

import com.putoet.grid.Point;

/**
 * Represents a direction on the keypad with movement logic.
 */
public record KeypadDirection(Point point) {
    KeypadDirection move(char direction) {
        Move move = Move.fromChar(direction);
        Point newPoint = calculate(move);
        return new KeypadDirection(newPoint);
    }

    private Point calculate(Move move) {
        return switch (move) {
            case U -> point.sub(Point.NORTH);
            case D -> point.sub(Point.SOUTH);
            case L -> point.add(Point.WEST);
            case R -> point.add(Point.EAST);
        };
    }
}

