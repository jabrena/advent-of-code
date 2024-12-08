package info.jab.aoc.day8;

import com.google.common.collect.Sets;

import com.putoet.resources.ResourceLines;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class AntennaMap {

    private List<String> list;
    private Grid grid;

    public AntennaMap(String fileName) {
        list = ResourceLines.list(fileName);
        grid = new Grid(GridUtils.of(list));
    }

    private Map<Character, Set<Point>> getAntennasByType() {
        var points = grid.findAll(c -> c != '.');
        return points.stream()
            .collect(Collectors.groupingBy(
                p -> grid.get(p.x(), p.y()), // Group by the character at the point's position
                Collectors.toSet()           // Collect points into a Set
            ));
    }

    record Deltas(Integer dx, Integer dy) {}

    BiFunction<Point, Point, Deltas> getDeltas = (a, b) -> {
        Integer dx = b.x() - a.x();
        Integer dy = b.y() - a.y();
        return new Deltas(dx, dy);
    };

    //Calculate delta between 2 points
    //TODO: Candidate to promote as new Point method
    private Point getAntinode(Point a, Point b) {
        var deltas = getDeltas.apply(a, b);
        return new Point(a.x() - deltas.dx(), a.y() - deltas.dy());
    }

    //Predicate to know if a Point is inside of Grid
    Predicate<Point> isInsideOfGrid = point -> {
        return  point.x() >= 0 && point.x() < grid.maxX() &&
                point.y() >= 0 && point.y() < grid.maxY();
    };

    //https://javadoc.io/doc/com.google.guava/guava/latest/com/google/common/collect/Sets.html
    public Long countAntinodes() {
        Map<Character, Set<Point>> antennas = getAntennasByType();
        return antennas.entrySet().stream()
            .flatMap(entry -> {
                Set<Point> points = entry.getValue();
                return Sets.cartesianProduct(points, points).stream()
                    .filter(pair -> !pair.get(0).equals(pair.get(1)))
                    .flatMap(pair -> Stream.of(
                        getAntinode(pair.get(0), pair.get(1)),
                        getAntinode(pair.get(1), pair.get(0))));
            })
            .collect(Collectors.toSet()).stream()
            .filter(isInsideOfGrid).count();
    }

    //Native approach with flatmap is difficult to read the code
    private Long countAntinodes2() {
        Map<Character, Set<Point>> antennas = getAntennasByType();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return antennas.values().stream()
                .flatMap(types -> types.stream()
                    .flatMap(a -> types.stream()
                        .filter(b -> !a.equals(b))
                        .flatMap(b -> List.of(getAntinode(a, b), getAntinode(b, a)).stream())))
                .collect(Collectors.toSet()).stream()
                .filter(isInsideOfGrid).count();
        }
    }

    private Long countAntinodesImperative() {
        Map<Character, Set<Point>> antennas = getAntennasByType();
        Set<Point> antinodes = new HashSet<>();
        for (Character types : antennas.keySet()) {
            for (Point a : antennas.get(types)) {
                for (Point b : antennas.get(types)) {
                    if (a != b) {
                        antinodes.add(getAntinode(a, b));
                        antinodes.add(getAntinode(b, a));
                    }
                }
            }
        }
        return antinodes.stream().filter(isInsideOfGrid).count();
    }

    private Set<Point> calculateHarmonics(Point a, Point b) {
        var deltas = getDeltas.apply(a, b);

        //https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#iterate-T-java.util.function.UnaryOperator-
        return Stream.iterate(
            new Point(a.x(), a.y()),
            p -> isInsideOfGrid.test(p),
            p -> new Point(p.x() - deltas.dx(), p.y() - deltas.dy())
        )
        .collect(Collectors.toSet());
    }

    public Long countAntinodesWithHarmonics() {
        Map<Character, Set<Point>> antennas = getAntennasByType();

        return antennas.entrySet().stream()
            .flatMap(entry -> {
                Set<Point> points = entry.getValue();
                return Sets.cartesianProduct(points, points).stream()
                    .filter(pair -> !pair.get(0).equals(pair.get(1)))
                    .flatMap(pair -> Stream.of(
                        calculateHarmonics(pair.get(0), pair.get(1)),
                        calculateHarmonics(pair.get(1), pair.get(0))
                    ).flatMap(Set::stream)); // Flatten the sets of harmonics into one stream
            })
            .collect(Collectors.toSet()).stream().filter(isInsideOfGrid).count();
    }
}
