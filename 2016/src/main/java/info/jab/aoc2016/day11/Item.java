package info.jab.aoc2016.day11;

/**
 * Represents an item (generator or microchip) in the RTG facility.
 */
public record Item(String element, ItemType type) {
    @Override
    public String toString() {
        return element + "_" + type;
    }
}

