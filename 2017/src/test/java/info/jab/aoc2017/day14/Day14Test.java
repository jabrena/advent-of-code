package info.jab.aoc2017.day14;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day14Test {

    @Test
    void should_solve_day14_part1() {
        //Given
        String fileName = "/day14/day14-input.txt";

        //When
        Day14 day14 = new Day14();
        var result = day14.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(8194);
    }

    @Test
    void should_solve_day14_part2() {
        //Given
        String fileName = "/day14/day14-input.txt";

        //When
        Day14 day14 = new Day14();
        var result = day14.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1141);
    }

}
