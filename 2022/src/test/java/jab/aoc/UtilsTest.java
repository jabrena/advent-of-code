package jab.aoc;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void given_string_when_convertToHashSet_then_expected_result() {
        //Given
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //When
        var result = Utils.getUniqueCharactersAsHashSet(alphabet);

        //Then
        then(result.size()).isEqualTo(alphabet.length());
    }

    @Test
    void given_string_when_convertToHashSet_nonHappyPath_then_expected_result() {
        //Given
        String sample = "AAA";

        //When
        var result = Utils.getUniqueCharactersAsHashSet(sample);

        //Then
        then(result.size()).isEqualTo(1);
    }

    @Test
    void given_fileName_when_convertToHashSet_then_expected_result() {
        //Given
        String fileName = "day1/day1-input.txt";

        //When
        var result = Utils.readFileToStringF.apply(fileName);

        //Then
        then(result.length()).isGreaterThan(0);
    }
}
