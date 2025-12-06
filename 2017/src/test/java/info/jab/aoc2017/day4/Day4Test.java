package info.jab.aoc2017.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class Day4Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day4_part1() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(466);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(251);
    }

}
