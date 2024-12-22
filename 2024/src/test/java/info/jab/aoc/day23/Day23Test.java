package info.jab.aoc.day23;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day23Test {

    @Test
    void should_solve_day1_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input-sample.txt";

            //When
            var day = new Day23();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(1);
        });
    }
}
