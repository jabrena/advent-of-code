package info.jab.aoc2015.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        //Given
        String input = "1321131112";

        //When
        var day = new Day10();
        var result = day.getPart1Result(input);

        //Then
        then(result).isEqualTo(492982);
    }

    @Test
    void should_solve_day9_part2() {
        //Given
        String input = "1321131112";

        //When
        var day = new Day10();
        var result = day.getPart2Result(input);

        //Then
        then(result).isEqualTo(6989950);
    }

}
