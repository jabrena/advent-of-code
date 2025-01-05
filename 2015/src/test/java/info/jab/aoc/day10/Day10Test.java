package info.jab.aoc.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

import info.jab.aoc.GherkinUtils;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        Timer.run(() -> {
            //Given
            String input = "1321131112";

            //var gherkinFileName = "src/test/gherkin/day10-part1.feature";
            //var gherkinDocument = GherkinUtils.getGherkinDocument(gherkinFileName);
            //System.out.println(gherkinDocument.getFeature().get().getChildren().get(0));
            
            //When
            var day = new Day10();
            var result = day.getPart1Result(input);

            //Then
            then(result).isEqualTo(492982);
        });
    }

    @Test
    void should_solve_day9_part2() {
        Timer.run(() -> {
            //Given
            String input = "1321131112";

            //When
            var day = new Day10();
            var result = day.getPart2Result(input);

            //Then
            then(result).isEqualTo(6989950);
        });
    }

}
