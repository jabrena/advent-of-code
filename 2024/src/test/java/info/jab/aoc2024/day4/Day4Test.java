package info.jab.aoc2024.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day4Test {

    @ParameterizedTest
    @CsvSource({
        "/day4/day4-input-sample2.txt, 18",
        "/day4/day4-input-sample3.txt, 18",
        "/day4/day4-input.txt, 2464"
    })
    void should_solve_day4_part1(String fileName, int expectedResult) {
        //Given
        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @Test
    void should_solve_day4_part2_sample1() {
        //Given
        String fileName = "/day4/day4-part2-input-sample.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(9);
    }

    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1982);
    }

}
