package info.jab.aoc2024.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

// https://dreampuf.github.io/GraphvizOnline/
// Idea to generate GraphViz graph to see the bad elements manually for Part 2
class GraphVizUtil {

    private record InputData(String[] initialValues, String[] gates) {}

    private static InputData getInputData(String fileName) {
        var list = ResourceLines.list(fileName);

        int separatorIndex = list.indexOf(""); // Find the blank line
        String[] initialValues = list.subList(0, separatorIndex).toArray(new String[0]); // Lines before blank
        String[] gates = list.subList(separatorIndex + 1, list.size()).toArray(new String[0]); // Lines after blank
        return new InputData(initialValues, gates);
    }

    private record Instruction(String a, String op, String b, String c) { }

    private static List<Instruction> parseInstructions(List<String> input) {
        String pattern = "([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)";
        Pattern regex = Pattern.compile(pattern);

        // Parse each string into an Instruction
        return input.stream()
                .map(line -> {
                    Matcher matcher = regex.matcher(line);
                    if (matcher.matches()) {
                        String a = matcher.group(1);
                        String op = matcher.group(2);
                        String b = matcher.group(3);
                        String c = matcher.group(4);
                        return new Instruction(a, op, b, c);
                    } else {
                        throw new IllegalArgumentException("Invalid instruction format: " + line);
                    }
                })
                .toList();
    }

    public static void generate(String fileName) {
        InputData inputData = getInputData(fileName);
        List<Instruction> instructions = parseInstructions(Arrays.asList(inputData.gates));

        var output = new StringBuilder();
        output.append("digraph {\n");
        for (var instruction : instructions) {
            output.append("  {").append(instruction.a).append(",").append(instruction.b).append("} -> ").append(instruction.c)
                .append(" [color=").append(switch (instruction.op) {
                    case "AND" -> "red";
                    case "OR" -> "green";
                    case "XOR" -> "blue";
                    default -> throw new IllegalStateException("Unexpected value: " + instruction.op);
                }).append("];\n");
        }
        output.append("}\n");
        try {
            Files.writeString(Path.of("day24.txt"), output.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
