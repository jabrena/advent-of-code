package jab.aoc.day8;

import jab.aoc.Day;
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

    record Tuple(Boolean isVisible, Integer distance) {}

    private Tuple getDistanceAndVisibility(Integer[][] grid, int i, int j, Edge edge) {
        int treeHeight = grid[i][j];
        boolean treeVisible = true;
        int distance = 0;

        //Adjustment
        int ti = 0;
        int tj = 0;
        if (edge == Edge.LEFT) {
            ti = 0;
            tj = -1;
        } else if (edge == Edge.RIGHT) {
            ti = 0;
            tj = 1;
        } else if (edge == Edge.TOP) {
            ti = -1;
            tj = 0;
        } else {
            ti = 1;
            tj = 0;
        }
        i = i + ti;
        j = j + tj;

        while (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
            if (grid[i][j] <= treeHeight) {
                distance = distance + 1;
            }
            if (grid[i][j] >= treeHeight) {
                treeVisible = false;
                break;
            }
            i = i + ti;
            j = j + tj;
        }

        return new Tuple(treeVisible, distance);
    }

    // @formatter:off
    private Boolean isVisibleFromOutside(Integer[][] grid, Integer i, Integer j) {
        return (getDistanceAndVisibility(grid, i, j, Edge.LEFT).isVisible()
            || getDistanceAndVisibility(grid, i, j, Edge.RIGHT).isVisible()
            || getDistanceAndVisibility(grid, i, j, Edge.TOP).isVisible()
            || getDistanceAndVisibility(grid, i, j, Edge.BOTTON).isVisible()
        );
    }

    // @formatter:on

    @Override
    public Integer getPart1Result(String fileName) {
        //Source
        Integer[][] grid = MatrixHelper.getMatrix(fileName);

        //Transform
        //Sink
        Integer visibleTrees = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (isVisibleFromOutside(grid, x, y)) {
                    visibleTrees++;
                }
            }
        }
        return visibleTrees;
    }

    // @formatter:off
    private Integer scenicScore(int i, int j, Integer[][] grid) {
        return (getDistanceAndVisibility(grid, i, j, Edge.LEFT).distance()
                * getDistanceAndVisibility(grid, i, j, Edge.RIGHT).distance()
                * getDistanceAndVisibility(grid, i, j, Edge.TOP).distance()
                * getDistanceAndVisibility(grid, i, j, Edge.BOTTON).distance()
        );
    }

    // @formatter:on

    @Override
    public Integer getPart2Result(String fileName) {
        //Source
        Integer[][] grid = MatrixHelper.getMatrix(fileName);

        //Transform
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                scores.add(scenicScore(i, j, grid));
            }
        }

        //Sink
        return Collections.max(scores);
    }
}
