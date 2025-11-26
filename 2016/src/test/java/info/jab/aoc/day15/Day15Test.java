package info.jab.aoc.day15;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day15Test {

    @Test
    void should_solve_day15_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day15/day15-input.txt";

            //When
            var day = new Day15();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(317371);
        });
    }

    @Test
    void should_solve_day15_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day15/day15-input.txt";

            //When
            var day = new Day15();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(2080951);
        });
    }

}
