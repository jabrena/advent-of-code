package info.jab.aoc2017.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day11Test {

    @Test
    void should_solve_day11_part1() {
        //Given
        String fileName = "/day11/day11-input.txt";

        //When
        Day11 day11 = new Day11();
        var result = day11.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(877);
    }

    @Test
    void should_solve_day11_part2() {
        //Given
        String fileName = "/day11/day11-input.txt";

        //When
        Day11 day11 = new Day11();
        var result = day11.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1622);
    }

}
