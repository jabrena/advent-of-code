package jab.aoc.day5;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day5Test {

    @Test
    void given_sampleData_when_execute_day5_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day5/input-sample.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("CMZ");
    }

    @Test
    void given_data_when_execute_day5_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day5/input.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("TLFGBZHCN");
    }

    @Test
    void given_sampleData_when_execute_day5_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day5/input-sample.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("MCD");
    }

    @Test
    void given_data_when_execute_day5_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day5/input.txt";

        //When
        Day5 day5 = new Day5();
        var result = day5.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("QRQFHFWCL");
    }
}
