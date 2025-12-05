package info.jab.aoc2024.day14;

import java.util.List;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;

public class RobotMotion {

    private List<Robot> parseRobots(String fileName) {
        return ResourceLines.list(fileName).stream()
                .map(Robot::of)
                .toList();
    }

    // Simulate the robot motion for a given time and return a new grid state
    private int[][] simulateRobotMotion(List<Robot> robots, int width, int height, int time) {
        int[][] grid = new int[width][height];

        for (Robot robot : robots) {
            int finalX = (robot.x() + robot.vx() * time) % width;
            int finalY = (robot.y() + robot.vy() * time) % height;

            // Handle wrapping (ensure positive coordinates)
            if (finalX < 0) finalX += width;
            if (finalY < 0) finalY += height;

            // Increment the count at the resulting position
            grid[finalX][finalY]++;
        }
        return grid;
    }

    // Calculate the safety factor by counting robots in quadrants
    private int calculateSafetyFactor(int[][] grid, int width, int height) {
        int midX = width / 2;
        int midY = height / 2;
        int[] quadrants = new int[4];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] > 0) {
                    if (x > midX && y < midY) quadrants[0] += grid[x][y];
                    else if (x < midX && y < midY) quadrants[1] += grid[x][y];
                    else if (x < midX && y > midY) quadrants[2] += grid[x][y];
                    else if (x > midX && y > midY) quadrants[3] += grid[x][y];
                }
            }
        }

        return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
    }

    public Integer part1(String fileName, Integer width, Integer height) {
        List<Robot> robots = parseRobots(fileName);
        var grid = simulateRobotMotion(robots, width, height, 100);
        return calculateSafetyFactor(grid, width, height);
    }

    private static final int TIME_LIMIT = 10000;


    private static final String PATTERN = "###############################";
    private static final Pattern COMPILED_PATTERN = Pattern.compile(Pattern.quote(PATTERN));

    // Detect the Christmas tree pattern in the grid
    private boolean detectPattern(int[][] grid, int width, int height) {
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(grid[x][y] > 0 ? "#" : ".");
            }
            if (COMPILED_PATTERN.matcher(row.toString()).find()) {
                return true;
            }
        }
        return false;
    }

    public Integer part2(String fileName, Integer width, Integer height) {
        List<Robot> robots = parseRobots(fileName);

        int time = 0;
        while (time <= TIME_LIMIT) {
            var grid = simulateRobotMotion(robots, width, height, time);

            // Check for the Christmas tree pattern
            if (detectPattern(grid, width, height)) {
                break;
            }
            time++;
        }
        return time;
    }
}
