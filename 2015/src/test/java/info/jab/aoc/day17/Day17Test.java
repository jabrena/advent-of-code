package info.jab.aoc.day17;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day17Test {

    @Test
    void should_solve_day17_part1() {
        //Given
        String fileName = "/day17/day17-input.txt";

        //When
        var day = new Day17();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1638);
    }

    @Test
    void should_solve_day17_part2() {
        //Given
        String fileName = "/day17/day17-input.txt";

        //When
        var day = new Day17();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(17);
    }

}