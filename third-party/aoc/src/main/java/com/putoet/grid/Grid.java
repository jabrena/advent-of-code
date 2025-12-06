package com.putoet.grid;

import java.util.*;
import java.util.function.Predicate;

/**
 * A Grid represents a rectangular grid of characters. It provides methods to get and set characters, and to find
 * characters. It should be relatively easy to make it generic for any type of object, but for now it is char based.
 * The grid coordinates are adjusted based on the minimum x and y positions upon access. This means the 2d matrix
 * with the actual content is always 0-based, but the coordinates are adjusted to the minimum x and y positions.
 */
public class Grid implements GridType {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final char[][] data;

    /**
     * Create a new Grid with the given grid
     * @param grid the grid to use
     * @throws NullPointerException if the grid is null
     */
    public Grid(char[][] grid) {
        this(0, grid[0].length, 0, grid.length, grid);
    }

    /**
     * Create a new Grid with the given grid and the given minimum and maximum x and y positions
     * @param minX the minimum x position
     * @param maxX the maximum x position
     * @param minY the minimum y position
     * @param maxY the maximum y position
     * @param grid the grid to use
     * @throws NullPointerException if the grid is null
     * @throws IllegalArgumentException if the grid is not rectangular
     */
    public Grid(int minX, int maxX, int minY, int maxY, char[][] grid) {
        Objects.requireNonNull(grid);

        if (!GridUtils.isRectangular(grid))
            throw new IllegalArgumentException("Grid is not rectangular");

        if (grid.length != maxY - minY)
            throw new IllegalArgumentException("Grid length is not correct");

        for (var row : grid) {
            if (row.length != maxX - minX) {
                throw new IllegalArgumentException("Row length does not match expected width");
            }
        }

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.data = grid;
    }

    /**
     * Create a copy of this Grid
     */
    public Grid copy() {
        return new Grid(minX, maxX, minY, maxY, GridUtils.copy(data));
    }

    @Override
    public void set(int x, int y, char c) {
        if (x < minX || x >= maxX) {
            throw new IllegalArgumentException("x coordinate out of bounds: " + x);
        }
        if (y < minY || y >= maxY) {
            throw new IllegalArgumentException("y coordinate out of bounds: " + y);
        }

        data[y - minY][x - minX] = c;
    }

    @Override
    public void set(Point p, char c) {
        if (p.x() < minX || p.x() >= maxX) {
            throw new IllegalArgumentException("x coordinate out of bounds: " + p.x());
        }
        if (p.y() < minY || p.y() >= maxY) {
            throw new IllegalArgumentException("y coordinate out of bounds: " + p.y());
        }

        data[p.y() - minY][p.x() - minX] = c;
    }

    @Override
    public char get(int x, int y) {
        if (x < minX || x >= maxX) {
            throw new IllegalArgumentException("x coordinate out of bounds: " + x);
        }
        if (y < minY || y >= maxY) {
            throw new IllegalArgumentException("y coordinate out of bounds: " + y);
        }

        return data[y - minY][x - minX];
    }

