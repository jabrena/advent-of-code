package info.jab.aoc.day2;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/2
 */
public class Day2 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(ReportAnalyzer.toList).filter(ReportAnalyzer.isSafePart1).count();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(ReportAnalyzer.toList).filter(ReportAnalyzer.isSafePart2).count();
    }

    public Integer getPart1Result2(String fileName) {
        var list = ResourceLines.list(fileName);
        return ReportAnalyzer2.solvePart12(list);
    }
}
