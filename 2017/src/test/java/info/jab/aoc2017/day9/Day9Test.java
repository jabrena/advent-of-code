package info.jab.aoc2017.day9;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day9Test {

    @Test
    @Timeout(30)
    void should_solve_day9_part1() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        Day9 day9 = new Day9();
        var result = day9.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(16827);
    }

    @Test
    @Timeout(30)
    void should_solve_day9_part2() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        Day9 day9 = new Day9();
        var result = day9.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(7298);
    }

}
