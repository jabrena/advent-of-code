package info.jab.aoc2015.day6;

import java.util.Arrays;

enum CommandType {
    TURN_ON("turn on"),
    TURN_OFF("turn off"),
    TOGGLE("toggle");

    private final String text;

    CommandType(String text) {
        this.text = text;
    }

    public static CommandType fromString(String text) {
        return Arrays.stream(CommandType.values())
                .filter(cmd -> cmd.text.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + text));
    }
}

