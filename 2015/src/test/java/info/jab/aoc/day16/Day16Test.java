package info.jab.aoc.day16;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day16Test {

    @Test
    void should_solve_day16_part1() {
        //Given
        String fileName = "/day16/day16-input.txt";

        //When
        var day = new Day16();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(40);
    }

    @Test
    void should_solve_day16_part2() {
        //Given
        String fileName = "/day16/day16-input.txt";

        //When
        var day = new Day16();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(241);
    }

}