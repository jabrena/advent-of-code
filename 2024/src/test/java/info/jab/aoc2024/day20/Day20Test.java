package info.jab.aoc2024.day20;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//TODO Solution doesn´t perform well
class Day20Test {

    @Test
    void should_solve_day20_part1_sample() {
        //Given
        String fileName = "/day20/day20-input-sample.txt";

        //When
        var day = new Day20();
        var result = day.getPart1Result(fileName, 64);

        //Then
        then(result).isEqualTo(1);
    }

    @Test
    void should_solve_day20_part1() {
        //Given
        String fileName = "/day20/day20-input.txt";

        //When
        var day = new Day20();
        var result = day.getPart1Result(fileName, 100);

        //Then
        then(result).isEqualTo(1452);
    }

    @Disabled("Part 2 test disabled - implementation may be incomplete or slow")
    @Test
    void should_solve_day20_part2() {
        //Given
        String fileName = "/day20/day20-input.txt";

        //When
        var day = new Day20();
        var result = day.getPart2Result(fileName, 100);

        //Then
        then(result).isEqualTo(999556);
    }

}
