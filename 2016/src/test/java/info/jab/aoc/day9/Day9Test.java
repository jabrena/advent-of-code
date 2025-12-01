package info.jab.aoc.day9;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day9Test {

    @Test
    void should_solve_day9_part1() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        System.out.println("Day 9 Part 1 result: " + result);
        then(result).isEqualTo(102239L);
    }

    @Test
    void should_solve_day9_part2() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart2Result(fileName);

        //Then
        System.out.println("Day 9 Part 2 result: " + result);
        then(result).isEqualTo(10780403063L);
    }
}
