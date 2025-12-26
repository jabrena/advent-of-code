package info.jab.aoc2025.day10;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.List;

/**
 * Solver for button press optimization problems.
 */
public final class ButtonPressOptimizer implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        Part1Solver solver = new Part1Solver();
        return lines.stream()
            .filter(line -> line.contains("["))
            .map(InputParser::parsePart1)
            .mapToLong(solver::solve)
            .sum();
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        Part2Solver solver = new Part2Solver();
        // Use sequential stream since Part2Solver already uses ForkJoinPool for parallelization
        // This avoids double parallelization overhead and thread contention
        return lines.stream()
            .filter(line -> line.contains("["))
            .map(InputParser::parsePart2)
            .mapToLong(solver::solve)
            .sum();
    }
}
