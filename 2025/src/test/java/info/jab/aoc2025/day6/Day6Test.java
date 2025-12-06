package info.jab.aoc2025.day6;

import info.jab.aoc.Day;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

public class Day6Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    public void testPart1Sample() {
        Day<Long> day6 = new Day6();
        Long result = day6.getPart1Result("day6/day6-input-sample.txt");
        assertEquals(4277556L, result);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    public void testPart1() {
        Day<Long> day6 = new Day6();
        Long result = day6.getPart1Result("day6/day6-input.txt");
        // We don't know the result yet, but we want to execute it.
        // Failing for now or printing.
        System.out.println("Part 1 Result: " + result);
    }
}
