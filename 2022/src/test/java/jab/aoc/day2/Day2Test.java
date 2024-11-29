package jab.aoc.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day2Test {

    @Test
    void given_sampleData_when_execute_day2_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day2/day2-input-sample.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(15);
    }

    @Test
    void given_sampleData_when_execute_day2_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day2/day2-input-sample.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(12);
    }

    @Test
    void given_data_when_execute_day2_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day2/day2-input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(14827);
    }

    @Test
    void given_data_when_execute_day2_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day2/day2-input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(13889);
    }
}
