package info.jab.aoc.day24;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day24Test {

    @Test
    void should_solve_day24_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day24/day24-input.txt";

            //When
            var day = new Day24();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(889779);
        });
    }
}
