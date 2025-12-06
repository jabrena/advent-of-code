package info.jab.aoc2015.day14;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReindeerRaceSolver implements Solver<Integer> {

    private static final int TOTAL_SECONDS = 2503;

    record Reindeer(String name, int speed, int flyTime, int restTime) {
        
        public int distanceAfter(int totalSeconds) {
            int cycleTime = flyTime + restTime;
            int completeCycles = totalSeconds / cycleTime;
            int remainingSeconds = totalSeconds % cycleTime;
            
            int distance = completeCycles * speed * flyTime;
            
            // Handle remaining seconds in the current cycle
            if (remainingSeconds > 0) {
                int flyingSecondsInLastCycle = Math.min(remainingSeconds, flyTime);
                distance += flyingSecondsInLastCycle * speed;
            }
            
            return distance;
        }
    }

    @Override
    public Integer solvePartOne(final String fileName) {
        List<Reindeer> reindeer = parseReindeer(fileName);
        
        int maxDistance = 0;
        for (Reindeer r : reindeer) {
            int distance = r.distanceAfter(TOTAL_SECONDS);
            maxDistance = Math.max(maxDistance, distance);
        }
        
        return maxDistance;
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        List<Reindeer> reindeer = parseReindeer(fileName);
        int[] points = new int[reindeer.size()];
        
        // Optimize: Instead of simulating each second, calculate distances at each second
        // but use the efficient distanceAfter method which is O(1) per reindeer per second
        // This reduces from O(R×T²) to O(R×T) by avoiding redundant calculations
        
        // Pre-calculate distances for all reindeer at each second
        // We can optimize further by only checking at state transitions, but for clarity
        // and correctness, we'll use the optimized distance calculation
        for (int second = 1; second <= TOTAL_SECONDS; second++) {
            int maxDistance = 0;
            
            // Calculate distance for each reindeer at this second - O(1) per reindeer
            for (int i = 0; i < reindeer.size(); i++) {
                int distance = reindeer.get(i).distanceAfter(second);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
            
            // Award points to reindeer in the lead - O(R)
            for (int i = 0; i < reindeer.size(); i++) {
                if (reindeer.get(i).distanceAfter(second) == maxDistance) {
                    points[i]++;
                }
            }
        }
        
        // Find the maximum points - O(R)
        int maxPoints = 0;
        for (int point : points) {
            maxPoints = Math.max(maxPoints, point);
        }
        
        return maxPoints;
    }
    
    private List<Reindeer> parseReindeer(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Reindeer> reindeer = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");
        
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line.trim());
            if (matcher.matches()) {
                String name = matcher.group(1);
                int speed = Integer.parseInt(matcher.group(2));
                int flyTime = Integer.parseInt(matcher.group(3));
                int restTime = Integer.parseInt(matcher.group(4));
                
                reindeer.add(new Reindeer(name, speed, flyTime, restTime));
            }
        }
        
        return reindeer;
    }
}
