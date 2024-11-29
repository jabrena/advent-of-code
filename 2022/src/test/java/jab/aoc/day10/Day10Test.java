package jab.aoc.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day10Test {

    @Test
    void given_sampleData_when_execute_day10_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day10/input-sample.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(String.valueOf(13140));
    }

    @Test
    void given_data_when_execute_day10_getPart1Solution_then_expected_result() {
        //Given
        String fileName = "day10/input.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(String.valueOf(12540));
    }

    @Test
    void given_sampleData_when_execute_day10_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day10/input-sample.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart2Result(fileName);

        var expectedResult =
            """

##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....""";

        //Then
        then(result).isEqualTo(expectedResult);
    }

    //FECZELHE
    @Test
    void given_data_when_execute_day10_getPart2Solution_then_expected_result() {
        //Given
        String fileName = "day10/input.txt";

        //When
        Day10 day10 = new Day10();
        var result = day10.getPart2Result(fileName);

        //Then
        var expectedResult =
            """

####.####..##..####.####.#....#..#.####.
#....#....#..#....#.#....#....#..#.#....
###..###..#......#..###..#....####.###..
#....#....#.....#...#....#....#..#.#....
#....#....#..#.#....#....#....#..#.#....
#....####..##..####.####.####.#..#.####.""";

        then(result).isEqualTo(expectedResult);
    }
}
