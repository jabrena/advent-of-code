package info.jab.aoc2019.day2;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ProgramAlarmTest {

    @Test
    @Timeout(30)
    void should_parse_and_compute_correctly() {
        //Given
        ProgramAlarm program = new ProgramAlarm();

        //When & Then
        then(program.compute(new ArrayList<>(Arrays.asList(1, 0, 0, 0, 99)), 0))
                .isEqualTo(Arrays.asList(2, 0, 0, 0, 99));
        then(program.compute(new ArrayList<>(Arrays.asList(2, 3, 0, 3, 99)), 0))
                .isEqualTo(Arrays.asList(2, 3, 0, 6, 99));
        then(program.compute(new ArrayList<>(Arrays.asList(2, 4, 4, 5, 99, 0)), 0))
                .isEqualTo(Arrays.asList(2, 4, 4, 5, 99, 9801));
        then(program.compute(new ArrayList<>(Arrays.asList(1, 1, 1, 4, 99, 5, 6, 0, 99)), 0))
                .isEqualTo(Arrays.asList(30, 1, 1, 4, 2, 5, 6, 0, 99));

        then(program.compute(new ArrayList<>(Arrays.asList(99, 0, 0, 0, 99)), 0))
                .isEqualTo(Arrays.asList(99, 0, 0, 0, 99));
    }

    @Test
    @Timeout(30)
    void should_get_position_0() {
        //Given
        ProgramAlarm program = new ProgramAlarm();

        //When
        int result = program.getPosition0();

        //Then
        then(result).isEqualTo(3058646);
    }

    @Disabled("Test not implemented yet - getPair() method returns 0")
    @Test
    @Timeout(30)
    void should_get_pair() {
        //Given
        ProgramAlarm program = new ProgramAlarm();

        //When
        int result = program.getPair();

        //Then
        then(result).isEqualTo(100);
    }
}

