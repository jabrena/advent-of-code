package jab.aoc.day15;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

public class Day15Test {

    @Test
    void given_sampleData_when_execute_day15_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day15/input-sample.txt";

        //When
        var y = 10;
        Day15 day13 = new Day15();
        var result = day13.getPart1Result(fileName, y);

        //Then
        then(result).isEqualTo(26);
    }

    @Test
    void given_data_when_execute_day15_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day15/input.txt";

        //When
        var y = 2000000;
        Day15 day13 = new Day15();
        var result = day13.getPart1Result(fileName, y);

        //Then
        then(result).isEqualTo(4582667);
    }

    @Test
    void given_sampleData_when_execute_day15_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day15/input-sample.txt";

        //When
        Day15 day13 = new Day15();
        var result = day13.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(108000000);
    }

    @Test
    void given_data_when_execute_day15_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day15/input.txt";

        //When
        Day15 day13 = new Day15();
        var result = day13.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(10961118625406L);
    }
}
