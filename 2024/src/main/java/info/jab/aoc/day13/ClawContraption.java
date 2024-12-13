package info.jab.aoc.day13;

import java.util.ArrayList;
import java.util.List;
import com.putoet.resources.ResourceLines;

public class ClawContraption {

    public Long solve(String fileName, boolean flag) {
        List<Claw> claws = getInput2(fileName);
        if (flag) {
            claws.forEach(Claw::update);
        }
        return claws.stream()
            .map(Claw::solve)
            .mapToLong(Long::valueOf)
            .sum();
    }

    public static List<List<String>> groupLines(List<String> lines, int groupSize) {
        List<List<String>> grouped = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue; // Skip empty lines
            currentGroup.add(line);
            if (currentGroup.size() == groupSize) {
                grouped.add(new ArrayList<>(currentGroup));
                currentGroup.clear();
            }
        }
        if (!currentGroup.isEmpty()) {
            if (currentGroup.size() < groupSize) {
                throw new IllegalStateException("Incomplete group detected: " + currentGroup);
            }
            grouped.add(currentGroup);
        }
        return grouped;
    }

    public List<Claw> getInput2(String fileName) {
        return groupLines(ResourceLines.list(fileName), 3).stream()
            .map(group -> new Claw(String.join("\n", group)))
            .toList();
    }
}
