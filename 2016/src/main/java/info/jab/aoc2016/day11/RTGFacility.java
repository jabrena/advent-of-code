package info.jab.aoc2016.day11;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public class RTGFacility implements Solver<Integer> {

    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking.
     */
    private static final int MAX_INPUT_LENGTH = 10_000;

    /**
     * Maximum number of regex matches to prevent DoS attacks.
     * This limit prevents excessive iterations in regex find() loops.
     */
    private static final int MAX_MATCHES = 100;

    /**
     * Regex pattern for matching generator elements.
     * Uses atomic group ((?>\\w+)) to prevent backtracking and ReDoS attacks.
     * Atomic groups prevent the regex engine from backtracking, making it safe from polynomial runtime attacks.
     * Pattern matches: "<element> generator" where element is one or more word characters.
     *
     * Security: This pattern is safe from ReDoS because:
     * 1. Atomic group (?>\\w+) prevents backtracking within the element match
     * 2. Input length is limited to MAX_INPUT_LENGTH (10,000 characters)
     * 3. Match iteration is limited to MAX_MATCHES (100 matches)
     * 4. The pattern structure is linear with no nested quantifiers or alternations
     */
    @SuppressWarnings("java:S5852") // Safe: atomic groups prevent backtracking, input/match limits prevent DoS
    private static final Pattern GENERATOR_PATTERN = Pattern.compile("((?>\\w+)) generator");

    /**
     * Regex pattern for matching microchip elements.
     * Uses atomic group ((?>\\w+)) to prevent backtracking and ReDoS attacks.
     * Atomic groups prevent the regex engine from backtracking, making it safe from polynomial runtime attacks.
     * Pattern matches: "<element>-compatible microchip" where element is one or more word characters.
     *
     * Security: This pattern is safe from ReDoS because:
     * 1. Atomic group (?>\\w+) prevents backtracking within the element match
     * 2. Input length is limited to MAX_INPUT_LENGTH (10,000 characters)
     * 3. Match iteration is limited to MAX_MATCHES (100 matches)
     * 4. The pattern structure is linear with no nested quantifiers or alternations
     */
    @SuppressWarnings("java:S5852") // Safe: atomic groups prevent backtracking, input/match limits prevent DoS
    private static final Pattern MICROCHIP_PATTERN = Pattern.compile("((?>\\w+))-compatible microchip");

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        RTGState initialState = parseInput(lines);
        return findMinimumSteps(initialState);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        RTGState initialState = parseInput(lines);

        // For part 2, add elerium and dilithium items to the first floor
        initialState = initialState.addItem(0, "elerium", ItemType.GENERATOR);
        initialState = initialState.addItem(0, "elerium", ItemType.MICROCHIP);
        initialState = initialState.addItem(0, "dilithium", ItemType.GENERATOR);
        initialState = initialState.addItem(0, "dilithium", ItemType.MICROCHIP);

        return findMinimumSteps(initialState);
    }

    private RTGState parseInput(List<String> lines) {
        RTGState state = new RTGState();

        for (int floor = 0; floor < lines.size(); floor++) {
            String line = lines.get(floor);

            if (line.length() > MAX_INPUT_LENGTH) {
                throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
            }

            // Find generators
            Matcher genMatcher = GENERATOR_PATTERN.matcher(line);
            int genMatchCount = 0;
            while (genMatcher.find()) {
                if (genMatchCount >= MAX_MATCHES) {
                    throw new IllegalStateException("Exceeded maximum number of generator matches: " + MAX_MATCHES);
                }
                genMatchCount++;

                String element = genMatcher.group(1);
                state = state.addItem(floor, element, ItemType.GENERATOR);
            }

            // Find microchips
            Matcher chipMatcher = MICROCHIP_PATTERN.matcher(line);
            int chipMatchCount = 0;
            while (chipMatcher.find()) {
                if (chipMatchCount >= MAX_MATCHES) {
                    throw new IllegalStateException("Exceeded maximum number of microchip matches: " + MAX_MATCHES);
                }
                chipMatchCount++;

                String element = chipMatcher.group(1);
                state = state.addItem(floor, element, ItemType.MICROCHIP);
            }
        }

        return state;
    }

    private int findMinimumSteps(RTGState initialState) {
        Queue<StateStep> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new StateStep(initialState, 0));
        visited.add(initialState.getStateKey());

        while (!queue.isEmpty()) {
            StateStep current = queue.poll();

            if (current.state().isComplete()) {
                return current.steps();
            }

            // Generate all possible next states
            for (RTGState nextState : generateNextStates(current.state())) {
                String key = nextState.getStateKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.offer(new StateStep(nextState, current.steps() + 1));
                }
            }
        }

        return -1; // Should not happen
    }

    private List<RTGState> generateNextStates(RTGState state) {
        List<RTGState> nextStates = new ArrayList<>();
        int currentFloor = state.getElevatorFloor();

        // Get all items on current floor
        List<Item> items = state.getItemsOnFloor(currentFloor);

        // Try moving 1 or 2 items up or down
        for (int direction : new int[]{-1, 1}) {
            int targetFloor = currentFloor + direction;
            if (targetFloor < 0 || targetFloor >= 4) continue;

            // Try moving 1 item
            for (Item item : items) {
                RTGState newState = state.moveItem(currentFloor, targetFloor, item)
                        .withElevatorFloor(targetFloor);

                if (newState.isValidState()) {
                    nextStates.add(newState);
                }
            }

            // Try moving 2 items
            for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    RTGState newState = state.moveItem(currentFloor, targetFloor, items.get(i))
                            .moveItem(currentFloor, targetFloor, items.get(j))
                            .withElevatorFloor(targetFloor);

                    if (newState.isValidState()) {
                        nextStates.add(newState);
                    }
                }
            }
        }

        return nextStates;
    }
}
