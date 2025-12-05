package info.jab.aoc2024.day15;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//For historical reasons, I maintain this class. Only solve the Part I
public class WarehouseWoes {

    public static void main(String[] args) {
        // Input grid and moves
        char[][] warehouse = {
            "########".toCharArray(),
            "#..O.O.#".toCharArray(),
            "##@.O..#".toCharArray(),
            "#...O..#".toCharArray(),
            "#.#.O..#".toCharArray(),
            "#...O..#".toCharArray(),
            "#......#".toCharArray(),
            "########".toCharArray()
        };
        String moves = "<^^>>>vv<v>>v<<";

        int result = calculateGPSCoordinates(warehouse, moves);
        System.out.println("Sum of GPS coordinates: " + result);
    }

    public static int calculateGPSCoordinates(char[][] warehouse, String moves) {
        int rows = warehouse.length;
        int cols = warehouse[0].length;

        // Find robot's initial position
        int robotRow = -1;
        int robotCol = -1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (warehouse[r][c] == '@') {
                    robotRow = r;
                    robotCol = c;
                }
            }
        }

        // Direction vectors
        Map<Character, int[]> directions = Map.of(
            '^', new int[]{-1, 0},
            'v', new int[]{1, 0},
            '<', new int[]{0, -1},
            '>', new int[]{0, 1}
        );

        // Print the initial state of the warehouse
        System.out.println("Initial warehouse state:");
        printWarehouse(warehouse);

        // Process each move
        for (char move : moves.toCharArray()) {
            int[] dir = directions.get(move);
            int newRobotRow = robotRow + dir[0];
            int newRobotCol = robotCol + dir[1];

            boolean moved = false;

            // Check robot's intended move
            if (warehouse[newRobotRow][newRobotCol] == '#') {
                // Wall: do nothing
            } else if (warehouse[newRobotRow][newRobotCol] == 'O') {
                // Box in the way: check if all boxes can be pushed
                if (canPushBoxes(warehouse, newRobotRow, newRobotCol, dir)) {
                    // Push boxes
                    pushBoxes(warehouse, newRobotRow, newRobotCol, dir);
                    // Move robot
                    warehouse[newRobotRow][newRobotCol] = '@';
                    warehouse[robotRow][robotCol] = '.';
                    robotRow = newRobotRow;
                    robotCol = newRobotCol;
                    moved = true;
                }
            } else if (warehouse[newRobotRow][newRobotCol] == '.') {
                // Normal move
                warehouse[newRobotRow][newRobotCol] = '@';
                warehouse[robotRow][robotCol] = '.';
                robotRow = newRobotRow;
                robotCol = newRobotCol;
                moved = true;
            }

            // Print the warehouse state after every move
            System.out.println("After move: " + move);
            printWarehouse(warehouse);

            if (!moved) {
                System.out.println("(No movement occurred.)\n");
            }
        }

        // Calculate GPS coordinates
        int sumGPS = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (warehouse[r][c] == 'O') {
                    sumGPS += 100 * r + c;
                }
            }
        }

        return sumGPS;
    }

    // Helper to check if all boxes in the direction can be pushed
    private static boolean canPushBoxes(char[][] warehouse, int boxRow, int boxCol, int[] dir) {
        int rows = warehouse.length;
        int cols = warehouse[0].length;

        while (boxRow >= 0 && boxRow < rows && boxCol >= 0 && boxCol < cols) {
            if (warehouse[boxRow][boxCol] == '.') {
                // Found an empty space, all boxes can be pushed
                return true;
            }
            if (warehouse[boxRow][boxCol] == '#' || warehouse[boxRow][boxCol] == '@') {
                // Encountered a wall or the robot, can't push
                return false;
            }
            // Move to the next box
            boxRow += dir[0];
            boxCol += dir[1];
        }
        return false; // Out of bounds
    }

    // Helper to push all boxes in the direction
    private static void pushBoxes(char[][] warehouse, int boxRow, int boxCol, int[] dir) {
        List<int[]> boxes = new ArrayList<>();

        // Collect all boxes that need to be moved
        while (warehouse[boxRow][boxCol] == 'O') {
            boxes.add(new int[]{boxRow, boxCol});
            boxRow += dir[0];
            boxCol += dir[1];
        }

        // Move boxes in reverse order (from farthest to closest)
        for (int i = boxes.size() - 1; i >= 0; i--) {
            int[] box = boxes.get(i);
            int newRow = box[0] + dir[0];
            int newCol = box[1] + dir[1];
            warehouse[newRow][newCol] = 'O';
            warehouse[box[0]][box[1]] = '.';
        }
    }

    // Helper to print the warehouse
    private static void printWarehouse(char[][] warehouse) {
        for (char[] row : warehouse) {
            System.out.println(new String(row));
        }
        System.out.println();
    }
}

