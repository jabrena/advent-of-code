package info.jab.aoc.day13;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day13Test {

    @Test
    void should_solve_day13_part1() {
        //Given
        String fileName = "/day13/day13-input.txt";

        //When
        var day = new Day13();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(90);
    }

    @Test
    void should_solve_day13_part2() {
        //Given
        String fileName = "/day13/day13-input.txt";

        //When
        var day = new Day13();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(135);
    }

}