package info.jab.aoc2019.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class CrossedWiresTest {

    @Test
    void should_calculate_manhattan_distance() {
        //Given
        CrossedWires crossedWires = new CrossedWires();

        //When
        int result = crossedWires.getManhattanDistance();

        //Then
        then(result).isEqualTo(0);
    }
}

