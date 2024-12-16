package info.jab.aoc.day16;

class State implements Comparable<State> {
    int x, y, score;
    Direction direction;

    public State(int x, int y, Direction direction, int score) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.score = score;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.score, other.score);
    }
}

