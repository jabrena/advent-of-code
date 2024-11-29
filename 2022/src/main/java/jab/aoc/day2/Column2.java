package jab.aoc.day2;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Column2 {
    ROCK("X"),
    PAPER("Y"),
    SCISSOR("Z");

    private static final Map<String, Column2> MAP = Stream
        .of(Column2.values())
        .collect(Collectors.toMap(Object::toString, Function.identity()));
    private final String value;

    Column2(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Column2 fromValue(String value) {
        return MAP.get(value);
    }
}
