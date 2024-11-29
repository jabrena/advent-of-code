package jab.aoc.day10;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Instructions {
    addx("addx"),
    noop("noop");

    private static final Map<String, Instructions> MAP = Stream
        .of(Instructions.values())
        .collect(Collectors.toMap(Object::toString, Function.identity()));
    private final String value;

    Instructions(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Instructions fromValue(String value) {
        return MAP.get(value);
    }
}
