package info.jab.aoc2017.day12;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day12Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day12_part1() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        Day12 day12 = new Day12();
        var result = day12.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(380);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day12_part2() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        Day12 day12 = new Day12();
        var result = day12.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(181);
    }

}
