package info.jab.aoc.day6;

public class LightGrid {
    private boolean[][] grid;
    private static final int GRID_SIZE = 1000;

    public LightGrid() {
        grid = new boolean[GRID_SIZE][GRID_SIZE];
        // Inicialmente todas las luces están apagadas (false)
    }

    public void turnOn(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid[i][j] = true;
            }
        }
    }

    public void turnOff(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid[i][j] = false;
            }
        }
    }

    public void toggle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                grid[i][j] = !grid[i][j];
            }
        }
    }

    public int getNumberOfLightsOn() {
        int count = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j]) count++;
            }
        }
        return count;
    }
} 