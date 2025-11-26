package info.jab.aoc.day17;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day17Test {

    @Test
    void should_solve_day17_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day17/day17-input.txt";

            //When
            var day = new Day17();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("DDURRLRRDD");
        });
    }

    @Test
    void should_solve_day17_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day17/day17-input.txt";

            //When
            var day = new Day17();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("436");
        });
    }

}
