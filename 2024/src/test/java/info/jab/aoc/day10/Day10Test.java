package info.jab.aoc.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day10Test {

    @Test
    void should_solve_day10_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input-sample.txt";

            //When
            var day = new Day10();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(36);
        });
    }

    @Test
    void should_solve_day10_part1() {
        System.out.println("Part 1: Recursion");
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(574);
        });
    }

    @Test
    void should_solve_day10_part1_bfs() {
        System.out.println("Part 1: BFS");
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart1Result2(fileName);

            //Then
            then(result).isEqualTo(574);
        });
    }

    @Test
    void should_solve_day10_part2_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input-sample.txt";

            //When
            var day = new Day10();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(81);
        });
    }

    @Test
    void should_solve_day10_part2() {
        System.out.println("Part 2: Recursion");
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(1238);
        });
    }

    @Test
    void should_solve_day10_part2_bfs() {
        System.out.println("Part 2: BFS");
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart2Result2(fileName);

            //Then
            then(result).isEqualTo(1238);
        });
    }

}
