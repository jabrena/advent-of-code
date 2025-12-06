package com.putoet.grid;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridDirectionsTest {

    private Grid createTestGrid() {
        // Create a 5x5 grid:
        // 01234
        // abcde
        // fghij
        // klmno
        // pqrst
        return new Grid(GridUtils.of(List.of(
                "01234",
                "abcde",
                "fghij",
                "klmno",
                "pqrst"
        )));
    }

    @Test
    void getNeighborsWithPoint_CardinalOnly() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2); // 'h'

        final var neighbors = GridDirections.getNeighbors(grid, center);

        assertEquals(4, neighbors.size());
        assertTrue(neighbors.contains(Point.of(1, 2))); // 'g'
        assertTrue(neighbors.contains(Point.of(3, 2))); // 'i'
        assertTrue(neighbors.contains(Point.of(2, 1))); // 'c'
        assertTrue(neighbors.contains(Point.of(2, 3))); // 'm'
    }

    @Test
    void getNeighborsWithPoint_IncludeDiagonals() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2); // 'h'

        final var neighbors = GridDirections.getNeighbors(grid, center, true);

        assertEquals(8, neighbors.size());
        // Cardinal
        assertTrue(neighbors.contains(Point.of(1, 2))); // 'g'
        assertTrue(neighbors.contains(Point.of(3, 2))); // 'i'
        assertTrue(neighbors.contains(Point.of(2, 1))); // 'c'
        assertTrue(neighbors.contains(Point.of(2, 3))); // 'm'
        // Diagonal
        assertTrue(neighbors.contains(Point.of(1, 1))); // 'b'
        assertTrue(neighbors.contains(Point.of(3, 1))); // 'd'
        assertTrue(neighbors.contains(Point.of(1, 3))); // 'l'
        assertTrue(neighbors.contains(Point.of(3, 3))); // 'n'
    }

    @Test
    void getNeighborsWithPoint_Corner() {
        final var grid = createTestGrid();
        final var corner = Point.of(0, 0); // '0'

        final var neighbors = GridDirections.getNeighbors(grid, corner);

        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(Point.of(1, 0))); // '1'
        assertTrue(neighbors.contains(Point.of(0, 1))); // 'a'
    }

    @Test
    void getNeighborsWithPoint_Edge() {
        final var grid = createTestGrid();
        final var edge = Point.of(0, 2); // 'f'

        final var neighbors = GridDirections.getNeighbors(grid, edge);

        assertEquals(3, neighbors.size());
        assertTrue(neighbors.contains(Point.of(1, 2))); // 'g'
        assertTrue(neighbors.contains(Point.of(0, 1))); // 'a'
        assertTrue(neighbors.contains(Point.of(0, 3))); // 'k'
    }

    @Test
    void getNeighborsWithCoordinates() {
        final var grid = createTestGrid();

        final var neighbors = GridDirections.getNeighbors(grid, 2, 2);

        assertEquals(4, neighbors.size());
        assertTrue(neighbors.contains(Point.of(1, 2)));
        assertTrue(neighbors.contains(Point.of(3, 2)));
        assertTrue(neighbors.contains(Point.of(2, 1)));
        assertTrue(neighbors.contains(Point.of(2, 3)));
    }

    @Test
    void getNeighborsWithCoordinates_IncludeDiagonals() {
        final var grid = createTestGrid();

        final var neighbors = GridDirections.getNeighbors(grid, 2, 2, true);

        assertEquals(8, neighbors.size());
    }

    @Test
    void getNeighborsWithCustomDirections() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2);
        final var directions = List.of(Point.NORTH, Point.SOUTH);

        final var neighbors = GridDirections.getNeighbors(grid, center, directions);

        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(Point.of(2, 1))); // 'c'
        assertTrue(neighbors.contains(Point.of(2, 3))); // 'm'
    }

    @Test
    void getNeighbors_NullGrid() {
        final var point = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.getNeighbors(null, point));
    }

    @Test
    void getNeighbors_NullPoint() {
        final var grid = createTestGrid();

        assertThrows(NullPointerException.class, () ->
                GridDirections.getNeighbors(grid, (Point) null));
    }

    @Test
    void getNeighbors_NullDirections() {
        final var grid = createTestGrid();
        final var point = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.getNeighbors(grid, point, (List<Point>) null));
    }

    @Test
    void countNeighborsWithPoint_CardinalOnly() {
        final var grid = createTestGrid();
        grid.set(1, 2, '#');
        grid.set(3, 2, '#');
        grid.set(2, 1, '#');
        final var center = Point.of(2, 2);

        final var count = GridDirections.countNeighbors(grid, center, c -> c == '#');

        assertEquals(3, count);
    }

    @Test
    void countNeighborsWithPoint_IncludeDiagonals() {
        final var grid = createTestGrid();
        grid.set(1, 2, '#');
        grid.set(3, 2, '#');
        grid.set(2, 1, '#');
        grid.set(1, 1, '#');
        grid.set(3, 3, '#');
        final var center = Point.of(2, 2);

        final var count = GridDirections.countNeighbors(grid, center, c -> c == '#', true);

        assertEquals(5, count);
    }

    @Test
    void countNeighborsWithPoint_NoMatches() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2);

        final var count = GridDirections.countNeighbors(grid, center, c -> c == '#');

        assertEquals(0, count);
    }

    @Test
    void countNeighborsWithCoordinates() {
        final var grid = createTestGrid();
        grid.set(1, 2, '#');
        grid.set(3, 2, '#');
        grid.set(2, 1, '#');

        final var count = GridDirections.countNeighbors(grid, 2, 2, c -> c == '#');

        assertEquals(3, count);
    }

    @Test
    void countNeighborsWithCoordinates_IncludeDiagonals() {
        final var grid = createTestGrid();
        grid.set(1, 2, '#');
        grid.set(3, 2, '#');
        grid.set(2, 1, '#');
        grid.set(1, 1, '#');

        final var count = GridDirections.countNeighbors(grid, 2, 2, c -> c == '#', true);

        assertEquals(4, count);
    }

    @Test
    void countNeighbors_NullGrid() {
        final var point = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.countNeighbors(null, point, c -> c == '#'));
    }

    @Test
    void countNeighbors_NullPoint() {
        final var grid = createTestGrid();

        assertThrows(NullPointerException.class, () ->
                GridDirections.countNeighbors(grid, (Point) null, c -> c == '#'));
    }

    @Test
    void countNeighbors_NullCondition() {
        final var grid = createTestGrid();
        final var point = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.countNeighbors(grid, point, null));
    }

    @Test
    void getNeighborValuesWithPoint_CardinalOnly() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2); // 'h'

        final var values = GridDirections.getNeighborValues(grid, center);

        assertEquals(4, values.size());
        assertTrue(values.contains('g'));
        assertTrue(values.contains('i'));
        assertTrue(values.contains('c'));
        assertTrue(values.contains('m'));
    }

    @Test
    void getNeighborValuesWithPoint_IncludeDiagonals() {
        final var grid = createTestGrid();
        final var center = Point.of(2, 2); // 'h'

        final var values = GridDirections.getNeighborValues(grid, center, true);

        assertEquals(8, values.size());
        assertTrue(values.contains('g'));
        assertTrue(values.contains('i'));
        assertTrue(values.contains('c'));
        assertTrue(values.contains('m'));
        assertTrue(values.contains('b'));
        assertTrue(values.contains('d'));
        assertTrue(values.contains('l'));
        assertTrue(values.contains('n'));
    }

    @Test
    void getNeighborValuesWithCoordinates() {
        final var grid = createTestGrid();

        final var values = GridDirections.getNeighborValues(grid, 2, 2);

        assertEquals(4, values.size());
        assertTrue(values.contains('g'));
        assertTrue(values.contains('i'));
        assertTrue(values.contains('c'));
        assertTrue(values.contains('m'));
    }

    @Test
    void getNeighborValuesWithCoordinates_IncludeDiagonals() {
        final var grid = createTestGrid();

        final var values = GridDirections.getNeighborValues(grid, 2, 2, true);

        assertEquals(8, values.size());
    }

    @Test
    void getNeighborValues_NullGrid() {
        final var point = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.getNeighborValues(null, point));
    }

    @Test
    void getNeighborValues_NullPoint() {
        final var grid = createTestGrid();

        assertThrows(NullPointerException.class, () ->
                GridDirections.getNeighborValues(grid, (Point) null));
    }

    @Test
    void scanDirection_Horizontal_StopAtCondition() {
        final var grid = createTestGrid();
        grid.set(3, 2, '#'); // Set 'i' to '#'
        final var start = Point.of(0, 2); // 'f'
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(3, scanned.size());
        assertEquals(Point.of(0, 2), scanned.get(0)); // 'f'
        assertEquals(Point.of(1, 2), scanned.get(1)); // 'g'
        assertEquals(Point.of(2, 2), scanned.get(2)); // 'h'
        // Should stop before 'i' which is '#'
    }

    @Test
    void scanDirection_Vertical_StopAtCondition() {
        final var grid = createTestGrid();
        grid.set(2, 3, '#'); // Set 'm' to '#'
        final var start = Point.of(2, 0); // '2'
        final var direction = Point.NORTH; // NORTH = (0, 1) increases y

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(3, scanned.size());
        assertEquals(Point.of(2, 0), scanned.get(0)); // '2'
        assertEquals(Point.of(2, 1), scanned.get(1)); // 'c'
        assertEquals(Point.of(2, 2), scanned.get(2)); // 'h'
        // Should stop before 'm' which is '#'
    }

    @Test
    void scanDirection_StopAtBoundary() {
        final var grid = createTestGrid();
        final var start = Point.of(0, 2); // 'f'
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == 'X');

        assertEquals(5, scanned.size());
        assertEquals(Point.of(0, 2), scanned.get(0)); // 'f'
        assertEquals(Point.of(1, 2), scanned.get(1)); // 'g'
        assertEquals(Point.of(2, 2), scanned.get(2)); // 'h'
        assertEquals(Point.of(3, 2), scanned.get(3)); // 'i'
        assertEquals(Point.of(4, 2), scanned.get(4)); // 'j'
        // Should stop at boundary
    }

    @Test
    void scanDirection_StartAtBoundary() {
        final var grid = createTestGrid();
        final var start = Point.of(4, 2); // 'j'
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(1, scanned.size());
        assertEquals(Point.of(4, 2), scanned.get(0)); // 'j'
        // Should stop immediately at boundary
    }

    @Test
    void scanDirection_StartOutsideBoundary() {
        final var grid = createTestGrid();
        final var start = Point.of(5, 2); // Outside grid
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(0, scanned.size());
        // Should return empty list immediately
    }

    @Test
    void scanDirection_StopAtFirstMatch() {
        final var grid = createTestGrid();
        grid.set(1, 2, '#'); // Set 'g' to '#'
        grid.set(3, 2, '#'); // Set 'i' to '#'
        final var start = Point.of(0, 2); // 'f'
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(1, scanned.size());
        assertEquals(Point.of(0, 2), scanned.get(0)); // 'f'
        // Should stop at first '#' which is 'g'
    }

    @Test
    void scanDirection_WithCoordinates() {
        final var grid = createTestGrid();
        grid.set(3, 2, '#');
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, 0, 2, direction, c -> c == '#');

        assertEquals(3, scanned.size());
        assertEquals(Point.of(0, 2), scanned.get(0));
        assertEquals(Point.of(1, 2), scanned.get(1));
        assertEquals(Point.of(2, 2), scanned.get(2));
    }

    @Test
    void scanDirection_Diagonal() {
        final var grid = createTestGrid();
        grid.set(3, 3, '#'); // Set 'n' to '#'
        final var start = Point.of(0, 0); // '0'
        final var direction = Point.NORTH_EAST; // NORTH_EAST = (1, 1) increases both x and y

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(3, scanned.size());
        assertEquals(Point.of(0, 0), scanned.get(0)); // '0'
        assertEquals(Point.of(1, 1), scanned.get(1)); // 'b'
        assertEquals(Point.of(2, 2), scanned.get(2)); // 'h'
        // Should stop before 'n' which is '#'
    }

    @Test
    void scanDirection_NullGrid() {
        final var start = Point.of(1, 1);
        final var direction = Point.EAST;

        assertThrows(NullPointerException.class, () ->
                GridDirections.scanDirection(null, start, direction, c -> c == '#'));
    }

    @Test
    void scanDirection_NullStart() {
        final var grid = createTestGrid();
        final var direction = Point.EAST;

        assertThrows(NullPointerException.class, () ->
                GridDirections.scanDirection(grid, (Point) null, direction, c -> c == '#'));
    }

    @Test
    void scanDirection_NullDirection() {
        final var grid = createTestGrid();
        final var start = Point.of(1, 1);

        assertThrows(NullPointerException.class, () ->
                GridDirections.scanDirection(grid, start, null, c -> c == '#'));
    }

    @Test
    void scanDirection_NullStopCondition() {
        final var grid = createTestGrid();
        final var start = Point.of(1, 1);
        final var direction = Point.EAST;

        assertThrows(NullPointerException.class, () ->
                GridDirections.scanDirection(grid, start, direction, null));
    }

    @Test
    void scanDirection_EmptyGrid() {
        final var grid = new Grid(GridUtils.of(List.of("")));
        final var start = Point.of(0, 0);
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(0, scanned.size());
    }

    @Test
    void scanDirection_SingleCell() {
        final var grid = new Grid(GridUtils.of(List.of("X")));
        final var start = Point.of(0, 0);
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(1, scanned.size());
        assertEquals(Point.of(0, 0), scanned.get(0));
    }

    @Test
    void scanDirection_StopAtStart() {
        final var grid = createTestGrid();
        grid.set(2, 2, '#'); // Set 'h' to '#'
        final var start = Point.of(2, 2);
        final var direction = Point.EAST;

        final var scanned = GridDirections.scanDirection(grid, start, direction, c -> c == '#');

        assertEquals(0, scanned.size());
        // Should stop immediately because start point matches condition
    }
}
