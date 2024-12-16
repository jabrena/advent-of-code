package info.jab.aoc.day16;

enum Direction {
    EAST, NORTH, WEST, SOUTH;

    public Direction rotateClockwise() {
        return values()[(this.ordinal() + 1) % 4];
    }

    public Direction rotateCounterClockwise() {
        return values()[(this.ordinal() + 3) % 4];
    }
}
