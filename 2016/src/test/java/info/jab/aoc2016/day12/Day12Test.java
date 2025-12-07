package info.jab.aoc2016.day12;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day12Test {

    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @Test
    void should_solve_day12_part1() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(318007);
    }

    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    @Test
    void should_solve_day12_part2() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(9227661);
    }
}