package info.jab.aoc.day8;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day8Test {

    @Test
    void should_solve_day8_part1_example() {
        var day = new Day8();
        
        // Test with example from problem statement (smaller screen 7x3)
        var instructions = java.util.List.of(
            "rect 3x2",
            "rotate column x=1 by 1", 
            "rotate row y=0 by 4",
            "rotate column x=1 by 1"
        );
        
        var result = day.countLitPixels(instructions, 7, 3);
        then(result).isEqualTo(6); // Should have 6 lit pixels after all operations
    }

    @Test
    void should_solve_day8_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day8();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("123");
        });
    }

    @Test
    void should_solve_day8_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day8();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("AFBUPZBJPS");
        });
    }
}