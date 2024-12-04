package info.jab.aoc.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.putoet.utils.Timer;

class Day4Test {

    @Test
    void should_solve_day4_part1_with_sample2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day4/day4-input-sample2.txt";

            //When
            var day = new Day4();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(18);
        });
    }

    @Test
    void should_solve_day4_part1_with_sample3() {
        Timer.run(() -> {
            //Given
            String fileName = "/day4/day4-input-sample3.txt";

            //When
            var day = new Day4();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(18);
        });
    }

    @Test
    void should_solve_day4_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day4/day4-input.txt";

            //When
            var day = new Day4();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(2464);
        });
    }

}
