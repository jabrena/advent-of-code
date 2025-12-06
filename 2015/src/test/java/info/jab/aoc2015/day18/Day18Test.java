package info.jab.aoc2015.day18;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day18Test {

    @Test
    void should_solve_day18_sample() {
        //Given
        String fileName = "/day18/day18-sample.txt";

        //When
        var lightGrid = new LightGrid();
        var result = lightGrid.solvePartOneWithSteps(fileName, 4);

        //Then
        then(result).isEqualTo(4);
    }

    @Test
    void should_solve_day18_sample_part2() {
        //Given
        String fileName = "/day18/day18-sample.txt";

        //When
        var lightGrid = new LightGrid();
        var result = lightGrid.solvePartTwoWithSteps(fileName, 5);

        //Then
        then(result).isEqualTo(17);
    }

    @Test
    void should_solve_day18_part1() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(814);
    }

    @Test
    void should_solve_day18_part2() {
        //Given
        String fileName = "/day18/day18-input.txt";

        //When
        var day = new Day18();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(924);
    }

}
