package info.jab.aoc.day7;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day7Test {

    @Test
    void should_solve_day7_part1_examples() {
        var day = new Day7();
        
        // Test with examples from problem statement
        then(day.supportsTLS("abba[mnop]qrst")).isTrue();
        then(day.supportsTLS("abcd[bddb]xyyx")).isFalse();
        then(day.supportsTLS("aaaa[qwer]tyui")).isFalse();
        then(day.supportsTLS("ioxxoj[asdfgh]zxcvbn")).isTrue();
    }
    
    @Test
    void should_solve_day7_part1() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day = new Day7();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(110);
    }
    
    @Test
    void should_solve_day7_part2_examples() {
        var day = new Day7();
        
        // Test with examples from problem statement
        then(day.supportsSSL("aba[bab]xyz")).isTrue();
        then(day.supportsSSL("xyx[xyx]xyx")).isFalse();
        then(day.supportsSSL("aaa[kek]eke")).isTrue();
        then(day.supportsSSL("zazbz[bzb]cdb")).isTrue();
    }
    
    @Test
    void should_solve_day7_part2() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day = new Day7();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(242);
    }
}