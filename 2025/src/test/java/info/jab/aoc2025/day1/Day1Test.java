package info.jab.aoc2025.day1;

import static org.assertj.core.api.BDDAssertions.then;

import info.jab.aoc.Solver;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day1Test {

    private static final String FILE_NAME = "/day1/day1-input.txt";
    private static final int EXPECTED_PART1 = 1034;
    private static final int EXPECTED_PART2 = 6166;

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = FILE_NAME;

        //When
        var day1 = new Day1();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(EXPECTED_PART1);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = FILE_NAME;

        //When
        var day1 = new Day1();
        var result = day1.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(EXPECTED_PART2);
    }

    @ParameterizedTest(name = "{0} should solve part 1 correctly")
    @MethodSource("solvers")
    void should_solve_part1_with_all_solvers(final String solverName, final Solver<Integer> solver) {
        //When
        final Integer result = solver.solvePartOne(FILE_NAME);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(EXPECTED_PART1);
    }

    @ParameterizedTest(name = "{0} should solve part 2 correctly")
    @MethodSource("solvers")
    void should_solve_part2_with_all_solvers(final String solverName, final Solver<Integer> solver) {
        //When
        final Integer result = solver.solvePartTwo(FILE_NAME);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(EXPECTED_PART2);
    }

    static Stream<Arguments> solvers() {
        return Stream.of(
                Arguments.of("DialRotator", new DialRotator()),
                Arguments.of("DialRotator2", new DialRotator2()),
                Arguments.of("DialRotator3", new DialRotator3())
        );
    }

}
