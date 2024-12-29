package info.jab.aoc.day3;

import info.jab.aoc.Solver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

class Houses3 implements Solver<Integer> {

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

    enum Direction {
        UP('^', 0, 1),
        DOWN('v', 0, -1),
        RIGHT('>', 1, 0),
        LEFT('<', -1, 0);

        private static final Map<Character, Direction> SYMBOL_MAP = 
            Arrays.stream(values()).collect(Collectors.toMap(dir -> dir.symbol, dir -> dir));


        private final char symbol;
        private final int dx;
        private final int dy;

        Direction(char symbol, int dx, int dy) {
            this.symbol = symbol;
            this.dx = dx;
            this.dy = dy;
        }

        public static Direction fromSymbol(char symbol) {
            return Optional.ofNullable(SYMBOL_MAP.get(symbol))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid direction symbol: " + symbol));
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

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
