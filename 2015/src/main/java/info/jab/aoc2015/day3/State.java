package info.jab.aoc2015.day3;

import com.putoet.grid.Point;

record State(int x, int y) {
    public State move(Direction direction) {
        return new State(
            x + direction.getDx(),
            y + direction.getDy()
        );
    }

    public Point toPoint() {
        return new Point(x, y);
    }
}

