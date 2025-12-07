package info.jab.aoc2017.day13;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Map<Integer, Integer> layers = parseLayers(fileName);
        int maxDepth = layers.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        int severity = 0;
        
        for (int depth = 0; depth <= maxDepth; depth++) {
            if (layers.containsKey(depth)) {
                int range = layers.get(depth);
                // Scanner position at time t: t % (2 * (range - 1))
                // If position == 0, scanner is at the top
                int time = depth;
                int cycleLength = 2 * (range - 1);
                if (cycleLength > 0 && time % cycleLength == 0) {
                    severity += depth * range;
                }
            }
        }
        
        return severity;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Map<Integer, Integer> layers = parseLayers(fileName);
        int maxDepth = layers.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        
        for (int delay = 0; ; delay++) {
            boolean caught = false;
            for (int depth = 0; depth <= maxDepth; depth++) {
                if (layers.containsKey(depth)) {
                    int range = layers.get(depth);
                    int time = delay + depth;
                    int cycleLength = 2 * (range - 1);
                    if (cycleLength > 0 && time % cycleLength == 0) {
                        caught = true;
                        break;
                    }
                }
            }
            if (!caught) {
                return delay;
            }
        }
    }

    private Map<Integer, Integer> parseLayers(String fileName) {
        Map<Integer, Integer> layers = new HashMap<>();
        List<String> lines = ResourceLines.list(fileName);
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(": ");
            int depth = Integer.parseInt(parts[0].trim());
            int range = Integer.parseInt(parts[1].trim());
            layers.put(depth, range);
        }
        
        return layers;
    }
}
