package info.jab.aoc2024.day16;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day16Test {

    @ParameterizedTest
    @CsvSource({
        "/day16/day16-input-sample.txt, 7036",
        "/day16/day16-input-sample2.txt, 11048",
        "/day16/day16-input.txt, 75416"
    })
    void should_solve_day16_part1(String fileName, int expectedResult) {
        //Given
        //When
        var day = new Day16();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
        "/day16/day16-input-sample.txt, 45",
        "/day16/day16-input-sample2.txt, 64",
        "/day16/day16-input.txt, 476"
    })
    void should_solve_day16_part2(String fileName, int expectedResult) {
        //Given
        //When
        var day = new Day16();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
    }

}
