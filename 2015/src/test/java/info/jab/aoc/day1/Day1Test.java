package info.jab.aoc.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day1Test {

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(74);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1795);
    }

}
