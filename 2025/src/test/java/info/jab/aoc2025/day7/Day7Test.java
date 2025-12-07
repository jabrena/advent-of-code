package info.jab.aoc2025.day7;

import info.jab.aoc.Day;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

public class Day7Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    public void testPart1Sample() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart1Result("/day7/day7-input-sample.txt");
        assertEquals(21L, result);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    public void testPart1() {
        Day<Long> day7 = new Day7();
        Long result = day7.getPart1Result("/day7/day7-input.txt");
        assertEquals(1537L, result);
    }
}
