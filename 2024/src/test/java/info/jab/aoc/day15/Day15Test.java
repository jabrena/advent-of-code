package info.jab.aoc.day15;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day15Test {

    @Test
    void should_solve_day1_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day14/day14-input-sample.txt";

            //When
            var day1 = new Day15();
            var result = day1.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(12);
        });
    }
}
