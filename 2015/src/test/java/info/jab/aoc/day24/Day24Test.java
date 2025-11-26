package info.jab.aoc.day24;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day24Test {

    @Test
    void should_solve_day24_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day24/day24-input.txt";

            //When
            var day = new Day24();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(11846773891L);
        });
    }

    @Test
    void should_solve_day24_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day24/day24-input.txt";

            //When
            var day = new Day24();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(80393059L);
        });
    }

}