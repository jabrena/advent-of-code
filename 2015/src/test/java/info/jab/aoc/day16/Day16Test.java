package info.jab.aoc.day16;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day16Test {

    @Test
    void should_solve_day16_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day16/day16-input.txt";

            //When
            var day = new Day16();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(40);
        });
    }

    @Test
    void should_solve_day16_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day16/day16-input.txt";

            //When
            var day = new Day16();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(241);
        });
    }

}