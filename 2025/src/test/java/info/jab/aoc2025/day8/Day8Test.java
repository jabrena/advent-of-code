package info.jab.aoc2025.day8;

import static org.assertj.core.api.BDDAssertions.then;

import info.jab.aoc.Solver2;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day8Test {

    @Test
    void should_solve_day8_part1() {
        //Given
        String fileName = "/day8/day8-input.txt";
        Integer connectionLimit = 1000;

        //When
        var day8 = new Day8();
        var result = day8.getPart1Result(fileName, connectionLimit);

        //Then
        then(result).isEqualTo(164475L);
    }

    @Test
    void should_solve_day8_part2() {
        //Given
        String fileName = "/day8/day8-input.txt";
        Integer connectionLimit = 1000;

        //When
        var day8 = new Day8();
        var result = day8.getPart2Result(fileName, connectionLimit);

        //Then
        then(result).isEqualTo(169521198L);
    }

    @Test
    void should_solve_part1_with_sample_after_10_connections() {
        //Given
        // According to problem description: https://adventofcode.com/2025/day/8
        // After making the ten shortest connections, there are 11 circuits:
        // one circuit which contains 5 junction boxes, one circuit which contains 4 junction boxes,
        // two circuits which contain 2 junction boxes each, and seven circuits which each contain
        // a single junction box. Multiplying together the sizes of the three largest circuits
        // (5, 4, and one of the circuits of size 2) produces 40.
        String fileName = "/day8/day8-input-sample.txt";
        Integer connectionLimit = 10;

        //When
        var day8 = new Day8();
        var result = day8.getPart1Result(fileName, connectionLimit);

        //Then
        then(result).isEqualTo(40L);
    }

    @Test
    void should_solve_part2_with_sample() {
        //Given
        // Part 2: Find the connection that merges all points into one cluster
        // The last connection that completes the merge is between points at indices 10 and 12
        // Point 10: (216, 146, 977) - x = 216
        // Point 12: (117, 168, 530) - x = 117
        // Result: 216 * 117 = 25272
        String fileName = "/day8/day8-input-sample.txt";
        Integer connectionLimit = 1000;

        //When
        var day8 = new Day8();
        var result = day8.getPart2Result(fileName, connectionLimit);

        //Then
        then(result).isEqualTo(25272L);
    }

    @ParameterizedTest(name = "{0} should solve part 1 correctly")
    @MethodSource("solvers")
    void should_solve_part1_with_all_solvers(final String solverName, final Solver2<Long, String, Integer> solver) {
        //Given
        final String fileName = "/day8/day8-input.txt";
        final Integer connectionLimit = 1000;

        //When
        final Long result = solver.solvePartOne(fileName, connectionLimit);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(164475L);
    }

    @ParameterizedTest(name = "{0} should solve part 2 correctly")
    @MethodSource("solvers")
    void should_solve_part2_with_all_solvers(final String solverName, final Solver2<Long, String, Integer> solver) {
        //Given
        final String fileName = "/day8/day8-input.txt";
        final Integer connectionLimit = 1000;

        //When
        final Long result = solver.solvePartTwo(fileName, connectionLimit);

        //Then
        then(result).as("Solver: %s", solverName).isNotNull().isEqualTo(169521198L);
    }

    static Stream<Arguments> solvers() {
        return Stream.of(
                Arguments.of("PointCluster", new PointCluster()),
                Arguments.of("PointCluster2", new PointCluster2()),
                Arguments.of("PointCluster3", new PointCluster3())
        );
    }

}
