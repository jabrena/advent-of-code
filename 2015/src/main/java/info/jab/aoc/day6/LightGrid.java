package info.jab.aoc.day6;

import com.putoet.grid.Grid;

import java.util.function.BiConsumer;

public class LightGrid {

    private static final int GRID_SIZE = 1000;
    private int[][] grid2;
    private Grid grid3;

    public LightGrid() {
        grid2 = new int[GRID_SIZE][GRID_SIZE];
        grid3 = new Grid(new char[GRID_SIZE][GRID_SIZE]);
    }

    public void turnOn(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid3.set(i, j, '1');
            }
        }
    }

    public void turnOff(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid3.set(i, j, '0');
            }
        }
    }

    public void toggle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                char currentValue = grid3.get(i, j);
                grid3.set(i, j, currentValue == '1' ? '0' : '1');
            }
        }
    }

    public int getNumberOfLightsOn() {
        int count = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid3.get(i, j) == '1') count++;
            }
        }
        return count;
    }

    //Part 2

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
} 