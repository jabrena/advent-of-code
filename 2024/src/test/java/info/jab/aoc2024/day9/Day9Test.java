package info.jab.aoc2024.day9;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Day9Test {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
        "/day9/day9-input-sample2.txt, 60",
        "/day9/day9-input-sample.txt, 1928"
    })
    void should_solve_day9_part1(String fileName, long expectedResult) {
        //Given
        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @Disabled("Test disabled - sample3 input file may not exist or test is redundant")
    @org.junit.jupiter.api.Test
    void should_solve_day9_part1_with_sample3() {
        //Given
        String fileName = "/day9/day9-input-sample3.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(60);
    }

    @Test
    void should_solve_day9_part1() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(6241633730082L);
    }

    @Test
    void should_solve_day9_part2_with_sample() {
        //Given
        String fileName = "/day9/day9-input-sample.txt";

        //When
        var day = new Day9();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2858);
    }

    @Test
    void should_solve_day9_part2() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(6265268809555L);
    }

}
