package info.jab.aoc2024.day17;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;


class Day17Test {

    @Test
    void should_solve_day17_part1_sample() {
        //Given
        String fileName = "/day17/day17-input-sample.txt";

        //When
        var day = new Day17();
        var result = day.getPart1Result(fileName);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("4,6,3,5,6,3,5,2,1,0");
    }

    @Test
    void should_solve_day17_part1() {
        //Given
        String fileName = "/day17/day17-input.txt";

        //When
        var day = new Day17();
        var result = day.getPart1Result(fileName);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("6,5,7,4,5,7,3,1,0");
    }

    /*
     *
     *  free -h
     *                 total        used        free      shared  buff/cache   available
     *  Mem:           7.7Gi       2.3Gi       2.3Gi        63Mi       3.2Gi       5.1Gi
     *
     */

    @Test
    void should_solve_day17_part2_sample() {
        //Given
        String fileName = "/day17/day17-input-sample2.txt";

        //When
        var day = new Day17();
        var result = day.getPart2Result(fileName);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("117440");
    }

    @Test
    void should_solve_day17_part2() {
        //Given
        String fileName = "/day17/day17-input.txt";

        //When
        var day = new Day17();
        var result = day.getPart2Result(fileName);

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(day).toPrintable());
        //System.out.println(GraphLayout.parseInstance(day).toFootprint());

        //Then
        then(result).isEqualTo("105875099912602");
    }

}
