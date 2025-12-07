package info.jab.aoc2017.day5;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day5Test {

    @Test
    @Timeout(30)
    void should_solve_day5_part1() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(364539);
    }

    @Test
    @Timeout(30)
    void should_solve_day5_part2() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(27477714);
    }

}
