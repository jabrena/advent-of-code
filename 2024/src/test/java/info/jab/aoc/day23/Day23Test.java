package info.jab.aoc.day23;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day23Test {

    @Test
    void should_solve_day1_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input-sample.txt";

            //When
            var day = new Day23();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("7");
        });
    }

    @Test
    void should_solve_day1_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input.txt";

            //When
            var day = new Day23();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("1240");
        });
    }

    @Test
    void should_solve_day1_part2_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input-sample.txt";

            //When
            var day = new Day23();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("co,de,ka,ta");
        });
    }

    @Test
    void should_solve_day1_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day23/day23-input.txt";

            //When
            var day = new Day23();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("am,aq,by,ge,gf,ie,mr,mt,rw,sn,te,yi,zb");
        });
    }
}
