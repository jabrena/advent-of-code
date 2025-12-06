package info.jab.aoc2015.day13;

import java.util.stream.Stream;

/**
 * Represents the type of happiness change in the seating arrangement problem.
 * Immutable enum following functional programming principles.
 */
public enum HappinessType {
    GAIN("gain"),
    LOSE("lose");

    private final String value;

    HappinessType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Converts a string value to its corresponding HappinessType.
     * 
     * @param text the text value ("gain" or "lose")
     * @return the HappinessType enum value
     * @throws IllegalArgumentException if the text doesn't match any HappinessType
     */
    public static HappinessType from(final String text) {
        return Stream.of(HappinessType.values())
                .filter(type -> type.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HappinessType with text " + text + " found"));
    }
}
