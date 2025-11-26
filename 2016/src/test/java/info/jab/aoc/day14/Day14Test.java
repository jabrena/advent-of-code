package info.jab.aoc.day14;

import com.putoet.utils.Timer;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class Day14Test {

    @Test
    void should_solve_day14_part1_example() {
        var day = new Day14();
        
        // Test with example from problem statement: salt "abc" should give 64th key at index 22728
        then(day.findNthKey("abc", 64)).isEqualTo(22728);
    }
    
    @Test
    void should_solve_day14_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day14/day14-input.txt";

            //When
            var day = new Day14();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(15168);
        });
    }
    
    @Test
    void should_solve_day14_part2_example() {
        var day = new Day14();
        
        // Test with example from problem statement: salt "abc" with key stretching should give 64th key at index 22551
        then(day.findNthKeyWithStretching("abc", 64)).isEqualTo(22551);
    }
    
    @Test
    void should_solve_day14_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day14/day14-input.txt";

            //When
            var day = new Day14();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(20864);
        });
    }
}