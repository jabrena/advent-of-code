package info.jab.aoc.day9;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class ExplosivesInCyberspace implements Solver<Long> {

    private static final Pattern MARKER_PATTERN = Pattern.compile("\\((\\d+)x(\\d+)\\)");

    @Override
    public Long solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName).replaceAll("\\s", ""); // Remove all whitespace
        return calculateDecompressedLength(input, false);
    }

    @Override
    public Long solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName).replaceAll("\\s", ""); // Remove all whitespace
        return calculateDecompressedLength(input, true);
    }

    public long calculateDecompressedLength(String input, boolean recursive) {
        long totalLength = 0;
        int i = 0;

        while (i < input.length()) {
            if (input.charAt(i) == '(') {
                // Find the end of the marker
                int markerEnd = input.indexOf(')', i);
                if (markerEnd == -1) {
                    // No closing parenthesis found, treat as regular characters
                    totalLength += input.length() - i;
                    break;
                }

                String marker = input.substring(i, markerEnd + 1);
                Matcher matcher = MARKER_PATTERN.matcher(marker);

                if (matcher.matches()) {
                    int length = Integer.parseInt(matcher.group(1));
                    int repeat = Integer.parseInt(matcher.group(2));

                    int dataStart = markerEnd + 1;
                    int dataEnd = dataStart + length;

                    if (dataEnd <= input.length()) {
                        String data = input.substring(dataStart, dataEnd);

                        if (recursive) {
                            // For part 2, recursively calculate the length of the data
                            long dataLength = calculateDecompressedLength(data, true);
                            totalLength += dataLength * repeat;
                        } else {
                            // For part 1, just multiply the raw data length
                            totalLength += (long) data.length() * repeat;
                        }

                        i = dataEnd;
                    } else {
                        // Not enough data after the marker, treat as regular character
                        totalLength++;
                        i++;
                    }
                } else {
                    // Invalid marker format, treat as regular character
                    totalLength++;
                    i++;
                }
            } else {
                // Regular character
                totalLength++;
                i++;
            }
        }

        return totalLength;
    }
}
