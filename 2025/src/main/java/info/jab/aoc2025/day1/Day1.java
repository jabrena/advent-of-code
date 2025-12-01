package info.jab.aoc2025.day1;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

public class Day1 implements Solver<Integer> {

    private final DialRotator dialRotator = new DialRotator();

    @Override
    public Integer solvePartOne(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);
        return dialRotator.countZerosAfterRotations(rotations);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);
        return dialRotator.countZerosDuringRotations(rotations);
    }
}
