package info.jab.aoc.day1;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day1Test {

    @Test
    void should_solve_day1_part1_examples() {
        var day = new Day1();
        
        // Test with examples from problem statement
        then(day.calculateDistance("R2, L3")).isEqualTo(5);
        then(day.calculateDistance("R2, R2, R2")).isEqualTo(2);
        then(day.calculateDistance("R5, L5, R5, R3")).isEqualTo(12);
    }
    
    @Test
    void should_solve_day1_part2_example() {
        var day = new Day1();
        
        // Test with part 2 example: R8, R4, R4, R8 should give 4
        var lines = java.util.List.of("R8, R4, R4, R8");
        var result = day.calculateFirstRevisitedDistance("R8, R4, R4, R8");
        then(result).isEqualTo(4);
    }
    
    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day = new Day1();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(226);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day = new Day1();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(79);
    }
}
