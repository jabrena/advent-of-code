package info.jab.aoc.day16;

import java.util.ArrayList;
import java.util.List;

class State2 implements Comparable<State2> {
    int x, y, score;
    Direction direction;
    List<int[]> path; // Track tiles visited in this path

    public State2(int x, int y, Direction direction, int score, List<int[]> path) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.score = score;
        this.path = new ArrayList<>(path);
        this.path.add(new int[]{x, y}); // Add current tile to path
    }

    @Override
    public int compareTo(State2 other) {
        return Integer.compare(this.score, other.score);
    }
}
