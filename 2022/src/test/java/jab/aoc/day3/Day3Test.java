package jab.aoc.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day3Test {

    @Test
    void given_sampleData_when_execute_day3_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day3/input-sample.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(157);
    }

    @Test
    void given_data_when_execute_day3_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day3/input.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(7746);
    }

    @Test
    void given_sampleData_when_execute_day3_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day3/input-sample.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(70);
    }

    @Test
    void given_data_when_execute_day3_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day3/input.txt";

        //When
        Day3 day3 = new Day3();
        var result = day3.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2604);
    }
}