    @Override
    public char get(Point p) {
        if (p.x() < minX || p.x() >= maxX) {
            throw new IllegalArgumentException("x coordinate out of bounds: " + p.x());
        }
        if (p.y() < minY || p.y() >= maxY) {
            throw new IllegalArgumentException("y coordinate out of bounds: " + p.y());
        }

        return data[p.y() - minY][p.x() - minX];
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    @Override
    public boolean contains(Point p) {
        return p.x() >= minX && p.x() < maxX && p.y() >= minY && p.y() < maxY;
    }

    @Override
    public int minX() {
        return minX;
    }

    @Override
    public int maxX() {
        return maxX;
    }

    @Override
    public int minY() {
        return minY;
    }

    @Override
    public int maxY() {
        return maxY;
    }

    @Override
    public int width() {
        return Math.abs(maxX - minX);
    }

    @Override
    public int height() {
        return Math.abs(maxY - minY);
    }

    /**
     * Get the grid as a 2d array of characters
     * @return the grid as a 2d array of characters
     */
    public char[][] grid() {
        return data;
    }

    /**
     * Return a copy of this Grid which is flipped horizontally
     * @return a copy of this Grid which is flipped horizontally
     */
    public Grid flipHorizontally() {
        return new Grid(minX, maxX, minY, maxY, GridUtils.flipHorizontally(data));
    }

    /**
     * Return a copy of this Grid which is flipped vertically
     * @return a copy of this Grid which is flipped vertically
     */
    public Grid flipVertically() {
        return new Grid(minX, maxX, minY, maxY, GridUtils.flipVertically(data));
    }

    /**
     * Return a copy of this Grid which is rotated 90 degrees clockwise
     * @return a copy of this Grid which is rotated 90 degrees clockwise
     */
    public Grid rotate() {
        //noinspection SuspiciousNameCombination
        return new Grid(minY, maxY, minX, maxX, GridUtils.rotate(data));
    }

    /**
     * Count how often he given character is present in the grid
     * @return the number of times the given character is present in the grid
     */
    public long count(char toCount) {
        return GridUtils.count(data, toCount);
    }

    /**
     * Count how often the given predicate is true for the characters in the grid
     * @param filter the predicate to use, must not be null
     * @return the number of times the given predicate is true for the characters in the grid
     */
    public long count(Predicate<Integer> filter) {
        Objects.requireNonNull(filter);

        return GridUtils.count(data, filter);
    }

    @Override
    public Optional<Point> findFirst(Predicate<Character> predicate) {
        for (var y = minY; y < maxY; y++)
            for (var x = minX; x < maxX; x++)
                if (predicate.test(data[y][x]))
                    return Optional.of(Point.of(x, y));

        return Optional.empty();
    }

    @Override
    public List<Point> findAll(Predicate<Character> predicate) {
        final var found = new ArrayList<Point>();

        for (var y = minY; y < maxY; y++)
            for (var x = minX; x < maxX; x++)
                if (predicate.test(data[y][x]))
                    found.add(Point.of(x, y));

        return found;
    }

    /**
     * Get all valid neighbor points from the given point using cardinal directions only (4 directions).
     * @param p the point to get neighbors from, must not be null
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public List<Point> getNeighbors(Point p) {
        return getNeighbors(p, false);
    }

    /**
     * Get all valid neighbor points from the given point.
     * @param p the point to get neighbors from, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public List<Point> getNeighbors(Point p, boolean includeDiagonals) {
        Objects.requireNonNull(p);
        final var directions = includeDiagonals ? Points.directionsAll() : Points.directionsSquare();
        return p.adjacent(directions).stream()
                .filter(this::contains)
                .toList();
    }

    /**
     * Get all valid neighbor points from the given coordinates using cardinal directions only (4 directions).
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public List<Point> getNeighbors(int x, int y) {
        return getNeighbors(Point.of(x, y), false);
    }

    /**
     * Get all valid neighbor points from the given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public List<Point> getNeighbors(int x, int y, boolean includeDiagonals) {
        return getNeighbors(Point.of(x, y), includeDiagonals);
    }

    /**
     * Get neighbor points using specific directions.
     * @param p the point to get neighbors from, must not be null
     * @param directions the directions to use, must not be null
     * @return a list of valid neighbor points that are within the grid bounds
     */
    public List<Point> getNeighbors(Point p, List<Point> directions) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(directions);
        return p.adjacent(directions).stream()
                .filter(this::contains)
                .toList();
    }

    /**
     * Count neighbors matching the given condition using cardinal directions only (4 directions).
     * @param p the point to count neighbors from, must not be null
     * @param condition the condition to test each neighbor, must not be null
     * @return the number of neighbors matching the condition
     */
    public int countNeighbors(Point p, Predicate<Character> condition) {
        return countNeighbors(p, condition, false);
    }

