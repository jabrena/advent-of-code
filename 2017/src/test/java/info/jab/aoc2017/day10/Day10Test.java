package info.jab.aoc2017.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day10Test {

    @Test
    @Timeout(30)
    void should_solve_day10_part1() {
        //Given
        String fileName = "/day10/day10-input.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("826");
    }
  
    @Test
    @Timeout(30)
    void should_solve_day10_part2() {
        //Given
        String fileName = "/day10/day10-input.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("d067d3f14d07e09c2e7308c3926605c4");
    }

}
