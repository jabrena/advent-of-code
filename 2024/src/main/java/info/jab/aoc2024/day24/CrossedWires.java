package info.jab.aoc2024.day24;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

public class CrossedWires {

    public String solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);

        String[] initialValues;
        String[] gates;

        int separatorIndex = list.indexOf(""); // Find the blank line
        initialValues = list.subList(0, separatorIndex).toArray(new String[0]); // Lines before blank
        gates = list.subList(separatorIndex + 1, list.size()).toArray(new String[0]); // Lines after blank

        // Simulate the system and calculate the result
        return "" + simulateSystem(initialValues, gates);
    }

    private long simulateSystem(String[] initialValues, String[] gates) {
        // Wire values
        Map<String, Integer> wireValues = new HashMap<>();

        // Parse initial values
        for (String line : initialValues) {
            String[] parts = line.split(": ");
            wireValues.put(parts[0], Integer.parseInt(parts[1]));
        }

        // Parse and process gates until all are resolved
        List<String> remainingGates = new ArrayList<>(Arrays.asList(gates));
        while (!remainingGates.isEmpty()) {
            Iterator<String> iterator = remainingGates.iterator();
            while (iterator.hasNext()) {
                String gate = iterator.next();
                if (processGate(gate, wireValues)) {
                    iterator.remove();
                }
            }
        }

        // Collect output wires starting with "z"
        List<Integer> zValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wireValues.entrySet()) {
            if (entry.getKey().startsWith("z")) {
                int index = Integer.parseInt(entry.getKey().substring(1));
                while (zValues.size() <= index) {
                    zValues.add(0);
                }
                zValues.set(index, entry.getValue());
            }
        }

        String binaryResult = zValues.stream().map(String::valueOf).collect(Collectors.joining());
        return binaryToDecimal(binaryResult);
    }

    private long binaryToDecimal(String binaryString) {
        String reversedBinary = new StringBuilder(binaryString).reverse().toString();
        // Convert the reversed binary string to a long value
        return Long.parseLong(reversedBinary, 2);
    }

    private boolean processGate(String gate, Map<String, Integer> wireValues) {
        String pattern = "([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)";
        Matcher matcher = Pattern.compile(pattern).matcher(gate);

        if (matcher.matches()) {
            String input1 = matcher.group(1);
            String operation = matcher.group(2);
            String input2 = matcher.group(3);
            String output = matcher.group(4);

            if (wireValues.containsKey(input1) && wireValues.containsKey(input2)) {
                int value1 = wireValues.get(input1);
                int value2 = wireValues.get(input2);
                int result = 0;

                switch (operation) {
                    case "AND":
                        result = value1 & value2;
                        break;
                    case "OR":
                        result = value1 | value2;
                        break;
                    case "XOR":
                        result = value1 ^ value2;
                        break;
                    default:
                        // Unknown operation, cannot process gate
                        return false;
                }

                wireValues.put(output, result);
                return true;
            }
        }
        return false;
    }
}

