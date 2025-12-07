package info.jab.aoc2016.day2;

import com.putoet.grid.Grid;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BathroomSecurity3 implements Solver<String>{

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

    private KeypadDirection initializeStartPosition(Grid grid) {
        Point start = grid.findFirst(c -> c == '5').orElseThrow();
        return new KeypadDirection(start);
    }

    //The second condition is not required in the first part.
    private boolean isValidPosition(Point point, Grid grid) {
        return grid.contains(point) &&
               grid.get(point) != ' ';
    }

    private KeypadState processLine(Grid grid, String line, KeypadState state) {
        KeypadDirection current = state.current();
        for (char move : line.toCharArray()) {
            KeypadDirection next = current.move(move);
            if (isValidPosition(next.point(), grid)) {
                current = next;
            }
        }
        return state.withCurrent(current).append(grid.get(current.point()));
    }

    @Override
    public String solvePartOne(String fileName) {
        Grid grid = new Grid(keypadPart1);
        KeypadDirection initial = initializeStartPosition(grid);
        
        var list = ResourceLines.list(fileName);
        return list.stream()
            .reduce(new KeypadState(initial, ""),
                (state, line) -> processLine(grid, line, state),
                (s1, s2) -> s1
            )
            .result();
    }

    @Override
    public String solvePartTwo(String fileName) {
        Grid grid = new Grid(keypadPart2);
        KeypadDirection initial = initializeStartPosition(grid);
        
        var list = ResourceLines.list(fileName);
        return list.stream()
            .reduce(new KeypadState(initial, ""),
                (state, line) -> processLine(grid, line, state),
                (s1, s2) -> s1
            )
            .result();
    }
}
