package info.jab.aoc2015.day14;

/**
 * Represents a reindeer with its racing properties.
 * Immutable record following functional programming principles.
 */
public record Reindeer(String name, int speed, int flyTime, int restTime) {
    
    /**
     * Pure function: calculates distance after given seconds.
     * No side effects, deterministic output.
     */
    public int distanceAfter(final int totalSeconds) {
        final int cycleTime = flyTime + restTime;
        final int completeCycles = totalSeconds / cycleTime;
        final int remainingSeconds = totalSeconds % cycleTime;
        
        final int baseDistance = completeCycles * speed * flyTime;
        
        // Handle remaining seconds in the current cycle
        final int flyingSecondsInLastCycle = remainingSeconds > 0 
                ? Math.min(remainingSeconds, flyTime) 
                : 0;
        
        return baseDistance + flyingSecondsInLastCycle * speed;
    }
}
