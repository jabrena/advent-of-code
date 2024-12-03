package info.jab.aoc;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.putoet.grid.Point;
import com.putoet.grid.Points;

class PointDemoTest {

    @Test
    void should_print_a_point() {
        //Given
        //When
        var point = new PointDemo();
        var myPoint = point.point1();

        //Then
        then(myPoint).isNotNull();
    }

    @Test
    void adjacent() {
        var adjacent = Point.ORIGIN.adjacent();
        System.out.println(adjacent);
        assertEquals(4, adjacent.size());
        assertTrue(adjacent.containsAll(List.of(
                Point.of(1, 0),
                Point.of(0, 1),
                Point.of(-1, 0),
                Point.of(0, -1)
        )));

        adjacent = Point.ORIGIN.adjacent(Points.directionsAll());
        System.out.println(adjacent);
        assertEquals(8, adjacent.size());
        assertTrue(adjacent.containsAll(List.of(
                Point.of(1, 0),
                Point.of(1, 1),
                Point.of(0, 1),
                Point.of(-1, 1),
                Point.of(-1, 0),
                Point.of(-1, -1),
                Point.of(0, -1),
                Point.of(1, -1)
        )));
    }

}
