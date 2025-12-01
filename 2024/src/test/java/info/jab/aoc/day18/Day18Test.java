package info.jab.aoc.day18;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;


class Day18Test {

    @Test
    void should_solve_day18_part1_sample() {
        //Given
        String fileName = "/day18/day18-input-sample.txt";

        //When
        var day = new Day18();
        var result = day.getPart1Result(fileName, 7, 12, false);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("22");
    }

    @Test
    void should_solve_day18_part1() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart1Result(fileName, 71, 1024, false);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("506");
    }

    @Test
    void should_solve_day18_part2_sample() {
        //Given
        String fileName = "/day18/day18-input-sample.txt";

        //When
        var day = new Day18();
        var result = day.getPart2Result(fileName, 7, 12, false);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("6,1");
    }

    @Test
    void should_solve_day18_part2() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart2Result(fileName, 71, 1024, false);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("62,6");
    }

}
