package info.jab.aoc2015.day14;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    
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
    
    public static void main(String[] args) {
        String input = """
            Rudolph can fly 22 km/s for 8 seconds, but then must rest for 165 seconds.
            Cupid can fly 8 km/s for 17 seconds, but then must rest for 114 seconds.
            Prancer can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
            Donner can fly 25 km/s for 6 seconds, but then must rest for 145 seconds.
            Dasher can fly 11 km/s for 12 seconds, but then must rest for 125 seconds.
            Comet can fly 21 km/s for 6 seconds, but then must rest for 121 seconds.
            Blitzen can fly 18 km/s for 3 seconds, but then must rest for 50 seconds.
            Vixen can fly 20 km/s for 4 seconds, but then must rest for 75 seconds.
            Dancer can fly 7 km/s for 20 seconds, but then must rest for 119 seconds.
            """;
            
        Day14 solution = new Day14();
        int result = solution.solve(input, 2503);
        System.out.println("Result: " + result);
    }
    
    public int solve(String input, int totalSeconds) {
        List<Reindeer> reindeer = parseReindeer(input);
        
        int maxDistance = 0;
        for (Reindeer r : reindeer) {
            int distance = r.distanceAfter(totalSeconds);
            maxDistance = Math.max(maxDistance, distance);
        }
        
        return maxDistance;
    }
    
    public int solvePart2(String input, int totalSeconds) {
        List<Reindeer> reindeer = parseReindeer(input);
        int[] points = new int[reindeer.size()];
        
        // Simulate each second
        for (int second = 1; second <= totalSeconds; second++) {
            int maxDistance = 0;
            int[] distances = new int[reindeer.size()];
            
            // Calculate distance for each reindeer at this second
            for (int i = 0; i < reindeer.size(); i++) {
                distances[i] = reindeer.get(i).distanceAfter(second);
                maxDistance = Math.max(maxDistance, distances[i]);
            }
            
            // Award points to reindeer in the lead
            for (int i = 0; i < reindeer.size(); i++) {
                if (distances[i] == maxDistance) {
                    points[i]++;
                }
            }
        }
        
        // Find the maximum points
        int maxPoints = 0;
        for (int point : points) {
            maxPoints = Math.max(maxPoints, point);
        }
        
        return maxPoints;
    }
    
    private List<Reindeer> parseReindeer(String input) {
        List<Reindeer> reindeer = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");
        
        for (String line : input.trim().split("\n")) {
            Matcher matcher = pattern.matcher(line);
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