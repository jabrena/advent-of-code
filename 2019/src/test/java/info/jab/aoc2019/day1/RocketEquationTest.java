package info.jab.aoc2019.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * https://adventofcode.com/2019/day/1
 */
class RocketEquationTest {

    /**
     * For a mass of 12, divide by 3 and round down to get 4, then subtract 2 to get 2.
     * For a mass of 14, dividing by 3 and rounding down still yields 4, so the fuel required is also 2.
     * For a mass of 1969, the fuel required is 654.
     * For a mass of 100756, the fuel required is 33583.
     */
    @Test
    @Timeout(30)
    void should_calculate_fuel_correctly() {
        //Given
        RocketEquation santa = new RocketEquation();

        //When & Then
        then(santa.calculateFuel(12)).isEqualTo(2);
        then(santa.calculateFuel(14)).isEqualTo(2);
        then(santa.calculateFuel(1969)).isEqualTo(654);
        then(santa.calculateFuel(100756)).isEqualTo(33583);
    }

    @Test
    @Timeout(30)
    void should_calculate_total_fuel_requirement() {
        //Given
        RocketEquation santa = new RocketEquation();

        //When
        int result = santa.getTotalFuelRequeriment();

        //Then
        then(result).isEqualTo(3182375);
    }
}

