package info.jab.aoc2015.day3;

import info.jab.aoc.Solver;

import java.util.HashSet;
import java.util.Set;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

class Houses implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var line = ResourceLines.line(fileName);

        Set<Point> visitedHouses = new HashSet<>();
        State state = new State(0, 0);
        visitedHouses.add(state.toPoint());

        for (char move : line.toCharArray()) {
            state = state.move(Direction.fromSymbol(move));
            visitedHouses.add(state.toPoint());
        }
        return visitedHouses.size();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var line = ResourceLines.line(fileName);

        Set<Point> visitedHouses = new HashSet<>();
        State santaState = new State(0, 0);
        State roboState = new State(0, 0);

        visitedHouses.add(new Point(0, 0));

        for (int i = 0; i < line.length(); i++) {
            char move = line.charAt(i);
            if (i % 2 == 0) {
                santaState = santaState.move(Direction.fromSymbol(move));
                visitedHouses.add(santaState.toPoint());
            } else {
                roboState = roboState.move(Direction.fromSymbol(move));
                visitedHouses.add(roboState.toPoint());
            }
        }

        return visitedHouses.size();
    }
}
