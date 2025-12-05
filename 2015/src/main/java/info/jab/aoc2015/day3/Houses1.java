package info.jab.aoc2015.day3;

import info.jab.aoc.Solver;

import java.util.HashSet;
import java.util.Set;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

class Houses1 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var line = ResourceLines.line(fileName);

        Set<Point> visitedHouses = new HashSet<>();        
        int x = 0;
        int y = 0;
        visitedHouses.add(new Point(x, y));
        
        for (char move : line.toCharArray()) {
            switch (move) {
                case '^' -> y++;
                case 'v' -> y--;
                case '>' -> x++;
                case '<' -> x--;
                default -> {
                    // Ignore unknown characters
                }
            }
            visitedHouses.add(new Point(x, y));
        }
        
        return visitedHouses.size();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var line = ResourceLines.line(fileName);
        
        Set<Point> visitedHouses = new HashSet<>();
        // Posiciones para Santa y Robo-Santa
        int santaX = 0;
        int santaY = 0;
        int roboX = 0;
        int roboY = 0;
        
        // Ambos comienzan en la misma casa
        visitedHouses.add(new Point(0, 0));
        
        // Procesar movimientos alternando entre Santa y Robo-Santa
        for (int i = 0; i < line.length(); i++) {
            char move = line.charAt(i);
            // Determinar si es turno de Santa (índices pares) o Robo-Santa (índices impares)
            if (i % 2 == 0) {
                // Turno de Santa
                switch (move) {
                    case '^' -> santaY++;
                    case 'v' -> santaY--;
                    case '>' -> santaX++;
                    case '<' -> santaX--;
                    default -> {
                        // Ignore unknown characters
                    }
                }
                visitedHouses.add(new Point(santaX, santaY));
            } else {
                // Turno de Robo-Santa
                switch (move) {
                    case '^' -> roboY++;
                    case 'v' -> roboY--;
                    case '>' -> roboX++;
                    case '<' -> roboX--;
                    default -> {
                        // Ignore unknown characters
                    }
                }
                visitedHouses.add(new Point(roboX, roboY));
            }
        }
        
        return visitedHouses.size();
    }
}
