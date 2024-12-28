package info.jab.aoc.day2;

import java.util.List;
import com.putoet.grid.Grid;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BathroomSecurity2 implements Solver<String>{

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
        U, D, L, R;

        public Point apply(Point point) {
            return switch (this) {
                case U -> point.sub(Point.NORTH);
                case D -> point.sub(Point.SOUTH);
                case L -> point.add(Point.WEST);
                case R -> point.add(Point.EAST);
            };
        }
    }

    private record Direction(Point point) {
        Direction move(char direction) {
            Move move = Move.valueOf(String.valueOf(direction));
            Point newPoint = move.apply(point);
            return new Direction(newPoint);
        }
    }

    private boolean isValidPosition(Point point, Grid grid) {
        return grid.contains(point) &&
               grid.get(point) != ' ';
    }

    private Direction initializeStartPosition(Grid grid) {
        Point start = grid.findFirst(c -> c == '5').orElseThrow();
        return new Direction(start);
    }

    @Override
    public String solvePartOne(String fileName) {
        StringBuilder result = new StringBuilder();

        // Start at 5
        Grid grid = new Grid(KEYPAD_PART1);
        Direction current = initializeStartPosition(grid);
    
        List<String> lines = ResourceLines.list(fileName);
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                Direction next = current.move(move);
                if (isValidPosition(next.point(), grid)) {
                    current = next;
                }
            }
            result.append(KEYPAD_PART1[current.point().y()][current.point().x()]);
        }
        return result.toString();
    }

    @Override
    public String solvePartTwo(String fileName) {
        StringBuilder result = new StringBuilder();

        // Start at 5
        Grid grid = new Grid(KEYPAD_PART2);
        Direction current = initializeStartPosition(grid);
    
        List<String> lines = ResourceLines.list(fileName);
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                Direction next = current.move(move);
                if (isValidPosition(next.point(), grid)) {
                    current = next;
                }
            }
            result.append(KEYPAD_PART2[current.point().y()][current.point().x()]);
        }
        return result.toString();
    }
}
