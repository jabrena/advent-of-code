package info.jab.aoc2015.day2;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/2
 */
public class Present2 implements Solver<Integer> {

    private static final Pattern DIMENSIONS_PATTERN = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");

    private record Dimensions(int length, int width, int height) {
        static Dimensions from(String line) {
            Matcher matcher = DIMENSIONS_PATTERN.matcher(line);
            
            if (matcher.matches()) {
                return new Dimensions(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
                );
            }
            throw new IllegalArgumentException("Formato inválido: " + line);
        }
        
        int calculateWrappingPaper() {
            int side1 = length * width;
            int side2 = width * height;
            int side3 = height * length;
            
            int smallestSide = Math.min(Math.min(side1, side2), side3);
            int surfaceArea = 2 * (side1 + side2 + side3);
            
            return surfaceArea + smallestSide;
        }

        int calculateRibbon() {
            int[] sides = {length, width, height};
            Arrays.sort(sides);
            int smallestPerimeter = 2 * (sides[0] + sides[1]);
            int volume = length * width * height;
            return smallestPerimeter + volume;
        }
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(Dimensions::from)
            .map(Dimensions::calculateWrappingPaper)
            .mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(Dimensions::from)
            .map(Dimensions::calculateRibbon)
            .mapToInt(Integer::intValue)
            .sum();
    }
}
