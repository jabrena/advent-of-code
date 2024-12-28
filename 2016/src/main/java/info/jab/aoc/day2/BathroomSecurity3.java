package info.jab.aoc.day2;

import java.util.List;
import com.putoet.grid.Grid;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BathroomSecurity3 implements Solver<String>{

    private final char[][] KEYPAD_PART1 = {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };

    private final char[][] KEYPAD_PART2 = {
        {' ', ' ', '1', ' ', ' '},
        {' ', '2', '3', '4', ' '},
        {'5', '6', '7', '8', '9'},
        {' ', 'A', 'B', 'C', ' '},
        {' ', ' ', 'D', ' ', ' '}
    };

    private enum Move {
        U, D, L, R
    }

    private record Direction(Point point) {
        Direction move(char direction) {
            Move move = Move.valueOf(String.valueOf(direction));
            Point newPoint = calculate(move);
            return new Direction(newPoint);
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

    private Direction initializeStartPosition(Grid grid) {
        Point start = grid.findFirst(c -> c == '5').orElseThrow();
        return new Direction(start);
    }

    //The second condition is not required in the first part.
    private boolean isValidPosition(Point point, Grid grid) {
        return grid.contains(point) &&
               grid.get(point) != ' ';
    }

    record State(Direction current, StringBuilder result) {}

    private State processLine(Grid grid, String line, State state) {
        Direction current = state.current;
        for (char move : line.toCharArray()) {
            Direction next = current.move(move);
            if (isValidPosition(next.point(), grid)) {
                current = next;
            }
        }
        return new State(current, state.result.append(grid.get(current.point())));
    }

    @Override
    public String solvePartOne(String fileName) {
        Grid grid = new Grid(KEYPAD_PART1);
        Direction initial = initializeStartPosition(grid);
        
        var list = ResourceLines.list(fileName);
        return list.stream()
            .reduce(new State(initial, new StringBuilder()),
                (state, line) -> processLine(grid, line, state),
                (s1, s2) -> s1
            )
            .result().toString();
    }

    @Override
    public String solvePartTwo(String fileName) {
        Grid grid = new Grid(KEYPAD_PART2);
        Direction initial = initializeStartPosition(grid);
        
        var list = ResourceLines.list(fileName);
        return list.stream()
            .reduce(new State(initial, new StringBuilder()),
                (state, line) -> processLine(grid, line, state),
                (s1, s2) -> s1
            )
            .result().toString();
    }
}
