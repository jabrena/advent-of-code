package info.jab.aoc.day6;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day6Test {

    @Test
    void should_solve_day6_part1() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day = new Day6();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("nabgqlcw");
    }

    @Test
    void should_solve_day6_part2() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day = new Day6();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("ovtrjcjh");
    }

}