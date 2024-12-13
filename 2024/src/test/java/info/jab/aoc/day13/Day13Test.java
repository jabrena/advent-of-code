package info.jab.aoc.day13;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day13Test {

    @Test
    void should_solve_day11_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day13/day13-input-sample.txt";

            //When
            var day = new Day13();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(480);
        });
    }

    @Test
    void should_solve_day11_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day13/day13-input.txt";

            //When
            var day = new Day13();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(31623);
        });
    }

    @Test
    void should_solve_day11_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day13/day13-input.txt";

            //When
            var day = new Day13();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(93209116744825L);
        });
    }

}
