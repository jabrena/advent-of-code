package info.jab.aoc2022.day8;

import info.jab.aoc.Day;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
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
        boolean treeVisible = true;
        int distance = 0;

        //Adjustment
        int dx = switch (edge) {
            case LEFT -> -1;
            case RIGHT -> 1;
            case TOP -> 0;
            case BOTTON -> 0;
        };
        int dy = switch (edge) {
            case LEFT -> 0;
            case RIGHT -> 0;
            case TOP -> -1;
            case BOTTON -> 1;
        };
        int newX = x + dx;
        int newY = y + dy;

        while (grid.contains(newX, newY)) {
            int currentHeight = Character.getNumericValue(grid.get(newX, newY));
            if (currentHeight <= treeHeight) {
                distance = distance + 1;
            }
            if (currentHeight >= treeHeight) {
                treeVisible = false;
                break;
            }
            newX = newX + dx;
            newY = newY + dy;
        }

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
