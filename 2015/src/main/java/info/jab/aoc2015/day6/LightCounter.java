package info.jab.aoc2015.day6;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class LightCounter implements Solver<Long> {

    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking with alternation patterns.
     */
    private static final int MAX_INPUT_LENGTH = 10_000;

    private static final String PATTERN = "(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)";
    private static final Pattern PATTERN_COMPILED = Pattern.compile(PATTERN);

    private static final int GRID_SIZE = 1000;

    private LightCommand parseLightCommand(String line) {
        if (line.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
        }
        
        Matcher matcher = PATTERN_COMPILED.matcher(line);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid format: " + line);
        }

        String[] startCoords = matcher.group(2).split(",");
        String[] endCoords = matcher.group(3).split(",");

        return new LightCommand(
            CommandType.fromString(matcher.group(1)),
            new Point(Integer.parseInt(startCoords[0]), Integer.parseInt(startCoords[1])),
            new Point(Integer.parseInt(endCoords[0]), Integer.parseInt(endCoords[1])));
    }

    private void applyCommand(int[][] grid, Point start, Point end, IntUnaryOperator operation) {
        for (int i = start.x(); i <= end.x(); i++) {
            for (int j = start.y(); j <= end.y(); j++) {
                grid[i][j] = operation.applyAsInt(grid[i][j]);
            }
        }
    }

    private IntUnaryOperator getPart1Operation(CommandType command) {
        return switch (command) {
            case TURN_ON -> value -> 1;
            case TURN_OFF -> value -> 0;
            case TOGGLE -> value -> (value == 1) ? 0 : 1;
        };
    }

    private IntUnaryOperator getPart2Operation(CommandType command) {
        return switch (command) {
            case TURN_ON -> value -> value + 1;
            case TURN_OFF -> value -> Math.max(0, value - 1);
            case TOGGLE -> value -> value + 2;
        };
    }

    @Override
    public Long solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        lines.stream()
            .map(this::parseLightCommand)
            .forEach(cmd -> applyCommand(
                grid,
                cmd.start(),
                cmd.end(),
                getPart1Operation(cmd.command())));

        return Arrays.stream(grid)
            .flatMapToInt(Arrays::stream)
            .filter(light -> light == 1)
            .count();
    }

    @Override
    public Long solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        lines.stream()
            .map(this::parseLightCommand)
            .forEach(cmd -> applyCommand(
                grid,
                cmd.start(),
                cmd.end(),
                getPart2Operation(cmd.command())));

        return Arrays.stream(grid)
            .flatMapToInt(Arrays::stream)
            .mapToLong(Long::valueOf)
            .sum();
    }
}
