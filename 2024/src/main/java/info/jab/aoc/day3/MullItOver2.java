package info.jab.aoc.day3;

import java.util.stream.Gatherers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

import com.putoet.resources.ResourceLines;

public class MullItOver2 {

    private static final Pattern PATTERN = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");

    private Stream<Instruction> parseLine(final String line) {
        return PATTERN.matcher(line).results()
            .map(result -> switch (result.group()) {
                case "do()" -> new Do();
                case "don't()" -> new Dont();
                default -> new Mul(
                    Integer.parseInt(result.group(1)),
                    Integer.parseInt(result.group(2)));
            });
    }

    sealed interface Instruction permits Mul, Do, Dont { }
    record Mul(int m, int n) implements Instruction { }
    record Do() implements Instruction { }
    record Dont() implements Instruction { }

    public Integer solvePartOne1(String fileName) {
        var input = ResourceLines.list(fileName);
        return input.stream()
                .flatMap(line -> parseLine(line))
                .filter(instruction -> instruction instanceof Mul)
                .map(instruction -> (Mul) instruction)
                .mapToInt(mul -> mul.m() * mul.n())
                .sum();
    }

    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.list(fileName);
        return input.stream()
                .flatMap(this::parseLine)
                .flatMap(instruction -> instruction instanceof Mul mul ? Stream.of(mul) : Stream.empty())
                .mapToInt(mul -> mul.m() * mul.n())
                .sum();
    }

    record Acc(boolean enabled, long sum) { }

    public Integer solvePartTwo1(String fileName) {
        var input = ResourceLines.list(fileName);
        return (int) input.stream()
                .flatMap(this::parseLine)
                .reduce(
                        new Acc(true, 0),
                        (acc, instruction) -> switch (instruction) {
                            case Do() -> new Acc(true, acc.sum());
                            case Dont() -> new Acc(false, acc.sum());
                            case Mul(int m, int n) -> acc.enabled() ? new Acc(true, acc.sum() + m * n) : acc;
                        },
                        (acc1, acc2) -> acc1
                )
                .sum();
    }

    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list(fileName);
        return (int) input.stream()
                .flatMap(line -> parseLine(line))
                .gather(Gatherers.fold(
                        () -> new Acc(true, 0),
                        (acc, instruction) ->
                                switch (instruction) {
                                    case Do() -> new Acc(true, acc.sum());
                                    case Dont() -> new Acc(false, acc.sum());
                                    case Mul(int m, int n) -> acc.enabled() ? new Acc(true, acc.sum() + m * n) : acc;
                                }
                ))
                .findAny().get().sum();
    }
}
