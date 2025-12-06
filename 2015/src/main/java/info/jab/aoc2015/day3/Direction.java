package info.jab.aoc2015.day3;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

enum Direction {
    UP('^', 0, 1),
    DOWN('v', 0, -1),
    RIGHT('>', 1, 0),
    LEFT('<', -1, 0);

    @SuppressWarnings("null")
    private static final Map<Character, Direction> SYMBOL_MAP =
        Arrays.stream(values()).collect(Collectors.toMap(dir -> dir.symbol, dir -> dir));


    private final char symbol;
    private final int dx;
    private final int dy;

    Direction(char symbol, int dx, int dy) {
        this.symbol = symbol;
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction fromSymbol(char symbol) {
        return Optional.ofNullable(SYMBOL_MAP.get(symbol))
                .orElseThrow(() -> new IllegalArgumentException("Invalid direction symbol: " + symbol));
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}

