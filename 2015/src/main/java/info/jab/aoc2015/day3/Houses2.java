package info.jab.aoc2015.day3;

import info.jab.aoc.Solver;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

class Houses2 implements Solver<Integer> {
    
    enum Direction {
        UP('^', 0, 1),
        DOWN('v', 0, -1),
        RIGHT('>', 1, 0),
        LEFT('<', -1, 0);

        private final char symbol;
        private final int dx;
        private final int dy;

        Direction(char symbol, int dx, int dy) {
            this.symbol = symbol;
            this.dx = dx;
            this.dy = dy;
        }

        public static Direction fromSymbol(char symbol) {
            return Stream.of(values())
                    .filter(dir -> dir.symbol == symbol)
                    .findFirst()
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
        int x = 0, y = 0;
        visitedHouses.add(new Point(x, y));
        
        for (char move : line.toCharArray()) {
            Direction direction = Direction.fromSymbol(move);
            x += direction.getDx();
            y += direction.getDy();
            visitedHouses.add(new Point(x, y));
        }
        
        return visitedHouses.size();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var line = ResourceLines.line(fileName);
        
        Set<Point> visitedHouses = new HashSet<>();
        int santaX = 0, santaY = 0;
        int roboX = 0, roboY = 0;
        
        visitedHouses.add(new Point(0, 0));
        
        for (int i = 0; i < line.length(); i++) {
            char move = line.charAt(i);
            // Determinar si es turno de Santa (índices pares) o Robo-Santa (índices impares)
            if (i % 2 == 0) {
                // Turno de Santa
                Direction direction = Direction.fromSymbol(move);
                santaX += direction.getDx();
                santaY += direction.getDy();
                visitedHouses.add(new Point(santaX, santaY));
            } else {
                // Turno de Robo-Santa
                Direction direction = Direction.fromSymbol(move);
                roboX += direction.getDx();
                roboY += direction.getDy();
                visitedHouses.add(new Point(roboX, roboY));
            }
        }
        
        return visitedHouses.size();
    }
}
