package info.jab.aoc2019.day6;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class UniversalOrbitMapTest {

    @Test
    void should_calculate_total_orbits() {
        //Given
        UniversalOrbitMap universalOrbitMap = new UniversalOrbitMap();

        //When
        int result = universalOrbitMap.getTotalOrbits();

        //Then
        then(result).isEqualTo(0);
    }
}

