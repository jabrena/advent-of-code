package info.jab.aoc.day5;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day5Test {

    @Test
    void should_solve_day5_part1() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        var day = new Day5();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(238);
    }

    @Test
    void should_solve_day5_part2() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        var day = new Day5();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(69);
    }

}
