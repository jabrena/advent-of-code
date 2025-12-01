package info.jab.aoc2015.day23;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day23Test {

    @Test
    void should_solve_day23_part1() {
        //Given
        String fileName = "/day23/day23-input.txt";

        //When
        var day = new Day23();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(307);
    }

    @Test
    void should_solve_day23_part2() {
        //Given
        String fileName = "/day23/day23-input.txt";

        //When
        var day = new Day23();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(160);
    }

}