package info.jab.aoc.day6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.grid.Grid;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class LightCounter implements Solver<Long>{

    private static final Pattern COMMAND_PATTERN = Pattern.compile("(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)");

    private static final int GRID_SIZE = 1000;
    private Grid grid;

    public LightCounter() {
        grid = new Grid(new char[GRID_SIZE][GRID_SIZE]);
    }

    private void turnOn(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid.set(i, j, '1');
            }
        }
    }

    private void turnOff(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid.set(i, j, '0');
            }
        }
    }

    private void toggle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                char currentValue = grid.get(i, j);
                grid.set(i, j, currentValue == '1' ? '0' : '1');
            }
        }
    }

    private int getNumberOfLightsOn() {
        int count = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid.get(i, j) == '1') count++;
            }
        }
        return count;
    }

    private void executeCommand(String command, int x1, int y1, int x2, int y2) {
        switch (command.toLowerCase().trim()) {
            case "turn on":
                turnOn(x1, y1, x2, y2);
                break;
            case "turn off":
                turnOff(x1, y1, x2, y2);
                break;
            case "toggle":
                toggle(x1, y1, x2, y2);
                break;
            default:
                throw new IllegalArgumentException("Comando no válido: " + command);
        }
    }

    @Override
    public Long solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);

        LightCounter libGrid = new LightCounter();

        for (String line : lines) {
            Matcher matcher = COMMAND_PATTERN.matcher(line);
            if (matcher.find()) {
                String command = matcher.group(1);      // "turn off", "turn on", o "toggle"
                String start = matcher.group(2);        // "301,3"
                String end = matcher.group(3); 
                var startParts = start.split(",");
                var endParts = end.split(",");
                libGrid.executeCommand(command, 
                    Integer.parseInt(startParts[0]), Integer.parseInt(startParts[1]), 
                    Integer.parseInt(endParts[0]), Integer.parseInt(endParts[1]));
            }
        }

        return 0L + libGrid.getNumberOfLightsOn();
    }

    @Override
    public Long solvePartTwo(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solvePartTwo'");
    }
} 