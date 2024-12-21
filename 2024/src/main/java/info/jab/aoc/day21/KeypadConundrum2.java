package info.jab.aoc.day21;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.putoet.grid.Point;

/**
 * Inspired by:
 * https://github.com/thomas-raneland/aoc2024/blob/main/src/Day21.java
 */
class KeypadConundrum2 {

    private static final char keyPadA = 'A';

    private record CacheKey(String directions, int dirKeypads) {}
    private final Map<CacheKey, Long> minCodeLengthCache = new ConcurrentHashMap<>();

    /**
     * Calculates the minimum sequence length required to press buttons on the numeric keypad.
     * The solution applies Recursive Backtracking to find the shortest path between two points.
     * https://en.wikipedia.org/wiki/Backtracking
     * https://en.wikipedia.org/wiki/Recursive_backtracking
     *
     * TODO: Reduce complexity removing recursion approach
     *
     * @param directions The sequence of directions for the robot to follow.
     * @param dirKeypads The number of intermediate directional keypads involved.
     * @return The minimum length of the sequence needed to achieve the input directions.
     */
    private long getMinCodeLength1(String directions, int dirKeypads) {
        // Base case: No intermediate keypads left, return the length of the directions.
        if (dirKeypads == 0) {
            return directions.length();
        } else if (directions.indexOf(keyPadA) < directions.length() - 1) {
            // Split directions by 'A' and sum the results recursively for all parts.
            long sum = 0;
            int start = 0;

            for (int i = 0; i < directions.length(); i++) {
                if (directions.charAt(i) == keyPadA) {
                    int end = i + 1;
                    sum += getMinCodeLength1(directions.substring(start, end), dirKeypads);
                    start = end;
                }
            }
            return sum;
        } else {
            // Use caching to avoid recomputation for a given key.
            CacheKey key = new CacheKey(directions, dirKeypads);

            if (minCodeLengthCache.containsKey(key)) {
                return minCodeLengthCache.get(key);
            }

            // Generate all possible direction codes and calculate the minimum length recursively.
            long minCodeLength = Long.MAX_VALUE;
            for (String newDirections : getDirectionCodes(directions, DIR_KEYPAD)) {
                long codeLength = getMinCodeLength1(newDirections, dirKeypads - 1);

                if (codeLength < minCodeLength) {
                    minCodeLength = codeLength;
                }
            }

            minCodeLengthCache.put(key, minCodeLength);
            return minCodeLength;
        }
    }

    /**
     * Generates all possible sequences of directional codes to navigate a keypad.
     *
     * @param code The target sequence to be typed on the numeric keypad.
     * @param keypad The mapping of characters to their respective keypad positions.
     * @return A list of directional codes needed to type the given sequence.
     */
    private List<String> getDirectionCodes(String code, Map<Character, Point> keypad) {
        List<String> directionCodes = new ArrayList<>();
        directionCodes.add("");
        char start = keyPadA;

        for (char end : code.toCharArray()) {

            // Calculate shortest paths for each segment of the code.
            List<String> newDirectionCodes = new ArrayList<>();
            var shortestPaths = shortestPaths(keypad.get(start), keypad.get(end), keypad.values());
            for (String path : shortestPaths) {
                for (String directionCode : directionCodes) {
                    newDirectionCodes.add(directionCode + path + keyPadA);
                }
            }

            directionCodes = newDirectionCodes;
            start = end;
        }
        return directionCodes;
    }

    /**
     * Calculates the shortest paths from a starting point to an endpoint on a keypad grid.
     * The method uses a breadth-first search (BFS) to explore all possible paths.
     * https://en.wikipedia.org/wiki/Breadth-first_search
     *
     * @param start The starting position on the keypad.
     * @param end The target position on the keypad.
     * @param validPositions All valid positions on the keypad grid.
     * @return A list of the shortest directional sequences to navigate between the points.
     */
    private static List<String> shortestPaths(Point start, Point end, Collection<Point> validPositions) {
        record State(Point pos, String path) {}
        List<String> paths = new ArrayList<>();
        List<State> queue = new ArrayList<>();
        queue.add(new State(start, ""));

        while (!queue.isEmpty()) {
            State state = queue.removeFirst();

            // Stop if a shorter path has already been found.
            if (!paths.isEmpty() && paths.getFirst().length() < state.path().length()) {
                break;
            }

            // Check if the endpoint has been reached.
            if (state.pos().equals(end)) {
                paths.add(state.path());
                continue;
            }

            // Explore neighboring positions.
            for (Direction direction : Direction.values()) {
                Point n = direction.move(state.pos());
                if (validPositions.contains(n)) {
                    queue.add(new State(n, state.path() + direction.toChar()));
                }
            }
        }
        return paths;
    }

    private static final Map<Character, Point> DIR_KEYPAD = Map.ofEntries(
            Map.entry('^', new Point(1, 0)),
            Map.entry('A', new Point(2, 0)),
            Map.entry('<', new Point(0, 1)),
            Map.entry('v', new Point(1, 1)),
            Map.entry('>', new Point(2, 1))
    );

    private static final Map<Character, Point> NUM_KEYPAD = Map.ofEntries(
            Map.entry('7', new Point(0, 0)),
            Map.entry('8', new Point(1, 0)),
            Map.entry('9', new Point(2, 0)),

            Map.entry('4', new Point(0, 1)),
            Map.entry('5', new Point(1, 1)),
            Map.entry('6', new Point(2, 1)),

            Map.entry('1', new Point(0, 2)),
            Map.entry('2', new Point(1, 2)),
            Map.entry('3', new Point(2, 2)),

            Map.entry('0', new Point(1, 3)),
            Map.entry('A', new Point(2, 3))
    );

    /**
     * Extracts the numeric portion of a given code (ignoring leading zeroes).
     *
     * @param code The code string to parse.
     * @return The numeric part as a Long.
     */
    private Long getNumericPartOfCode(String code) {
        return Long.parseLong(code.substring(0, 3));
    }

    /**
     * Calculates the minimum code length for the provided code sequence and parameters.
     *
     * @param code The code to be typed on the numeric keypad.
     * @param param The number of intermediate directional keypads to navigate.
     * @return The minimum sequence length to type the given code.
     */
    private Long calculateMinCodeLength(String code, Integer param) {
        List<String> directions = getDirectionCodes(code, NUM_KEYPAD);
        return directions.stream().mapToLong(d -> getMinCodeLength1(d, param)).min().orElseThrow();
    }

    /**
     * Solves part one of the puzzle: computes the sum of complexities for all input codes
     * with two directional keypads.
     *
     * @param input The list of codes to type on the numeric keypad.
     * @return The sum of complexities for all codes.
     */
    long partOne(List<String> lines) {
        return lines.stream().parallel()
            .mapToLong(code -> getNumericPartOfCode(code) * calculateMinCodeLength(code, 2))
            .sum();
    }

    /**
     * Solves part two of the puzzle: computes the sum of complexities for all input codes
     * with 25 directional keypads.
     *
     * @param input The list of codes to type on the numeric keypad.
     * @return The sum of complexities for all codes.
     */
    long partTwo(List<String> lines) {
        return lines.stream().parallel()
            .mapToLong(code -> getNumericPartOfCode(code) * calculateMinCodeLength(code, 25))
            .sum();
    }
}
