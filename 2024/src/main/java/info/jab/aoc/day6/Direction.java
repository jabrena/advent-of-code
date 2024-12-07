package info.jab.aoc.day6;

enum Direction {
    NORTH("^"),
    RIGHT(">"),
    SOUTH("v"),
    LEFT("<");

    private final String symbol; // Add a private field to hold the symbol

    // Constructor to initialize the symbol
    Direction(String symbol) {
        this.symbol = symbol;
    }

    // Getter method to access the symbol
    public String getSymbol() {
        return symbol;
    }
}
