package info.jab.aoc.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day11Test {

    @Test
    void should_solve_day11_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day11/day11-input-sample.txt";

            //When
            var day = new Day11();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(36);
        });
    }

}
