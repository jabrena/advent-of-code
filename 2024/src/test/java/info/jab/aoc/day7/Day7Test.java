package info.jab.aoc.day7;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.resources.ResourceLines;
import com.putoet.utils.Timer;

class Day7Test {

    @Disabled
    @Test
    void should_solve_day7_methods() {
        //Given
        //When
        //Then
        var day = new Day7();
        String fileName = "/day7/day7-input-sample.txt";
        var list = ResourceLines.list(fileName);
        list.stream()
            .forEach(str -> {
                var parts = str.split(":");
                var result = Long.parseLong(parts[0]);
                var operators = parts[1].trim().split(" ");
                long[] numbers = Arrays.stream(operators).mapToLong(Long::parseLong).toArray();
                var flag = day.tryOps(true, numbers, result, 0, numbers[0], false, "");
                System.out.println(flag);
            });
    }

    @Test
    void should_solve_day7_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day7/day7-input-sample.txt";

            //When
            var day = new Day7();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(3749);
        });
    }

    @Test
    void should_solve_day7_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day7/day7-input.txt";

            //When
            var day = new Day7();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(1399219271639L);
        });
    }

    @Test
    void should_solve_day7_part2_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day7/day7-input-sample.txt";

            //When
            var day = new Day7();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(11387);
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
            then(result).isEqualTo(275791737999003L);
        });
    }

}
