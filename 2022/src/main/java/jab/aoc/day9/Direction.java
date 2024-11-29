package jab.aoc.day9;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    R("R"),
    U("U"),
    L("L"),
    D("D");

    private static final Map<String, Direction> MAP = Stream
        .of(Direction.values())
        .collect(Collectors.toMap(Object::toString, Function.identity()));
    private final String value;

    Direction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Direction fromValue(String value) {
        return MAP.get(value);
    }
}
