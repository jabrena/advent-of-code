package jab.aoc.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day1Test {

    @Test
    void given_sampleData_when_execute_day1_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day1/day1-input-sample.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(24000);
    }

    @Test
    void given_sampleData_when_execute_day1_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day1/day1-input-sample.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(45000);
    }

    @Test
    void given_data_when_execute_day1_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day1/day1-input.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(69177);
    }

    @Test
    void given_data_when_execute_day1_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day1/day1-input.txt";

        //When
        Day1 day1 = new Day1();
        var result = day1.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(207456);
    }
}
