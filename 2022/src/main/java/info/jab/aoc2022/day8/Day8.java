package info.jab.aoc2022.day8;

import info.jab.aoc.Day;
import com.putoet.grid.Grid;
import com.putoet.grid.GridDirections;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day8 implements Day<Integer> {

    enum Edge {
        LEFT,
        RIGHT,
        TOP,
        BOTTON,
    }

    record Tuple(boolean isVisible, Integer distance) {}

    private Tuple getDistanceAndVisibility(Grid grid, int x, int y, Edge edge) {
        int treeHeight = Character.getNumericValue(grid.get(x, y));
        Point start = Point.of(x, y);
        
        Point direction = switch (edge) {
            case LEFT -> Point.WEST;
            case RIGHT -> Point.EAST;
            case TOP -> Point.NORTH;
            case BOTTON -> Point.SOUTH;
        };
        
        // Start scanning from the next cell (not the current one)
        Point scanStart = start.add(direction);
        
        // Scan until we hit a tree >= treeHeight or go out of bounds
        // scanDirection includes points where stopCondition is false (height < treeHeight)
        List<Point> scanned = GridDirections.scanDirection(grid, scanStart, direction, 
            c -> Character.getNumericValue(c) >= treeHeight);
        
        // Calculate where we stopped: scanStart + direction * scanned.size()
        Point stopPoint = Point.of(
            scanStart.x() + direction.x() * scanned.size(),
            scanStart.y() + direction.y() * scanned.size()
        );
        boolean hitBlocker = grid.contains(stopPoint);
        
        // Distance: count all scanned trees (height < treeHeight)
        // If we hit a blocker with height == treeHeight, include it in distance
        int distance = scanned.size();
        if (hitBlocker) {
            int blockerHeight = Character.getNumericValue(grid.get(stopPoint));
            if (blockerHeight == treeHeight) {
                distance++; // Original code counts trees with equal height
            }
        }
        
        // Tree is visible if we didn't hit a blocker (went all the way to edge)
        boolean treeVisible = !hitBlocker;

        return new Tuple(treeVisible, distance);
    }

    // @formatter:off
    private boolean isVisibleFromOutside(Grid grid, Integer x, Integer y) {
        return (getDistanceAndVisibility(grid, x, y, Edge.LEFT).isVisible()
            || getDistanceAndVisibility(grid, x, y, Edge.RIGHT).isVisible()
            || getDistanceAndVisibility(grid, x, y, Edge.TOP).isVisible()
            || getDistanceAndVisibility(grid, x, y, Edge.BOTTON).isVisible()
        );
    }

    // @formatter:on

    private Grid createGrid(String fileName) {
        var lines = ResourceLines.list("/" + fileName);
        return new Grid(GridUtils.of(lines));
    }

    @Override
    public Integer getPart1Result(String fileName) {
        //Source
        Grid grid = createGrid(fileName);

        //Transform
        //Sink
        Integer visibleTrees = 0;
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                if (isVisibleFromOutside(grid, x, y)) {
                    visibleTrees++;
                }
            }
        }
        return visibleTrees;
    }

    // @formatter:off
    private Integer scenicScore(int x, int y, Grid grid) {
        return (getDistanceAndVisibility(grid, x, y, Edge.LEFT).distance()
                * getDistanceAndVisibility(grid, x, y, Edge.RIGHT).distance()
                * getDistanceAndVisibility(grid, x, y, Edge.TOP).distance()
                * getDistanceAndVisibility(grid, x, y, Edge.BOTTON).distance()
        );
    }

    // @formatter:on

    @Override
    public Integer getPart2Result(String fileName) {
        //Source
        Grid grid = createGrid(fileName);

        //Transform
        List<Integer> scores = new ArrayList<>();
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                scores.add(scenicScore(x, y, grid));
            }
        }

        //Sink
        return Collections.max(scores);
    }
}
