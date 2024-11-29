package jab.aoc.day6;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day6Test {

    @Test
    void given_sampleData_when_execute_day6_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day6/input-sample.txt";

        //When
        Day6 day6 = new Day6();
        var result = day6.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(List.of(7, 5, 6, 10, 11));
    }

    @Test
    void given_data_when_execute_day6_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day6/input.txt";

        //When
        Day6 day6 = new Day6();
        var result = day6.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(List.of(1142));
    }

    @Test
    void given_sampleData_when_execute_day6_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day6/input-sample.txt";

        //When
        Day6 day6 = new Day6();
        var result = day6.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(List.of(19, 23, 23, 29, 26));
    }

    @Test
    void given_data_when_execute_day6_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day6/input.txt";

        //When
        Day6 day6 = new Day6();
        var result = day6.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(List.of(2803));
    }
}
