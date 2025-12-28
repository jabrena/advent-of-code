package info.jab.aoc2025.day6;

import static org.assertj.core.api.BDDAssertions.then;

import info.jab.aoc.Solver;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day6Test {

    @Test
    void should_solve_day6_part1_with_sample() {
        //Given
        String fileName = "/day6/day6-input-sample.txt";

        //When
        var day6 = new Day6();
        var result = day6.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(4277556L);
    }

    @Test
    void should_solve_day6_part1() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day6 = new Day6();
        var result = day6.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(5316572080628L);
    }

    @Test
    void should_solve_day6_part2_with_sample() {
        //Given
        String fileName = "/day6/day6-input-sample.txt";

        //When
        var day6 = new Day6();
        var result = day6.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(3263827L);
    }

    @Test
    void should_solve_day6_part2() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day6 = new Day6();
        var result = day6.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(11299263623062L);
    }

    @ParameterizedTest(name = "{0} should solve part 1 correctly")
    @MethodSource("solvers")
    void should_solve_part1_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day6/day6-input.txt";

        //When
        final Long result = solver.solvePartOne(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(5316572080628L);
    }

    @ParameterizedTest(name = "{0} should solve part 2 correctly")
    @MethodSource("solvers")
    void should_solve_part2_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day6/day6-input.txt";

        //When
        final Long result = solver.solvePartTwo(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(11299263623062L);
    }

    static Stream<Arguments> solvers() {
        return Stream.of(
                Arguments.of("MathBlock", new MathBlock()),
                Arguments.of("MathBlock2", new MathBlock2())
        );
    }

}
