package info.jab.aoc.day6;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.putoet.utils.Timer;

class Day6Test {

    @Test
    void should_solve_day6_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day6/day6-input-sample.txt";

            //When
            var day = new Day6();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(41);
        });
    }

    @Disabled
    @Test
    void should_solve_day6_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day6/day6-input.txt";

            //When
            var day = new Day6();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(5312);
        });
    }

    @Disabled
    @Test
    void should_solve_day6_part2_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day6/day6-input-sample.txt";

            //When
            var day = new Day6();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(6);
        });
    }

    @Disabled
    @Test
    void should_solve_day6_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day6/day6-input2.txt";

            //When
            var day = new Day6();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(1748);
        });
    }

}
