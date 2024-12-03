package info.jab.aoc.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.putoet.utils.Timer;

class Day4Test {

    @Test
    void should_solve_day3_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day4/day4-input-sample.txt";

            //When
            var day = new Day4();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(161);
        });
    }

}
