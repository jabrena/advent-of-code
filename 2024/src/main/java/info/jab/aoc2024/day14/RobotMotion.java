package info.jab.aoc2024.day14;

import java.util.List;
import java.util.regex.Pattern;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

public class RobotMotion {

    private List<Robot> parseRobots(String fileName) {
        return ResourceLines.list(fileName).stream()
                .map(Robot::of)
                .toList();
    }

    // Simulate the robot motion for a given time and return a new grid state
    private Grid simulateRobotMotion(List<Robot> robots, int width, int height, int time) {
        Grid grid = new Grid(GridUtils.of(0, width, 0, height, '.'));

        for (Robot robot : robots) {
            int finalX = (robot.x() + robot.vx() * time) % width;
            int finalY = (robot.y() + robot.vy() * time) % height;

            // Handle wrapping (ensure positive coordinates)
            if (finalX < 0) finalX += width;
            if (finalY < 0) finalY += height;

            // Set the position (using '#' to mark robot presence)
            char current = grid.get(finalX, finalY);
            if (current == '.') {
                grid.set(finalX, finalY, '1');
            } else {
                grid.set(finalX, finalY, (char)(current + 1));
            }
        }
        return grid;
    }

    // Calculate the safety factor by counting robots in quadrants
    private int calculateSafetyFactor(Grid grid, int width, int height) {
        int midX = width / 2;
        int midY = height / 2;
        int[] quadrants = new int[4];

        for (int x = grid.minX(); x < grid.maxX(); x++) {
            for (int y = grid.minY(); y < grid.maxY(); y++) {
                char cell = grid.get(x, y);
                if (cell != '.') {
                    int count = Character.isDigit(cell) ? Character.getNumericValue(cell) : 1;
                    if (x > midX && y < midY) quadrants[0] += count;
                    else if (x < midX && y < midY) quadrants[1] += count;
                    else if (x < midX && y > midY) quadrants[2] += count;
                    else if (x > midX && y > midY) quadrants[3] += count;
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
    private boolean detectPattern(Grid grid) {
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                row.append(grid.get(x, y) != '.' ? "#" : ".");
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
            if (detectPattern(grid)) {
                break;
            }
            time++;
        }
        return time;
    }
}
