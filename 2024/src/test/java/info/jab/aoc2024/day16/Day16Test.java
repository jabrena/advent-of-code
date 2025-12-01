package info.jab.aoc2024.day16;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day16Test {

    @Test
    void should_solve_day16_part1_sample() {
        //Given
        String fileName = "/day16/day16-input-sample.txt";

        //When
        var day = new Day16();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(7036);
    }

    @Test
    void should_solve_day16_part1_sample2() {
        //Given
        String fileName = "/day16/day16-input-sample2.txt";

        //When
        var day = new Day16();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(11048);
    }

    @Test
    void should_solve_day16_part1() {
        //Given
        String fileName = "/day16/day16-input.txt";

        //When
        var day = new Day16();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(75416);
    }

    @Test
    void should_solve_day16_part2_sample1() {
        //Given
        String fileName = "/day16/day16-input-sample.txt";

        //When
        var day = new Day16();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(45);
    }

    @Test
    void should_solve_day16_part2_sample2() {
        //Given
        String fileName = "/day16/day16-input-sample2.txt";

        //When
        var day = new Day16();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(64);
    }

    @Test
    void should_solve_day16_part2() {
        //Given
        String fileName = "/day16/day16-input.txt";

        //When
        var day = new Day16();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(476);
    }

}
