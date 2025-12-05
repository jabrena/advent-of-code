package info.jab.aoc2017.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class Day3Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
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
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
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
