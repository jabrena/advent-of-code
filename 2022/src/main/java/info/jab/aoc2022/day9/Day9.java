package info.jab.aoc2022.day9;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;

public class Day9 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        RopePhysics ropePhysics = new RopePhysics();
        ropePhysics.create(25, 25);
        ropePhysics.setInitialState();

        // @formatter:off
        ResourceLines.list("/" + fileName).stream()
                .map(Movement::fromString)
                .forEach(ropePhysics::execute);

        boolean[][] cellsVisited = ropePhysics.getCellsVisited();
        long count = 0;
        for (boolean[] row : cellsVisited) {
            for (boolean cell : row) {
                if (cell) {
                    count++;
                }
            }
        }
        return count;
        // @formatter:on
    }

    @Override
    public Long getPart2Result(String fileName) {
        return null;
    }
}
