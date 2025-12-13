package info.jab.aoc2025.day12;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a region with dimensions and shape requirements.
 * A region defines a rectangular area and specifies how many of each shape type it needs.
 * Immutable record following functional programming principles.
 *
 * @param width The width of the region
 * @param height The height of the region
 * @param requirements Map of shape IDs to their required counts
 * @param regionArea The total area of the region (width * height)
 */
public record Region(int width, int height, Map<Integer, Integer> requirements, long regionArea) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     * Cast to long to prevent integer overflow during multiplication.
     * Without the cast, width * height would be computed as int (which can overflow),
     * then promoted to long. The cast ensures long arithmetic from the start.
     * regionArea must be long because it's compared with long values in ShapePacking.
     */
    public Region {
        // Defensive copy to ensure immutability
        requirements = Map.copyOf(requirements);
    }

    /**
     * Static factory method that creates a Region from a string representation.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param line The line containing region in format "WxH: count1 count2 ..."
     * @return A Region record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Region from(String line) {
        String[] parts = line.split(": ");
        String[] dims = parts[0].split("x");
        int width = Integer.parseInt(dims[0]);
        int height = Integer.parseInt(dims[1]);
        // Cast to long to prevent integer overflow during multiplication.
        // Without the cast, width * height would be computed as int (which can overflow),
        // then promoted to long. The cast ensures long arithmetic from the start.
        long regionArea = (long) width * height;

        Map<Integer, Integer> requirements = new HashMap<>();
        String[] counts = parts[1].trim().split("\\s+");
        for (int i = 0; i < counts.length; i++) {
            requirements.put(i, Integer.parseInt(counts[i]));
        }

        return new Region(width, height, requirements, regionArea);
    }
}

