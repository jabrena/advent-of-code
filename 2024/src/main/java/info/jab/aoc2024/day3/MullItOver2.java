package info.jab.aoc2024.day3;

import java.util.stream.Gatherers;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class MullItOver2 implements Solver<Integer> {

    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking with alternation patterns.
     */
    private static final int MAX_INPUT_LENGTH = 1_000_000;

    private static final Pattern PATTERN = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");

    sealed interface Instruction permits Mul, Do, Dont { }
    record Mul(int m, int n) implements Instruction { }
    record Do() implements Instruction { }
    record Dont() implements Instruction { }

    private Stream<Instruction> parseLine(final String line) {
        if (line.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
        }
        
        return PATTERN.matcher(line).results()
            .map(result -> switch (result.group()) {
                case "do()" -> new Do();
                case "don't()" -> new Dont();
                default -> new Mul(
                    Integer.parseInt(result.group(1)),
                    Integer.parseInt(result.group(2)));
            });
    }

    public Integer solvePartOne1(String fileName) {
        var input = ResourceLines.list(fileName);
        return input.stream()
                .flatMap(this::parseLine)
                .filter(Mul.class::isInstance)
                .map(Mul.class::cast)
                .mapToInt(mul -> mul.m() * mul.n())
                .sum();
    }

    @Override
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

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list(fileName);
        return (int) input.stream()
                .flatMap(this::parseLine)
                .gather(Gatherers.fold(
                        () -> new Acc(true, 0),
                        (acc, instruction) ->
                                switch (instruction) {
                                    case Do() -> new Acc(true, acc.sum());
                                    case Dont() -> new Acc(false, acc.sum());
                                    case Mul(int m, int n) -> acc.enabled() ? new Acc(true, acc.sum() + m * n) : acc;
                                }
                ))
                .findAny().orElseThrow(() -> new IllegalStateException("No result found")).sum();
    }
}
