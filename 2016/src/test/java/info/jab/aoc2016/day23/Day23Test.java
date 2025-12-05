package info.jab.aoc2016.day23;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day23Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day23_part1() {
        //Given
        String fileName = "/day23/day23-input.txt";

        //When
        var day = new Day23();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(10953);
    }

    @Test
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void should_solve_day23_part2() {
        //Given
        String fileName = "/day23/day23-input.txt";

        //When
        var day = new Day23();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(479007513);
    }

}
