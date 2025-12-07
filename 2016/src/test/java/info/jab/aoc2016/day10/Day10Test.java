package info.jab.aoc2016.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        //Given
        String fileName = "/day10/day10-input.txt";

        //When
        var day = new Day10();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(157);
    }
}
