package info.jab.aoc.day6;

import com.putoet.resources.ResourceLines;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;

public class Game {

    private final Guardian guardian;

    public Game(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
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
