package info.jab.aoc.day18;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day18Test {

    @Test
    void should_solve_day18_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day18/day18-input.txt";

            //When
            var day = new Day18();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(1956);
        });
    }
      
    @Test
    void should_solve_day18_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day18/day18-input.txt";

            //When
            var day = new Day18();
            var result = day.getPart2Result(fileName);      

            //Then
            then(result).isEqualTo(19995121);
        });
    }
}
