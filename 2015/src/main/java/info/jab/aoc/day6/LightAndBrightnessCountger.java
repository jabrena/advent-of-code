package info.jab.aoc.day6;

import java.util.function.BiConsumer;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class LightAndBrightnessCountger implements Solver<Long>{

    private static final int GRID_SIZE = 1000;
    private int[][] grid2;

    public LightAndBrightnessCountger() {
        grid2 = new int[GRID_SIZE][GRID_SIZE];
    }

    public void processInstructionsPart2(String instruction) {
        String[] parts = instruction.split(" ");
        if (instruction.startsWith("turn on")) {
            processCoordinates(parts[2], parts[4], (x, y) -> grid2[x][y]++);
        } else if (instruction.startsWith("turn off")) {
            processCoordinates(parts[2], parts[4], (x, y) -> {
                if (grid2[x][y] > 0) {
                    grid2[x][y]--;
                }
            });
        } else if (instruction.startsWith("toggle")) {
            processCoordinates(parts[1], parts[3], (x, y) -> grid2[x][y] += 2);
        }
    }

    private void processCoordinates(String start, String end, BiConsumer<Integer, Integer> operation) {
        String[] startCoord = start.split(",");
        String[] endCoord = end.split(",");
        
        int startX = Integer.parseInt(startCoord[0]);
        int startY = Integer.parseInt(startCoord[1]);
        int endX = Integer.parseInt(endCoord[0]);
        int endY = Integer.parseInt(endCoord[1]);
        
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                operation.accept(x, y);
            }
        }
    }

    public long getTotalBrightness() {
        long totalBrightness = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                totalBrightness += grid2[i][j];
            }
        }
        return totalBrightness;
    }

    @Override
    public Long solvePartOne(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solvePartTwo'");
    }

    @Override
    public Long solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);

        for (String line : lines) {
            processInstructionsPart2(line);
        }

        return getTotalBrightness();
    }

}
