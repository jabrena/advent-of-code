package info.jab.aoc;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class GridDemoTest {

    @Test
    void should_print_a_grid_with_sample() {
        //Given
        String fileName = "/gridSample1.txt";

        //When
        var grid = new GridDemo();
        var result = grid.grid1(fileName);

        //Then
        then(result).isNotNull();
    }

    @Test
    void should_print_a_grid_created_from_scratch() {
        //Given
        //String fileName = "/gridSample1.txt";

        //When
        var grid = new GridDemo();
        var result = grid.grid2();

        //Then
        then(result).isNotNull();
    }

    @Test
    void should_print_a_grid_with_sample2() {
        //Given
        String fileName = "/gridSample2.txt";

        //When
        var grid = new GridDemo();
        var result = grid.grid3(fileName);

        //Then
        then(result).isNotNull();
    }

}
