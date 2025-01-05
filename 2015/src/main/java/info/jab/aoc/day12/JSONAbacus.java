package info.jab.aoc.day12;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class JSONAbacus implements Solver<Integer> {

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(JsonParser.Feature.ALLOW_COMMENTS, true);

    private int calculateSum(JsonNode node) {
        int sum = 0;

        // If it's a number, add it
        if (node.isNumber()) {
            return node.asInt();
        }

        // If it's an array, process each element
        if (node.isArray()) {
            Iterator<JsonNode> elements = node.elements();
            while (elements.hasNext()) {
                sum += calculateSum(elements.next());
            }
            return sum;
        }

        // If it's an object, process each field
        if (node.isObject()) {
            Iterator<JsonNode> fields = node.elements();
            while (fields.hasNext()) {
                sum += calculateSum(fields.next());
            }
            return sum;
        }

        // If it's a string or other type, return 0
        return 0;
    }

    private int calculateSumIgnoringRed(JsonNode node) {
        int sum = 0;

        // If it's a number, add it
        if (node.isNumber()) {
            return node.asInt();
        }

        // If it's an array, process each element
        if (node.isArray()) {
            Iterator<JsonNode> elements = node.elements();
            while (elements.hasNext()) {
                sum += calculateSumIgnoringRed(elements.next());
            }
            return sum;
        }

        // If it's an object, check if it contains "red"
        if (node.isObject()) {
            // First check if any value is "red"
            Iterator<JsonNode> values = node.elements();
            while (values.hasNext()) {
                JsonNode value = values.next();
                if (value.isTextual() && value.asText().equals("red")) {
                    return 0; // Ignore the entire object
                }
            }

            // If it doesn't contain "red", process normally
            Iterator<JsonNode> fields = node.elements();
            while (fields.hasNext()) {
                sum += calculateSumIgnoringRed(fields.next());
            }
        }

        return sum;
    }

    @Override
    public Integer solvePartOne(String fileName) {
        try {
            String content = ResourceLines.line(fileName);
            JsonNode rootNode = objectMapper.readTree(content);
            return calculateSum(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        try {
            String content = ResourceLines.line(fileName);
            JsonNode rootNode = objectMapper.readTree(content);
            return calculateSumIgnoringRed(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
