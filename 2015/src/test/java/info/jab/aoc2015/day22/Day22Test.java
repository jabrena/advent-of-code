package info.jab.aoc2015.day22;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day22Test {

    @Test
    void should_solve_day22_part1() {
        //Given
        String fileName = "day22/day22-input.txt";

        //When
        var day = new Day22();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(900);
    }

    @Test
    void should_solve_day22_part2() {
        //Given
        String fileName = "day22/day22-input.txt";

        //When
        var day = new Day22();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1216);
    }

}