package info.jab.aoc2015.day24;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day24Test {

    @Test
    void should_solve_day24_part1() {
        //Given
        String fileName = "/day24/day24-input.txt";

        //When
        var day = new Day24();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(11846773891L);
    }

    @Test
    void should_solve_day24_part2() {
        //Given
        String fileName = "/day24/day24-input.txt";

        //When
        var day = new Day24();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(80393059L);
    }

}