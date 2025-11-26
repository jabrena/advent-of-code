package info.jab.aoc.day23;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.putoet.utils.Timer;

class Day23Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day23_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input.txt";

            //When
            var day = new Day23();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(10953);
        });
    }

    @Disabled
    @Test
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void should_solve_day23_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input.txt";

            //When
            var day = new Day23();
            var result = day.getPart2Result(fileName);

            // Print result for verification
            System.out.println("Part 2 Result: " + result);

            //Then
            then(result).isEqualTo(0); // Placeholder - will be updated after solution
        });
    }

}
