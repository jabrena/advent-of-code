package info.jab.aoc.day4;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void should_solve_day4_part1_examples() {
        var day = new Day4();
        
        // Test with examples from problem statement
        then(day.isRealRoom("aaaaa-bbb-z-y-x-123[abxyz]")).isTrue();
        then(day.isRealRoom("a-b-c-d-e-f-g-h-987[abcde]")).isTrue();
        then(day.isRealRoom("not-a-real-room-404[oarel]")).isTrue();
        then(day.isRealRoom("totally-real-room-200[decoy]")).isFalse();
        
        // Test sector ID sum calculation
        var testInput = java.util.List.of(
            "aaaaa-bbb-z-y-x-123[abxyz]",
            "a-b-c-d-e-f-g-h-987[abcde]", 
            "not-a-real-room-404[oarel]",
            "totally-real-room-200[decoy]"
        );
        then(day.sumRealRoomSectorIds(testInput)).isEqualTo(1514);
    }
    
    @Test
    void should_solve_day4_part2_example() {
        var day = new Day4();
        
        // Test decryption example from problem statement
        then(day.decryptRoomName("qzmt-zixmtkozy-ivhz", 343)).isEqualTo("very encrypted name");
    }
    
    @Test
    void should_solve_day4_part1() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(409147);
    }
    
    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(991);
    }
}