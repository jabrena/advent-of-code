package info.jab.aoc.day13;

import java.util.ArrayList;
import java.util.List;
import com.putoet.resources.ResourceLines;

public class ClawContraption {

    //TODO Candidate to be promoted to Utilities
    private List<List<String>> groupLines(List<String> lines, int groupSize) {
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
        return grouped;
    }

    private List<Claw> getInput(String fileName) {
        var list = ResourceLines.list(fileName);
        var groupBy = 3;
        return groupLines(list, groupBy).stream()
            .map(group -> new Claw(String.join("\n", group)))
            .toList();
    }

    public Long solve(String fileName, boolean flag) {
        List<Claw> claws = getInput(fileName);
        if (flag) {
            claws.forEach(Claw::update);
        }
        return claws.stream()
            .map(Claw::solve)
            .mapToLong(Long::valueOf)
            .sum();
    }
}
