package info.jab.aoc2016.day13;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.*;

/**
 * Solves the maze pathfinding problem for AoC 2016 Day 13
 */
public class MazePathfinder implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        int favoriteNumber = Integer.parseInt(lines.getFirst().trim());
        
        // Find shortest path from (1,1) to (31,39)
        return findShortestPath(1, 1, 31, 39, favoriteNumber);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        int favoriteNumber = Integer.parseInt(lines.getFirst().trim());
        
        // Count locations reachable in at most 50 steps from (1,1)
        return countReachableLocations(1, 1, 50, favoriteNumber);
    }

    /**
     * Determines if a coordinate is an open space or wall
     */
    private boolean isOpenSpace(int x, int y, int favoriteNumber) {
        if (x < 0 || y < 0) {
            return false; // Invalid coordinates
        }
        
        // Calculate: x*x + 3*x + 2*x*y + y + y*y + favoriteNumber
        int value = x * x + 3 * x + 2 * x * y + y + y * y + favoriteNumber;
        
        // Count number of 1 bits
        int bitCount = Integer.bitCount(value);
        
        // Even number of 1s = open space, odd = wall
        return bitCount % 2 == 0;
    }

    /**
     * Find shortest path using BFS
     */
    private int findShortestPath(int startX, int startY, int targetX, int targetY, int favoriteNumber) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        
        Point start = new Point(startX, startY, 0);
        queue.offer(start);
        visited.add(new Point(startX, startY, 0));
        
        // Directions: up, down, left, right
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            
            // Check if we reached the target
            if (current.x == targetX && current.y == targetY) {
                return current.steps;
            }
            
            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];
                Point neighbor = new Point(newX, newY, 0);
                
                if (newX >= 0 && newY >= 0 && 
                    !visited.contains(neighbor) && 
                    isOpenSpace(newX, newY, favoriteNumber)) {
                    
                    visited.add(neighbor);
                    queue.offer(new Point(newX, newY, current.steps + 1));
                }
            }
        }
        
        return -1; // Path not found
    }

    /**
     * Count all locations reachable in at most maxSteps steps using BFS
     */
    private int countReachableLocations(int startX, int startY, int maxSteps, int favoriteNumber) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        
        Point start = new Point(startX, startY, 0);
        queue.offer(start);
        visited.add(new Point(startX, startY, 0));
        
        // Directions: up, down, left, right
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            
            // If we've reached the maximum steps, don't explore further from this point
            if (current.steps >= maxSteps) {
                continue;
            }
            
            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];
                Point neighbor = new Point(newX, newY, 0);
                
                if (newX >= 0 && newY >= 0 && 
                    !visited.contains(neighbor) && 
                    isOpenSpace(newX, newY, favoriteNumber)) {
                    
                    visited.add(neighbor);
                    queue.offer(new Point(newX, newY, current.steps + 1));
                }
            }
        }
        
        return visited.size();
    }

    /**
     * Represents a point in the maze
     */
    private static class Point {
        final int x;
        final int y;
        final int steps;
        
        Point(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}