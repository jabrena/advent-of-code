package com.putoet.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Utility class for direction-based operations on grids.
 * Provides methods for getting neighbors, counting neighbors, and scanning in directions.
 */
public final class GridDirections {
    private GridDirections() {
        // Utility class - prevent instantiation
    }

    /**
     * Get all valid neighbor points from the given point using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param p the point to get neighbors from, must not be null
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public static List<Point> getNeighbors(GridType grid, Point p) {
        return getNeighbors(grid, p, false);
    }

    /**
     * Get all valid neighbor points from the given point.
     * @param grid the grid to operate on, must not be null
     * @param p the point to get neighbors from, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public static List<Point> getNeighbors(GridType grid, Point p, boolean includeDiagonals) {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(p);
        final var directions = includeDiagonals ? Points.directionsAll() : Points.directionsSquare();
        return p.adjacent(directions).stream()
                .filter(grid::contains)
                .toList();
    }

    /**
     * Get all valid neighbor points from the given coordinates using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public static List<Point> getNeighbors(GridType grid, int x, int y) {
        return getNeighbors(grid, Point.of(x, y), false);
    }

    /**
     * Get all valid neighbor points from the given coordinates.
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public static List<Point> getNeighbors(GridType grid, int x, int y, boolean includeDiagonals) {
        return getNeighbors(grid, Point.of(x, y), includeDiagonals);
    }

    /**
     * Get neighbor points using specific directions.
     * @param grid the grid to operate on, must not be null
     * @param p the point to get neighbors from, must not be null
     * @param directions the directions to use, must not be null
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public static List<Point> getNeighbors(GridType grid, Point p, List<Point> directions) {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(p);
        Objects.requireNonNull(directions);
        return p.adjacent(directions).stream()
                .filter(grid::contains)
                .toList();
    }

    /**
     * Count neighbors matching the given condition using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param p the point to count neighbors from, must not be null
     * @param condition the condition to test each neighbor, must not be null
     * @return the number of neighbors matching the condition
     */
    public static int countNeighbors(GridType grid, Point p, Predicate<Character> condition) {
        return countNeighbors(grid, p, condition, false);
    }

    /**
     * Count neighbors matching the given condition.
     * @param grid the grid to operate on, must not be null
     * @param p the point to count neighbors from, must not be null
     * @param condition the condition to test each neighbor, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return the number of neighbors matching the condition
     */
    public static int countNeighbors(GridType grid, Point p, Predicate<Character> condition, boolean includeDiagonals) {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(p);
        Objects.requireNonNull(condition);
        return (int) getNeighbors(grid, p, includeDiagonals).stream()
                .mapToInt(neighbor -> condition.test(grid.get(neighbor)) ? 1 : 0)
                .sum();
    }

    /**
     * Count neighbors matching the given condition using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param condition the condition to test each neighbor, must not be null
     * @return the number of neighbors matching the condition
     */
    public static int countNeighbors(GridType grid, int x, int y, Predicate<Character> condition) {
        return countNeighbors(grid, Point.of(x, y), condition, false);
    }

    /**
     * Count neighbors matching the given condition.
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param condition the condition to test each neighbor, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return the number of neighbors matching the condition
     */
    public static int countNeighbors(GridType grid, int x, int y, Predicate<Character> condition, boolean includeDiagonals) {
        return countNeighbors(grid, Point.of(x, y), condition, includeDiagonals);
    }

    /**
     * Get the values of all valid neighbors using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param p the point to get neighbor values from, must not be null
     * @return a list of neighbor cell values
     */
    public static List<Character> getNeighborValues(GridType grid, Point p) {
        return getNeighborValues(grid, p, false);
    }

    /**
     * Get the values of all valid neighbors.
     * @param grid the grid to operate on, must not be null
     * @param p the point to get neighbor values from, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of neighbor cell values
     */
    public static List<Character> getNeighborValues(GridType grid, Point p, boolean includeDiagonals) {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(p);
        return getNeighbors(grid, p, includeDiagonals).stream()
                .map(grid::get)
                .toList();
    }

    /**
     * Get the values of all valid neighbors using cardinal directions only (4 directions).
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a list of neighbor cell values
     */
    public static List<Character> getNeighborValues(GridType grid, int x, int y) {
        return getNeighborValues(grid, Point.of(x, y), false);
    }

    /**
     * Get the values of all valid neighbors.
     * @param grid the grid to operate on, must not be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of neighbor cell values
     */
    public static List<Character> getNeighborValues(GridType grid, int x, int y, boolean includeDiagonals) {
        return getNeighborValues(grid, Point.of(x, y), includeDiagonals);
    }

    /**
     * Scan in a direction from the given point until a stop condition is met or the grid boundary is reached.
     * @param grid the grid to operate on, must not be null
     * @param start the starting point, must not be null
     * @param direction the direction to scan in (as a Point vector), must not be null
     * @param stopCondition the condition that stops the scan, must not be null
     * @return a list of points scanned, including the start point up to (but not including) the stop point
     */
    public static List<Point> scanDirection(GridType grid, Point start, Point direction, Predicate<Character> stopCondition) {
        Objects.requireNonNull(grid);
        Objects.requireNonNull(start);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(stopCondition);
        
        final var result = new ArrayList<Point>();
        Point current = start;
        
        while (grid.contains(current)) {
            if (stopCondition.test(grid.get(current))) {
                break;
            }
            result.add(current);
            current = current.add(direction);
        }
        
        return result;
    }

    /**
     * Scan in a direction from the given coordinates until a stop condition is met or the grid boundary is reached.
     * @param grid the grid to operate on, must not be null
     * @param startX the starting x coordinate
     * @param startY the starting y coordinate
     * @param direction the direction to scan in (as a Point vector), must not be null
     * @param stopCondition the condition that stops the scan, must not be null
     * @return a list of points scanned, including the start point up to (but not including) the stop point
     */
    public static List<Point> scanDirection(GridType grid, int startX, int startY, Point direction, Predicate<Character> stopCondition) {
        return scanDirection(grid, Point.of(startX, startY), direction, stopCondition);
    }
}
