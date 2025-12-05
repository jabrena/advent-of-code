package info.jab.aoc2024.day22;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day22Test {

    @Test
    void should_solve_day1_part1_sample() {
        //Given
        String fileName = "/day22/day22-input-sample.txt";

        //When
        var day = new Day22();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(37327623);
    }

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day22/day22-input.txt";

        //When
        var day = new Day22();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(13584398738L);
    }

    @Test
    void should_solve_day1_part2_sample() {
        //Given
        String fileName = "/day22/day22-input-sample2.txt";

        //When
        var day = new Day22();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(23);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day22/day22-input.txt";

        //When
        var day = new Day22();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1612);
    }

}
