package info.jab.aoc2025.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day3Test {

    @Test
    @Timeout(30)
    void should_solve_day3_part1_sample() {
        //Given
        String fileName = "/day3/day3-input-sample.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(357L);
    }

    @Test
    @Timeout(30)
    void should_solve_day3_part1() {
        //Given
        String fileName = "day3/day3-input.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(17383L);
    }

    @Test
    @Timeout(30)
    void should_solve_day3_part2_sample() {
        //Given
        String fileName = "day3/day3-input-sample.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(3121910778619L);
    }

    @Test
    @Timeout(30)
    void should_solve_day3_part2() {
        //Given
        String fileName = "day3/day3-input.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(172601598658203L);
    }
}
