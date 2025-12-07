package info.jab.aoc2025.day7;

import java.util.Map;

public enum CellType {
    EMPTY('.'),
    SPLITTER('^'),
    START('S');

    private static final Map<Character, CellType> BY_CHARACTER = Map.ofEntries(
            Map.entry(EMPTY.character, EMPTY),
            Map.entry(SPLITTER.character, SPLITTER),
            Map.entry(START.character, START));

    private final char character;

    CellType(final char character) {
        this.character = character;
    }

    public char character() {
        return character;
    }

    public static CellType from(final char c) {
        return BY_CHARACTER.getOrDefault(c, EMPTY);
    }
}
