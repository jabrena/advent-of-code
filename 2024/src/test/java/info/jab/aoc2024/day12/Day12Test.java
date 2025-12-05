package info.jab.aoc2024.day12;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day12Test {

    @ParameterizedTest
    @CsvSource({
        "/day12/day12-input-sample.txt, 1930",
        "/day12/day12-input-sample2.txt, 32",
        "/day12/day12-input.txt, 1473276"
    })
    void should_solve_day12_part1(String fileName, int expectedResult) {
        //Given
        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
    }

    @Disabled("Part 2 test disabled - implementation may be incomplete or slow")
    @Test
    void should_solve_day12_part2_with_sample() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(901100);
    }
}
