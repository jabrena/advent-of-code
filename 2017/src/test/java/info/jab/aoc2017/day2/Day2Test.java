package info.jab.aoc2017.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class Day2Test {

    @Test
    void should_solve_day2_part1() {
        //Given
        String fileName = "day2/day2-input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.solvePartOne(fileName);

        //Then
        then(result).isEqualTo(51833);
    }

    @Test
    void should_solve_day2_part2() {
        //Given
        String fileName = "day2/day2-input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.solvePartTwo(fileName);

        //Then
        then(result).isEqualTo(288);
    }

}
