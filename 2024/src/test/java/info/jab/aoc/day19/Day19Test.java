package info.jab.aoc.day19;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day19Test {

    @Test
    void should_solve_day19_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input-sample.txt";

            //When
            var day = new Day19();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(6);
        });
    }

    @Test
    void should_solve_day19_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input.txt";

            //When
            var day = new Day19();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(365);
        });
    }

    @Test
    void should_solve_day19_part2_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input-sample.txt";

            //When
            var day = new Day19();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(16);
        });
    }

    @Test
    void should_solve_day19_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day19/day19-input.txt";

            //When
            var day = new Day19();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(730121486795169L);

            //System.out.println(VM.current().details());
            //System.out.println(ClassLayout.parseInstance(day).toPrintable());
            //System.out.println(GraphLayout.parseInstance(day).toFootprint());
        });
    }
}
