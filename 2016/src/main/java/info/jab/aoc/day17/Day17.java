package info.jab.aoc.day17;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import com.putoet.security.MD5;

import java.util.*;

/**
 * https://adventofcode.com/2016/day/17
 * 
 * Day 17: Two Steps Forward
 * 
 * Navigate a 4x4 grid vault using MD5 hash-based door states.
 * Doors are open if the corresponding character in the MD5 hash is b, c, d, e, or f.
 */
public class Day17 implements Day<String> {

    private static final int GRID_SIZE = 4;
    private static final int TARGET_X = 3;
    private static final int TARGET_Y = 3;

    @Override
    public String getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String passcode = lines.get(0).trim();
        return findShortestPath(passcode);
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String passcode = lines.get(0).trim();
        return String.valueOf(findLongestPathLength(passcode));
    }

    /**
     * Find the shortest path to the vault using BFS
     */
    private String findShortestPath(String passcode) {
        Queue<State> queue = new LinkedList<>();
        queue.offer(new State(0, 0, ""));

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Check if we reached the target
            if (current.x == TARGET_X && current.y == TARGET_Y) {
                return current.path;
            }

            // Get open doors based on MD5 hash
            String hash = MD5.hash(passcode + current.path).toLowerCase();
            boolean[] doors = getOpenDoors(hash, current.x, current.y);

            // Try moving in each direction
            if (doors[0] && current.y > 0) { // Up
                queue.offer(new State(current.x, current.y - 1, current.path + "U"));
            }
            if (doors[1] && current.y < GRID_SIZE - 1) { // Down
                queue.offer(new State(current.x, current.y + 1, current.path + "D"));
            }
            if (doors[2] && current.x > 0) { // Left
                queue.offer(new State(current.x - 1, current.y, current.path + "L"));
            }
            if (doors[3] && current.x < GRID_SIZE - 1) { // Right
                queue.offer(new State(current.x + 1, current.y, current.path + "R"));
            }
        }

        return ""; // No path found
    }

    /**
     * Find the length of the longest path to the vault using DFS
     */
    private int findLongestPathLength(String passcode) {
        return findLongestPathDFS(passcode, 0, 0, "");
    }

    private int findLongestPathDFS(String passcode, int x, int y, String path) {
        // Check if we reached the target
        if (x == TARGET_X && y == TARGET_Y) {
            return path.length();
        }

        // Get open doors based on MD5 hash
        String hash = MD5.hash(passcode + path).toLowerCase();
        boolean[] doors = getOpenDoors(hash, x, y);

        int maxLength = -1;

        // Try moving in each direction
        if (doors[0] && y > 0) { // Up
            int length = findLongestPathDFS(passcode, x, y - 1, path + "U");
            if (length > maxLength) {
                maxLength = length;
            }
        }
        if (doors[1] && y < GRID_SIZE - 1) { // Down
            int length = findLongestPathDFS(passcode, x, y + 1, path + "D");
            if (length > maxLength) {
                maxLength = length;
            }
        }
        if (doors[2] && x > 0) { // Left
            int length = findLongestPathDFS(passcode, x - 1, y, path + "L");
            if (length > maxLength) {
                maxLength = length;
            }
        }
        if (doors[3] && x < GRID_SIZE - 1) { // Right
            int length = findLongestPathDFS(passcode, x + 1, y, path + "R");
            if (length > maxLength) {
                maxLength = length;
            }
        }

        return maxLength;
    }

    /**
     * Determine which doors are open based on the MD5 hash
     * Returns an array of 4 booleans: [up, down, left, right]
     * A door is open if the corresponding character is b, c, d, e, or f
     */
    private boolean[] getOpenDoors(String hash, int x, int y) {
        boolean[] doors = new boolean[4];
        
        if (hash.length() >= 4) {
            doors[0] = isOpen(hash.charAt(0)); // Up
            doors[1] = isOpen(hash.charAt(1)); // Down
            doors[2] = isOpen(hash.charAt(2)); // Left
            doors[3] = isOpen(hash.charAt(3)); // Right
        }
        
        return doors;
    }

    /**
     * Check if a door is open (character is b, c, d, e, or f)
     */
    private boolean isOpen(char c) {
        return c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
    }

    /**
     * Represents a state in the vault navigation
     */
    private static class State {
        final int x;
        final int y;
        final String path;

        State(int x, int y, String path) {
            this.x = x;
            this.y = y;
            this.path = path;
        }
    }
}
