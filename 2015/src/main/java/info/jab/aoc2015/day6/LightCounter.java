package info.jab.aoc2015.day6;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class LightCounter implements Solver<Long>{

    private static final String  PATTERN = "(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)";
    private static final Pattern PATTTERN_COMPILED = Pattern.compile(PATTERN);

    private static final int GRID_SIZE = 1000;

    private void turnOn(int[][] grid, Point start, Point end) {
        for (int i = start.x(); i <= end.x(); i++) {
            for (int j = start.y(); j <= end.y(); j++) {
                grid[i][j] = 1;
            }
        }
    }

    private void turnOff(int[][] grid, Point start, Point end) {
        for (int i = start.x(); i <= end.x(); i++) {
            for (int j = start.y(); j <= end.y(); j++) {
                grid[i][j] = 0;
            }
        }
    }

    private void toggle(int[][] grid, Point start, Point end) {
        for (int i = start.x(); i <= end.x(); i++) {
            for (int j = start.y(); j <= end.y(); j++) {
                grid[i][j] = (grid[i][j] == 1) ? 0 : 1;
            }
        }
    }

    private void executeCommand1(int[][] grid, CommandType command, Point start, Point end) {
        switch (command) {
            case TURN_ON -> turnOn(grid, start, end);
            case TURN_OFF -> turnOff(grid, start, end);
            case TOGGLE -> toggle(grid, start, end);
        }
    }

    private enum CommandType {
        TURN_ON("turn on"),
        TURN_OFF("turn off"),
        TOGGLE("toggle");

        private final String text;

        CommandType(String text) {
            this.text = text;
        }

        public static CommandType fromString(String text) {
            return Arrays.stream(CommandType.values())
                    .filter(cmd -> cmd.text.equals(text))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + text));
        }
    }

    private record LightCommand(CommandType command, Point start, Point end) {}

    private LightCommand parseLightCommand(String line) {
        Matcher matcher = PATTTERN_COMPILED.matcher(line);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid format: " + line);
        }
        
        return new LightCommand(
            CommandType.fromString(matcher.group(1)),
            new Point(Integer.parseInt(matcher.group(2).split(",")[0]),
                     Integer.parseInt(matcher.group(2).split(",")[1])),
            new Point(Integer.parseInt(matcher.group(3).split(",")[0]),
                     Integer.parseInt(matcher.group(3).split(",")[1])));
    }

    private void executeCommand2(int[][] grid, CommandType command, Point start, Point end) {
        for (int i = start.x(); i <= end.x(); i++) {
            for (int j = start.y(); j <= end.y(); j++) {
                switch (command) {
                    case TURN_ON -> grid[i][j]++;
                    case TURN_OFF -> grid[i][j] = grid[i][j] > 0 ? grid[i][j] - 1 : grid[i][j];
                    case TOGGLE -> grid[i][j] += 2;
                }
            }
        }
    }

    @Override
    public Long solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);

        //TODO Avoid mutability
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        lines.stream()
            .map(this::parseLightCommand)
            .forEach(cmd -> executeCommand1(grid, cmd.command(), cmd.start(), cmd.end()));

        return Arrays.stream(grid)
            .flatMapToInt(Arrays::stream)
            .filter(light -> light == 1)
            .count();
    }

    @Override
    public Long solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);

        //TODO Avoid mutability
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
 
        lines.stream()
            .map(this::parseLightCommand)
            .forEach(cmd -> executeCommand2(grid, cmd.command(), cmd.start(), cmd.end()));

        return Arrays.stream(grid)
                    .flatMapToInt(Arrays::stream)
                    .mapToLong(Long::valueOf)
                    .sum();
    }
} 