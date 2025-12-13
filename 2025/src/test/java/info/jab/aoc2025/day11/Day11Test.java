package info.jab.aoc2025.day11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    @Test
    void testPart1() {
        Day11 day11 = new Day11();
        Long result = day11.getPart1Result("/day11/day11-input-sample.txt");
        assertEquals(5L, result);
    }

    @Test
    void testPart1Solution() {
        Day11 day11 = new Day11();
        Long result = day11.getPart1Result("/day11/day11-input.txt");
        assertEquals(500L, result);
    }

    @Test
    void testPart2() {
        Day11 day11 = new Day11();
        Long result = day11.getPart2Result("/day11/day11-part2-sample.txt");
        assertEquals(2L, result);
    }

    @Test
    void testPart2Solution() {
        Day11 day11 = new Day11();
        Long result = day11.getPart2Result("/day11/day11-input.txt");
        assertEquals(287039700129600L, result);
    }
}
