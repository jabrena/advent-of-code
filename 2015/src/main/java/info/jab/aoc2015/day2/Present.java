package info.jab.aoc2015.day2;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class Present implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(Dimensions::from)
            .map(Dimensions::calculateWrappingPaper)
            .mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(Dimensions::from)
            .map(Dimensions::calculateRibbon)
            .mapToInt(Integer::intValue)
            .sum();
    }
}
