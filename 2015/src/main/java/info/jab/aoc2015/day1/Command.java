package info.jab.aoc2015.day1;

import java.util.Arrays;

/**
 * Represents a command in the Lisp navigation system.
 * Immutable enum following functional programming principles.
 */
public enum Command {
    UP('(', 1),
    DOWN(')', -1);
    
    private final char symbol;
    private final int value;
    
    Command(final char symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }
    
    public char symbol() {
        return symbol;
    }
    
    public int value() {
        return value;
    }
    
    /**
     * Converts a character symbol to its corresponding Command.
     * 
     * @param symbol the character symbol ('(' or ')')
     * @return the Command enum value
     * @throws IllegalArgumentException if the symbol is invalid
     */
    public static Command fromChar(final char symbol) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.symbol == symbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid symbol: " + symbol));
    }
}
