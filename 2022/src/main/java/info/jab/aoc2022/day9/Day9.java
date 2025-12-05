package info.jab.aoc2022.day9;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.Arrays;

public class Day9 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        RopePhysics ropePhysics = new RopePhysics();
        ropePhysics.create(25, 25);
        ropePhysics.setInitialState();
        //ropePhysics.print();

        // @formatter:off
        ResourceLines.list("/" + fileName).stream()
                .map(Movement::fromString)
                .forEach(ropePhysics::execute);

        //ropePhysics.printVisited();
        return Arrays.stream(ropePhysics.getCellsVisited())
                .flatMap(Arrays::stream)
                .filter(v -> v)
                .count();
        // @formatter:on
    }

    @Override
    public Long getPart2Result(String fileName) {
        return null;
    }
}
