package info.jab.aoc2025.day7;

import info.jab.aoc.Day;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day7Test {

    @Test
    public void testPart1Sample() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart1Result("/day7/day7-input-sample.txt");
        assertEquals(21L, result);
    }

    @Test
    public void testPart1() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart1Result("/day7/day7-input.txt");
        assertEquals(1537L, result);
    }

    @Test
    public void testPart2Sample() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart2Result("/day7/day7-input-sample.txt");
        assertEquals(40L, result);
    }

    @Test
    public void testPart2() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart2Result("/day7/day7-input.txt");
        assertEquals(18818811755665L, result);
    }
}
