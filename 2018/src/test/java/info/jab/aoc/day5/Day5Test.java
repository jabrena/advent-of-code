package info.jab.aoc.day5;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.putoet.utils.Timer;

class Day5Test {

    @Test
    @Timeout(30)
    void should_solve_day5_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day5/day5-input.txt";

            //When
            var day5 = new Day5();
            var result = day5.solvePartOne(fileName);

            //Then
            then(result).isEqualTo(9386);
        });
    }

    @Test
    @Timeout(30)
    void should_solve_day5_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day5/day5-input.txt";

            //When
            var day5 = new Day5();
            var result = day5.solvePartTwo(fileName);

            //Then
            then(result).isEqualTo(4876);
        });
    }

}
