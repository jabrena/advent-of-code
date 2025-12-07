package info.jab.aoc2016.day1;

/**
 * Represents a cardinal direction for navigation.
 */
public enum Direction {
    NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);
    
    final int dx;
    final int dy;
    
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    Direction turnLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }
    
    Direction turnRight() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
}

