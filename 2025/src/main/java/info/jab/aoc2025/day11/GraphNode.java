package info.jab.aoc2025.day11;

/**
 * Represents a graph node with exactly 3 characters.
 * Immutable record following functional programming principles.
 * Makes invalid states unrepresentable through validation.
 */
public record GraphNode(String value) {
    private static final int NODE_NAME_LENGTH = 3;

    /**
     * Compact constructor with validation for type safety.
     * Ensures node name is exactly 3 characters, making invalid states unrepresentable.
     */
    public GraphNode {
        if (value == null || value.length() != NODE_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Node name must be exactly %d characters, got: '%s' (length: %d)"
                            .formatted(NODE_NAME_LENGTH, value, value != null ? value.length() : 0));
        }
    }

    /**
     * Static factory method that creates a GraphNode from a string representation.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param value The string value to create a GraphNode from
     * @return A GraphNode record
     * @throws IllegalArgumentException if the value is null, empty, or not exactly 3 characters
     */
    public static GraphNode from(final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Node value cannot be null or empty");
        }
        return new GraphNode(value.trim());
    }
}
