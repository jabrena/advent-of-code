package info.jab.aoc2022.day9;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Day9Test {

    @Test
    void given_sampleData_when_execute_day9_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day9/input-sample.txt";

        //When
        Day9 day9 = new Day9();
        var result = day9.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(13);
    }

}
