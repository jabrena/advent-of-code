package info.jab.aoc2024.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day11Test {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
        "/day11/day11-input-sample.txt, 6, 22",
        "/day11/day11-input-sample.txt, 25, 55312",
        "/day11/day11-input.txt, 25, 220999"
    })
    void should_solve_day11_part1(String fileName, int blinks, long expectedResult) {
        //Given
        //When
        var day = new Day11();
        var result = day.getPart1Result(fileName, blinks);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @Test
    void should_solve_day11_part2() {
        //Given
        String fileName = "/day11/day11-input.txt";

        //When
        var day = new Day11();
        var result = day.getPart2Result(fileName, 75);

        //Then
        then(result).isEqualTo(261936432123724L);
    }

}
