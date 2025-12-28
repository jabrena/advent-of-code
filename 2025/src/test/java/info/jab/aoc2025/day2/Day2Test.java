package info.jab.aoc2025.day2;

import static org.assertj.core.api.BDDAssertions.then;

import info.jab.aoc.Solver;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day2Test {

    @Test
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1227775554L);
    }

    @Test
    void should_solve_day2_part1() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(21898734247L);
    }

    @Test
    void should_solve_day2_part2_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(4174379265L);
    }

    @Test
    void should_solve_day2_part2() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(28915664389L);
    }

    @ParameterizedTest(name = "{0} should solve part 1 correctly")
    @MethodSource("solvers")
    void should_solve_part1_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day2/day2-input.txt";

        //When
        final Long result = solver.solvePartOne(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(21898734247L);
    }

    @ParameterizedTest(name = "{0} should solve part 2 correctly")
    @MethodSource("solvers")
    void should_solve_part2_with_all_solvers(final String solverName, final Solver<Long> solver) {
        //Given
        final String fileName = "/day2/day2-input.txt";

        //When
        final Long result = solver.solvePartTwo(fileName);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(28915664389L);
    }

    static Stream<Arguments> solvers() {
        return Stream.of(
                Arguments.of("InvalidIdValidator", new InvalidIdValidator()),
                Arguments.of("InvalidIdValidator2", new InvalidIdValidator2()),
                Arguments.of("InvalidIdValidator3", new InvalidIdValidator3())
        );
    }

}
