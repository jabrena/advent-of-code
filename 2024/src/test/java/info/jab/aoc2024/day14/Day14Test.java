package info.jab.aoc2024.day14;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day14Test {

    @Test
    void should_solve_day1_part1_sample() {
        //Given
        String fileName = "/day14/day14-input-sample.txt";

        //When
        var day1 = new Day14();
        var result = day1.getPart1Result(fileName, 11, 7);

        //Then
        then(result).isEqualTo(12);
    }

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day14/day14-input.txt";

        //When
        var day1 = new Day14();
        var result = day1.getPart1Result(fileName, 101, 103);

        //Then
        then(result).isEqualTo(215987200);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day14/day14-input.txt";

        //When
        var day1 = new Day14();
        var result = day1.getPart2Result(fileName, 101, 103);

        //Then
        then(result).isEqualTo(8050);
    }

}
