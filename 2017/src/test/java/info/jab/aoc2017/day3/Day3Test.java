package info.jab.aoc2017.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class Day3Test {

    @Test
    void should_solve_day3_part1() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(480);
    }

    @Test
    void should_solve_day3_part2() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(349975);
    }

}
