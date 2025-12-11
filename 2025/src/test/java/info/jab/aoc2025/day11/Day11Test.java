package info.jab.aoc2025.day11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class Day11Test {

    @Test
    void testPart1() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            Day11 day11 = new Day11();
            Long result = day11.getPart1Result("/day11/day11-input-sample.txt");
            assertEquals(5L, result);
        });
    }

    @Test
    void testPart1Solution() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            Day11 day11 = new Day11();
            Long result = day11.getPart1Result("/day11/day11-input.txt");
            assertEquals(500L, result);
        });
    }

    @Test
    void testPart2() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            Day11 day11 = new Day11();
            Long result = day11.getPart2Result("/day11/day11-part2-sample.txt");
            assertEquals(2L, result);
        });
    }

    @Test
    void testPart2Solution() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            Day11 day11 = new Day11();
            Long result = day11.getPart2Result("/day11/day11-input.txt");
            System.out.println("Part 2 Result: " + result);
        });
    }
}
