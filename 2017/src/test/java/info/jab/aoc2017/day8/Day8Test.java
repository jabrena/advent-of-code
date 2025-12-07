package info.jab.aoc2017.day8;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day8Test {

    @Test
    @Timeout(30)
    void should_solve_day8_part1() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        Day8 day8 = new Day8();
        var result = day8.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(5143);
    }

    @Test
    @Timeout(30)
    void should_solve_day8_part2() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        Day8 day8 = new Day8();
        var result = day8.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(6209);
    }

}
