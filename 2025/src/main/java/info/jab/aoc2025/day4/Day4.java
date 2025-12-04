package info.jab.aoc2025.day4;

import com.putoet.grid.GridUtils;
import info.jab.aoc.Day;
import info.jab.aoc.Utils;
import java.util.List;

public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> lines = Utils.readFileToList(fileName).stream()
                .filter(line -> !line.isEmpty())
                .toList();
        char[][] grid = GridUtils.of(lines);
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == '@') {
                    if (countNeighbors(grid, x, y, rows, cols) < 4) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int countNeighbors(char[][] grid, int x, int y, int rows, int cols) {
        int neighbors = 0;
        int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
                if (grid[ny][nx] == '@') {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