    /**
     * Count neighbors matching the given condition.
     * @param p the point to count neighbors from, must not be null
     * @param condition the condition to test each neighbor, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return the number of neighbors matching the condition
     */
    public int countNeighbors(Point p, Predicate<Character> condition, boolean includeDiagonals) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(condition);
        return (int) getNeighbors(p, includeDiagonals).stream()
                .mapToInt(neighbor -> condition.test(get(neighbor)) ? 1 : 0)
                .sum();
    }

    /**
     * Count neighbors matching the given condition using cardinal directions only (4 directions).
     * @param x the x coordinate
     * @param y the y coordinate
     * @param condition the condition to test each neighbor, must not be null
     * @return the number of neighbors matching the condition
     */
    public int countNeighbors(int x, int y, Predicate<Character> condition) {
        return countNeighbors(Point.of(x, y), condition, false);
    }

    /**
     * Count neighbors matching the given condition.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param condition the condition to test each neighbor, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return the number of neighbors matching the condition
     */
    public int countNeighbors(int x, int y, Predicate<Character> condition, boolean includeDiagonals) {
        return countNeighbors(Point.of(x, y), condition, includeDiagonals);
    }

    /**
     * Get the values of all valid neighbors using cardinal directions only (4 directions).
     * @param p the point to get neighbor values from, must not be null
     * @return a list of neighbor cell values
     */
    public List<Character> getNeighborValues(Point p) {
        return getNeighborValues(p, false);
    }

    /**
     * Get the values of all valid neighbors.
     * @param p the point to get neighbor values from, must not be null
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of neighbor cell values
     */
    public List<Character> getNeighborValues(Point p, boolean includeDiagonals) {
        Objects.requireNonNull(p);
        return getNeighbors(p, includeDiagonals).stream()
                .map(this::get)
                .toList();
    }

    /**
     * Get the values of all valid neighbors using cardinal directions only (4 directions).
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a list of neighbor cell values
     */
    public List<Character> getNeighborValues(int x, int y) {
        return getNeighborValues(Point.of(x, y), false);
    }

    /**
     * Get the values of all valid neighbors.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param includeDiagonals if true, includes diagonal neighbors (8 directions), otherwise only cardinal (4 directions)
     * @return a list of neighbor cell values
     */
    public List<Character> getNeighborValues(int x, int y, boolean includeDiagonals) {
        return getNeighborValues(Point.of(x, y), includeDiagonals);
    }

    /**
     * Scan in a direction from the given point until a stop condition is met or the grid boundary is reached.
     * @param start the starting point, must not be null
     * @param direction the direction to scan in (as a Point vector), must not be null
     * @param stopCondition the condition that stops the scan, must not be null
     * @return a list of points scanned, including the start point up to (but not including) the stop point
     */
    public List<Point> scanDirection(Point start, Point direction, Predicate<Character> stopCondition) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(stopCondition);
        
        final var result = new ArrayList<Point>();
        Point current = start;
        
        while (contains(current)) {
            if (stopCondition.test(get(current))) {
                break;
            }
            result.add(current);
            current = current.add(direction);
        }
        
        return result;
    }

    /**
     * Scan in a direction from the given coordinates until a stop condition is met or the grid boundary is reached.
     * @param startX the starting x coordinate
     * @param startY the starting y coordinate
     * @param direction the direction to scan in (as a Point vector), must not be null
     * @param stopCondition the condition that stops the scan, must not be null
     * @return a list of points scanned, including the start point up to (but not including) the stop point
     */
    public List<Point> scanDirection(int startX, int startY, Point direction, Predicate<Character> stopCondition) {
        return scanDirection(Point.of(startX, startY), direction, stopCondition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grid grid = (Grid) obj;
        return minX == grid.minX && maxX == grid.maxX && minY == grid.minY && maxY == grid.maxY
                && Arrays.deepEquals(data, grid.data);
    }

    @Override
    public int hashCode() {
        var result = Objects.hash(minX, maxX, minY, maxY);
        result = 31 * result + Arrays.deepHashCode(data);
        return result;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        sb.append(String.format("(%d,%d)..(%d,%d)%n", minX, minY, maxX, maxY));
        sb.append(" ".repeat(3));
        for (var i = minX; i < maxX / 10 + 1; i++) {
            sb.append(i);
            sb.append(" ".repeat(9));
        }

        sb.append(System.lineSeparator()).append(" ".repeat(3));
        for (var i = minX; i < maxX; i++)
            sb.append(i % 10);
        sb.append(System.lineSeparator());

        final var list = GridUtils.toList(data);
        for (var i = 0; i < list.size(); i++)
            sb.append(String.format("%02d ", i % 100)).append(list.get(i)).append(System.lineSeparator());

        return sb.toString();
    }
}
