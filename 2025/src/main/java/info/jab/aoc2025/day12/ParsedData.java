package info.jab.aoc2025.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Record to hold parsed input data.
 * Immutable data structure following functional programming principles.
 *
 * @param shapes Map of shape IDs to their corresponding Shape objects
 * @param regions List of regions to pack shapes into
 */
public record ParsedData(Map<Integer, Shape> shapes, List<Region> regions) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     */
    public ParsedData {
        shapes = Map.copyOf(shapes);
        regions = List.copyOf(regions);
    }

    /**
     * Static factory method that creates a ParsedData from input lines.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     * Uses Stream API with efficient accumulation to minimize object creation.
     *
     * @param lines The input lines to parse
     * @return A ParsedData record containing shapes and regions
     */
    public static ParsedData from(List<String> lines) {
        List<String> nonEmptyLines = lines.stream()
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList()); // Mutable list for accumulation

        Map<Integer, Shape> shapes = new HashMap<>();
        List<Region> regions = new ArrayList<>();
        List<String> currentShapeBuffer = new ArrayList<>();
        Integer currentShapeId = null;

        for (String line : nonEmptyLines) {
            if (Character.isDigit(line.charAt(0)) && line.contains(":")) {
                if (line.contains("x")) {
                    // Region line: save current shape if exists, then add region
                    if (currentShapeId != null && !currentShapeBuffer.isEmpty()) {
                        shapes.put(currentShapeId, Shape.from(currentShapeId, currentShapeBuffer));
                        currentShapeBuffer.clear();
                    }
                    regions.add(Region.from(line));
                    currentShapeId = null;
                } else {
                    // Shape ID line: save previous shape if exists, start new shape
                    if (currentShapeId != null && !currentShapeBuffer.isEmpty()) {
                        shapes.put(currentShapeId, Shape.from(currentShapeId, currentShapeBuffer));
                        currentShapeBuffer.clear();
                    }
                    currentShapeId = Integer.parseInt(line.replace(":", ""));
                }
            } else {
                // Shape content line: add to current buffer
                currentShapeBuffer.add(line);
            }
        }

        // Handle final shape if buffer is not empty
        if (currentShapeId != null && !currentShapeBuffer.isEmpty()) {
            shapes.put(currentShapeId, Shape.from(currentShapeId, currentShapeBuffer));
        }

        return new ParsedData(shapes, regions);
    }
}

