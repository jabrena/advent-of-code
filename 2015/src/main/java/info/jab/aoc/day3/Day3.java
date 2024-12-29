package info.jab.aoc.day3;

import info.jab.aoc.Day;

import java.util.HashSet;
import java.util.Set;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2015/day/3
 */
public class Day3 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var line = ResourceLines.line(fileName);

        Set<Point> visitedHouses = new HashSet<>();        
        int x = 0, y = 0;
        visitedHouses.add(new Point(x, y));
        
        for (char move : line.toCharArray()) {
            switch (move) {
                case '^' -> y++;
                case 'v' -> y--;
                case '>' -> x++;
                case '<' -> x--;
            }
            visitedHouses.add(new Point(x, y));
        }
        
        return visitedHouses.size();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var line = ResourceLines.line(fileName);
        
        Set<Point> visitedHouses = new HashSet<>();
        // Posiciones para Santa y Robo-Santa
        int santaX = 0, santaY = 0;
        int roboX = 0, roboY = 0;
        
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
                }
                visitedHouses.add(new Point(santaX, santaY));
            } else {
                // Turno de Robo-Santa
                switch (move) {
                    case '^' -> roboY++;
                    case 'v' -> roboY--;
                    case '>' -> roboX++;
                    case '<' -> roboX--;
                }
                visitedHouses.add(new Point(roboX, roboY));
            }
        }
        
        return visitedHouses.size();
    }
}
