package info.jab.aoc;

import com.putoet.resources.ResourceLines;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;

public class GridDemo {

    /**
     * https://adventofcode.com/2022/day/23
     * https://github.com/z669016/adventofcode-2022/blob/master/src/main/java/com/putoet/day23/Day23.java
     */
    public Grid grid1(String fileName) {

        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        System.out.println(grid);

        return grid;
    }

    /**
     * https://adventofcode.com/2022/day/17
     * https://github.com/z669016/adventofcode-2022/blob/master/src/main/java/com/putoet/day17/Day17.java
     */
    public Grid grid2() {

        Grid grid = new Grid(GridUtils.of(0, 10, 0, 10, '.'));
        System.out.println(grid);

        return grid;
    }

    /**
     * https://adventofcode.com/2021/day/11
     * https://github.com/z669016/adventofcode-2021/blob/master/src/main/java/com/putoet/day11/Cavern.java
     */
    public Grid grid3(String fileName) {

        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        System.out.println(grid);

        return grid;
    }

    /**
     * Dynamic grid
     * https://adventofcode.com/2021/day/5
     * https://github.com/z669016/adventofcode-2021/blob/master/src/main/java/com/putoet/day5/OceanFloor.java
     */
    public Grid grid4() {
        return null;
    }

}
