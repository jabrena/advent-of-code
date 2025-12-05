package info.jab.aoc2016.day2;

import java.util.List;
import com.putoet.grid.Grid;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BathroomSecurity2 implements Solver<String>{

    private final char[][] keypadPart1 = {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };

    private final char[][] keypadPart2 = {
        {' ', ' ', '1', ' ', ' '},
        {' ', '2', '3', '4', ' '},
        {'5', '6', '7', '8', '9'},
        {' ', 'A', 'B', 'C', ' '},
        {' ', ' ', 'D', ' ', ' '}
    };

    private enum Move {
        U, D, L, R;

        public static Move fromChar(char direction) {
            return Move.valueOf(String.valueOf(direction));
        }
    }

    private record Direction(Point point) {
        Direction move(char direction) {
            Move move = Move.fromChar(direction);
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

    private boolean isValidPosition(Point point, Grid grid) {
        return grid.contains(point) &&
               grid.get(point) != ' ';
    }

    @Override
    public String solvePartOne(String fileName) {
        StringBuilder result = new StringBuilder();

        Grid grid = new Grid(keypadPart1);
        Direction current = initializeStartPosition(grid);
        List<String> lines = ResourceLines.list(fileName);
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                Direction next = current.move(move);
                if (grid.contains(next.point())) {
                    current = next;
                }
            }
            result.append(grid.get(current.point()));
        }
        return result.toString();
    }

    @Override
    public String solvePartTwo(String fileName) {
        StringBuilder result = new StringBuilder();

        Grid grid = new Grid(keypadPart2);
        Direction current = initializeStartPosition(grid);
    
        List<String> lines = ResourceLines.list(fileName);
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                Direction next = current.move(move);
                if (isValidPosition(next.point(), grid)) {
                    current = next;
                }
            }
            result.append(grid.get(current.point()));
        }
        return result.toString();
    }
}
