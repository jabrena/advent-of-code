package info.jab.aoc.day20;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day20Test {

    @Test
    void should_solve_day20_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day20/day20-input-sample.txt";

            //When
            var day = new Day20();
            var result = day.getPart1Result(fileName, 64);

            //Then
            then(result).isEqualTo(1);
        });
    }

    @Test
    void should_solve_day20_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day20/day20-input.txt";

            //When
            var day = new Day20();
            var result = day.getPart1Result(fileName, 100);

            //Then
            then(result).isEqualTo(1452);
        });
    }

    @Test
    void should_solve_day20_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day20/day20-input.txt";

            //When
            var day = new Day20();
            var result = day.getPart2Result(fileName, 100);

            //Then
            then(result).isEqualTo(999556);
        });
    }

}
