package info.jab.aoc.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day11Test {

    // 2097446912  14168  4048  2 0 2 4 40                          48 2024     40 48 80 96 2 8 6 7 6 0 3 2
    //[2097446912, 14168, 4048, , , , , , , , , , , , , , , , , 40, 48, , , , , 40, 48, 80, 96, 2, 8, 6, 7, 6, , 3, 2]
    @Test
    void should_solve_day11_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day11/day11-input-sample.txt";

            //When
            var day = new Day11();
            var result = day.getPart1Result(fileName, 6);

            //Then
            then(result).isEqualTo(22);
        });
    }

    @Disabled
    @Test
    void should_solve_day11_part1_with_sample2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day11/day11-input-sample.txt";

            //When
            var day = new Day11();
            var result = day.getPart1Result(fileName, 25);

            //Then
            then(result).isEqualTo(55312);
        });
    }

    @Disabled
    @Test
    void should_solve_day11_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day11/day11-input.txt";

            //When
            var day = new Day11();
            var result = day.getPart1Result(fileName, 25);

            //Then
            then(result).isEqualTo(55312);
        });
    }

    @Disabled
    @Test
    void should_solve_day11_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day11/day11-input.txt";

            //When
            var day = new Day11();
            var result = day.getPart1Result(fileName, 75);

            //Then
            then(result).isEqualTo(55312);
        });
    }

}
