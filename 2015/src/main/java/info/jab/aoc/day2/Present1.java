package info.jab.aoc.day2;

import java.util.Arrays;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/2
 */
public class Present1 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(line -> {
                String[] dimensions = line.split("x");
                int l = Integer.parseInt(dimensions[0]);
                int w = Integer.parseInt(dimensions[1]);
                int h = Integer.parseInt(dimensions[2]);
                
                int side1 = l * w;
                int side2 = w * h;
                int side3 = h * l;
                
                int smallestSide = Math.min(Math.min(side1, side2), side3);
                int surfaceArea = 2 * (side1 + side2 + side3);
                
                return surfaceArea + smallestSide;
            })
            .mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(line -> {
                String[] dimensions = line.split("x");
                int l = Integer.parseInt(dimensions[0]);
                int w = Integer.parseInt(dimensions[1]);
                int h = Integer.parseInt(dimensions[2]);
                
                // Encontrar las dos dimensiones más pequeñas para el perímetro más corto
                int[] sides = {l, w, h};
                Arrays.sort(sides);
                int smallestPerimeter = 2 * (sides[0] + sides[1]);
                
                // Calcular el volumen para el lazo
                int volume = l * w * h;
                
                return smallestPerimeter + volume;
            })
            .mapToInt(Integer::intValue)
            .sum();
    }
}
