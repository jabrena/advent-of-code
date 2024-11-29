package jab.aoc.day15;

import com.google.common.collect.Range;
import jab.aoc.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day15 {

    // @formatter:off
    private static final String regex =
            "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)";
    private static final Pattern PATTERN = Pattern.compile(regex);

    // @formatter:on

    private record Data(Point sp, Point bp, int d) {
        boolean excludes(Point p) {
            return sp.dist(p) <= d;
        }
    }

    private record Point(int x, int y) {
        public int dist(Point p2) {
            return Math.abs(x - p2.x) + Math.abs(y - p2.y);
        }
    }

    private static List<Data> processLines(List<String> lines) {
        Function<String, List<Integer>> getListOfPoints = line -> {
            List<Integer> list = new ArrayList<>();
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                list.add(Integer.parseInt(matcher.group(1)));
                list.add(Integer.parseInt(matcher.group(2)));
                list.add(Integer.parseInt(matcher.group(3)));
                list.add(Integer.parseInt(matcher.group(4)));
            }
            return list;
        };

        return lines
            .stream()
            .map(getListOfPoints)
            .map(list -> {
                var sp = new Point(list.get(0), list.get(1));
                var bp = new Point(list.get(2), list.get(3));
                return new Data(sp, bp, sp.dist(bp));
            })
            .toList();
    }

    public Long getPart1Result(String fileName, Integer y) {
        //Source
        var fileLines = Utils.readFileToList(fileName);

        //Transform
        var processed = processLines(fileLines);
        int min = processed.stream().mapToInt(s -> s.sp().x() - s.d).min().orElseThrow();
        int max = processed.stream().mapToInt(s -> s.sp().x() + s.d).max().orElseThrow();

        System.out.println(min);
        System.out.println(max);

        //Sink
        return IntStream
            .range(min, max)
            .mapToObj(x -> new Point(x, y))
            .filter(p -> processed.stream().anyMatch(it -> it.excludes(p) && !it.bp.equals(p)))
            .count();
    }

    public Long getPart2Result(String fileName) {
        //Source
        var fileLines = Utils.readFileToList(fileName);

        //Transform
        var processed = processLines(fileLines);

        //Sink
        int max = 4000000;
        for (int y = 0; y <= max; y++) {
            var ranges = new ArrayList<Range<Integer>>();
            for (var s : processed) {
                int dx = s.d - Math.abs(y - s.sp.y());
                if (dx >= 0) {
                    ranges.add(Range.closed(s.sp.x() - dx, s.sp.x() + dx));
                }
            }

            for (int x = 0; x <= max;) {
                int xx = x;
                var range = ranges.stream().filter(r -> r.contains(xx)).findFirst();
                if (range.isPresent()) {
                    x = range.get().upperEndpoint() + 1;
                } else {
                    return (long) x * max + y;
                }
            }
        }

        return 0L; // not reached
    }
}
