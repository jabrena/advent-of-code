package info.jab.aoc2019.day7;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class AmplificationCircuitTest {

    @Test
    @Timeout(30)
    void should_get_max_signal() {
        //Given
        AmplificationCircuit amplificationCircuit = new AmplificationCircuit();

        //When
        long result = amplificationCircuit.getMaxSign();

        //Then
        then(result).isEqualTo(0L);
    }
}

