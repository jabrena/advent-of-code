package info.jab.aoc.day20;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day20Test {

    @Test
    void should_solve_day20_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day20/day20-input.txt";

            //When
            var day = new Day20();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(786240);
        });
    }

    @Test
    void should_solve_day20_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day20/day20-input.txt";

            //When
            var day = new Day20();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(831600);
        });
    }

}