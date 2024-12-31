package info.jab.aoc.day7;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day7Test {

    @Test
    void should_solve_day7_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day7/day7-input.txt";

            //When
            var day = new Day7();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(3176);
        });
    }

    @Test
    void should_solve_day7_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day7/day7-input.txt";

            //When
            var day = new Day7();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(14710);
        });
    }
}
