package info.jab.aoc2024.day15;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day15Test {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
        "/day15/day15-input-sample.txt, 2028",
        "/day15/day15-input-sample2.txt, 10092",
        "/day15/day15-input.txt, 1465523"
    })
    void should_solve_day15_part1(String fileName, int expectedResult) {
        //Given
        //When
        var day1 = new Day15();
        var result = day1.getPart1Result(fileName, false);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @Test
    void should_solve_day15_part2_sample() {
        //Given
        String fileName = "/day15/day15-input-sample3.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart2Result(fileName, true);

        //Then
        then(result).isEqualTo(618);
    }

    @Test
    void should_solve_day15_part2() {
        //Given
        String fileName = "/day15/day15-input.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart2Result(fileName, true);

        //Then
        then(result).isEqualTo(1471049);
    }
}
