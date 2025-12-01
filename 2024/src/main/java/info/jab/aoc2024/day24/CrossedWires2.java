package info.jab.aoc2024.day24;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

class CrossedWires2 {

    public String solvePartTwo(String fileName) {
        List<String> input = ResourceLines.list(fileName);

        // Find the index where the second part begins
        int splitIndex = input.indexOf("");

        // Skip the first part and filter out any empty lines
        List<String> secondPart = input.stream().skip(splitIndex + 1).filter(line -> !line.isEmpty()).toList();

        // Process the second part and collect all wrong elements
        Set<String> wrong = secondPart.stream()
                .flatMap(line -> process(input, line).stream())
                .collect(Collectors.toSet());

        return String.join(",", wrong.stream().sorted().toList());
    }

    private Set<String> process(List<String> input, String line) {
        Set<String> wrong = new HashSet<>();
        String[] parts = line.split(" ");

        // Checking for correctness.
        if (parts[4].startsWith("z") && !parts[1].equals("XOR") && !parts[4].equals("z45")) {
            wrong.add(parts[4]);
        }
        if (parts[1].equals("XOR") &&
                !List.of("x", "y", "z").contains(parts[4].split("")[0]) &&
                !List.of("x", "y", "z").contains(parts[0].split("")[0]) &&
                !List.of("x", "y", "z").contains(parts[2].split("")[0])) {
            wrong.add(parts[4]);
        }
        if (parts[1].equals("AND") && (!parts[0].equals("x00")) && (!parts[2].equals("x00"))) {
            input.stream()
                 .map(subline -> subline.split(" "))
                 .filter(subparts -> subparts.length > 2 &&
                         (parts[4].equals(subparts[0]) || parts[4].equals(subparts[2])) &&
                         !subparts[1].equals("OR"))
                 .forEach(subparts -> wrong.add(parts[4]));
        }
        if (parts[1].equals("XOR")) {
            input.stream()
                 .map(subline -> subline.split(" "))
                 .filter(subparts -> subparts.length > 2 &&
                         (parts[4].equals(subparts[0]) || parts[4].equals(subparts[2])) &&
                         subparts[1].equals("OR"))
                 .forEach(subparts -> wrong.add(parts[4]));
        }
        return wrong;
    }
}
