package info.jab.aoc2015.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day3Test {

    @Test
    void should_solve_day3_part1() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day = new Day3();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(2081);
    }

    @Test
    void should_solve_day3_part2() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day = new Day3();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2341);
    }

}
