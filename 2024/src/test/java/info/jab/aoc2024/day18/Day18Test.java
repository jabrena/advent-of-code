package info.jab.aoc2024.day18;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class Day18Test {

    @Test
    void should_solve_day18_part1_sample() {
        //Given
        String fileName = "/day18/day18-input-sample.txt";

        //When
        var day = new Day18();
        var result = day.getPart1Result(fileName, 7, 12, false);

        //Then
        then(result).isEqualTo("22");
    }

    @Test
    void should_solve_day18_part1() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart1Result(fileName, 71, 1024, false);

        //Then
        then(result).isEqualTo("506");
    }

    @Test
    void should_solve_day18_part2_sample() {
        //Given
        String fileName = "/day18/day18-input-sample.txt";

        //When
        var day = new Day18();
        var result = day.getPart2Result(fileName, 7, 12, false);

        //Then
        then(result).isEqualTo("6,1");
    }

    @Test
    void should_solve_day18_part2() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart2Result(fileName, 71, 1024, false);

        //Then
        then(result).isEqualTo("62,6");
    }

}
