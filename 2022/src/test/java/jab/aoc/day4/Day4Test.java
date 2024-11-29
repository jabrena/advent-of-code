package jab.aoc.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void given_sampleData_when_execute_day4_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day4/input-sample.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(2);
    }

    @Test
    void given_data_when_execute_day4_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day4/input.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(464);
    }

    @Test
    void given_sampleData_when_execute_day4_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day4/input-sample.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(4);
    }

    @Test
    void given_data_when_execute_day4_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day4/input.txt";

        //When
        Day4 day4 = new Day4();
        var result = day4.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(770);
    }
}
