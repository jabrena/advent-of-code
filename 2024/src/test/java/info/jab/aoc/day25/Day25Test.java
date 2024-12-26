package info.jab.aoc.day25;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day25Test {

    @Test
    void should_solve_day1_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day25/day25-input-sample.txt";

            //When
            var day = new Day25();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(3);
        });
    }

    @Test
    void should_solve_day1_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day25/day25-input.txt";

            //When
            var day = new Day25();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(2586);
        });
    }

}
