package info.jab.aoc2015.day15;

/**
 * Represents the score of a cookie recipe.
 * Immutable record following functional programming principles.
 */
public record RecipeScore(long capacity, long durability, long flavor, long texture) {
    
    /**
     * Calculates the total score of the recipe.
     * Negative totals become 0.
     */
    public long totalScore() {
        return Math.max(0, capacity) * Math.max(0, durability) * Math.max(0, flavor) * Math.max(0, texture);
    }
}
