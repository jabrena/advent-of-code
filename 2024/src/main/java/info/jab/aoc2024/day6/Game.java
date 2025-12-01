package info.jab.aoc2024.day6;

import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;

public class Game {

    private final Grid grid;
    private final Guardian guardian;

    public Game(String fileName) {
        var list = ResourceLines.list(fileName);
        grid = new Grid(GridUtils.of(list));
        var point = grid.findFirst(c -> c == '^').get();

        //TODO Autodetect initial point
        guardian = new Guardian(Direction.NORTH, point, grid);
    }

    public Integer moveGuardian() {
        //Walking
        while(!guardian.isOut()){
            guardian.walk();
        }
        //Calculate Steps
        return guardian.getSteps();
    }

}
