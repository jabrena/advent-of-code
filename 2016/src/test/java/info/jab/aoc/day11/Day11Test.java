package info.jab.aoc.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day11Test {

    @Test
    void should_solve_day11_part1() {
        //Given
        String fileName = "/day11/day11-input.txt";

        //When
        var day = new Day11();
        var result = day.getPart1Result(fileName);

        //Then
        System.out.println("Day 11 Part 1 result: " + result);
        then(result).isEqualTo(37);
    }

    @Test
    void should_solve_day11_part2() {
        //Given
        String fileName = "/day11/day11-input.txt";

        //When
        var day = new Day11();
        var result = day.getPart2Result(fileName);

        //Then
        System.out.println("Day 11 Part 2 result: " + result);
        then(result).isEqualTo(61);
    }
}