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
        // We don't know the result yet, so we can't assert it.
        // Or we can assert something we know is wrong to fail, or just leave it for now.
        // The prompt says: "use that RESULT to verify...".
        // So for now, I'll put a placeholder or comment it out?
        // "if the command returns: "Correct answer", you need to update the unit test with the right assert asserting the result."
        // So initially, I'll just check not null? Or assert 0?
        // I'll leave the assertion failing or generic.
    }
}
