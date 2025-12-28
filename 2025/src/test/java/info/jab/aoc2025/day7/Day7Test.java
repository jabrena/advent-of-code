package info.jab.aoc2025.day7;

import static org.assertj.core.api.BDDAssertions.then;

import info.jab.aoc.Solver;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day7Test {

    @Test
    void should_solve_day7_part1_with_sample() {
        //Given
        String fileName = "/day7/day7-input-sample.txt";

        //When
        var day7 = new Day7();
        var result = day7.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(21L);
    }

    @Test
    void should_solve_day7_part1() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day7 = new Day7();
        var result = day7.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1537L);
    }

    @Test
    void should_solve_day7_part2_with_sample() {
        //Given
        String fileName = "/day7/day7-input-sample.txt";

        //When
        var day7 = new Day7();
        var result = day7.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(40L);
    }

    @Test
    void should_solve_day7_part2() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day7 = new Day7();
        var result = day7.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(18818811755665L);
    }

    @ParameterizedTest(name = "{0} should solve part 1 correctly")
    @MethodSource("solvers")
    void should_solve_part1_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day7/day7-input.txt";

        //When
        final Long result = solver.solvePartOne(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(1537L);
    }

    @ParameterizedTest(name = "{0} should solve part 2 correctly")
    @MethodSource("solvers")
    void should_solve_part2_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day7/day7-input.txt";

        //When
        final Long result = solver.solvePartTwo(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(18818811755665L);
    }

    static Stream<Arguments> solvers() {
        return Stream.of(
                Arguments.of("BeamPathCounter", new BeamPathCounter()),
                Arguments.of("BeamPathCounter2", new BeamPathCounter2())
        );
    }

}
