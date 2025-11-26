package info.jab.aoc.day19;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day19Test {

    @Test
    void should_solve_day19_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input.txt";

            //When
            var day = new Day19();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(576);
        });
    }

    @Test
    void should_solve_day19_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input.txt";

            //When
            var day = new Day19();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(207);
        });
    }

}