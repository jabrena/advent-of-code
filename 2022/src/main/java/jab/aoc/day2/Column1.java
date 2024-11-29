package jab.aoc.day2;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Column1 {
    ROCK("A"),
    PAPER("B"),
    SCISSOR("C");

    private static final Map<String, Column1> MAP = Stream
        .of(Column1.values())
        .collect(Collectors.toMap(Object::toString, Function.identity()));
    private final String value;

    Column1(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Column1 fromValue(String value) {
        return MAP.get(value);
    }
}
