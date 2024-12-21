package info.jab.aoc.day21;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

import org.junit.jupiter.api.Disabled;

class Day21Test {

    @Test
    void should_solve_day21_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day21/day21-input-sample.txt";

            //When
            Day21 day1 = new Day21();
            var result = day1.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(126384);
        });
    }

    @Test
    void should_solve_day21_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day21/day21-input.txt";

            //When
            Day21 day1 = new Day21();
            var result = day1.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(212488);
        });
    }

    @Test
    void should_solve_day21_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day21/day21-input.txt";

            //When
            Day21 day1 = new Day21();
            var result = day1.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(258263972600402L);
        });
    }

}
