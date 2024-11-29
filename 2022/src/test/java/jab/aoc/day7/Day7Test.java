package jab.aoc.day7;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day7Test {

    @Test
    void given_sampleData_when_execute_day7_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day7/input-sample.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(95437);
    }

    @Test
    void given_data_when_execute_day7_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day7/input.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1182909);
    }

    @Test
    void given_sampleData_when_execute_day7_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day7/input-sample.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(24933642);
    }

    @Test
    void given_data_when_execute_day7_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day7/input.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2832508);
    }
}
