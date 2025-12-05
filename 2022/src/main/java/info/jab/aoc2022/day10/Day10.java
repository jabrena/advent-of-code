package info.jab.aoc2022.day10;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Day10 implements Day<String> {

    private static final Pattern SPACE_SEPARATOR_PATTERN = Pattern.compile(" ");

    record Tuple(Integer cycle, Integer value) {}

    private List<Tuple> transform(List<String> data) {
        Integer value = 1;
        Integer cycle = 1;
        List<Tuple> list = new ArrayList<>();
        for (String line : data) {
            String[] split = SPACE_SEPARATOR_PATTERN.split(line);

            //Case: noop
            if (Instructions.fromValue(split[0]) == Instructions.NOOP) {
                list.add(new Tuple(cycle, value));
                cycle++;
            } else if (Instructions.fromValue(split[0]) == Instructions.ADDX) {
                //Case: addx
                list.add(new Tuple(cycle, value));
                cycle++;
                list.add(new Tuple(cycle, value));
                cycle++;
                value += Integer.parseInt(split[1]);
            }
        }
        return list;
    }

    public String getPart1Result(String fileName) {
        //Source
        var source = ResourceLines.list("/" + fileName);

        //Processor
        var values = transform(source);

        //Sink
        List<Integer> cycles = List.of(20, 60, 100, 140, 180, 220);
        var score = cycles
            .stream()
            .map(cycle -> {
                // @formatter:off
                Tuple result = values.stream()
                    .filter(v -> v.cycle.equals(cycle))
                    .findFirst()
                    .orElseThrow();
                // @formatter:on
                return cycle * result.value;
            })
            .reduce(0, Integer::sum);
        return String.valueOf(score);
    }

    public String getPart2Result(String fileName) {
        //Source
        var list = ResourceLines.list("/" + fileName);

        //Transform
        var values = transform(list);

        //Sink
        List<String> screen = new ArrayList<>();
        screen.add("");
        for (var i : values) {
            if ((i.cycle - 1) % 40 == 0) {
                screen.add("");
            }
            // @formatter:off
            screen.set(
                screen.size() - 1,
                screen.get(screen.size() - 1) + (
                            ((i.cycle - 1) % 40 == i.value - 1
                            || (i.cycle - 1) % 40 == i.value
                            || (i.cycle - 1) % 40 == i.value + 1) ? "#" : ".")
            );
            // @formatter:on
        }
        return String.join("\n", screen);
    }
}
