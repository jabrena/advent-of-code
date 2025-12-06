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
        State initialState = parseInput(lines);
        return findMinimumSteps(initialState);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        State initialState = parseInput(lines);

        // For part 2, add elerium and dilithium items to the first floor
        initialState.addItem(0, "elerium", ItemType.GENERATOR);
        initialState.addItem(0, "elerium", ItemType.MICROCHIP);
        initialState.addItem(0, "dilithium", ItemType.GENERATOR);
        initialState.addItem(0, "dilithium", ItemType.MICROCHIP);

        return findMinimumSteps(initialState);
    }

    private State parseInput(List<String> lines) {
        State state = new State();

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
                state.addItem(floor, element, ItemType.GENERATOR);
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
                state.addItem(floor, element, ItemType.MICROCHIP);
            }
        }

        return state;
    }

    private int findMinimumSteps(State initialState) {
        Queue<StateStep> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new StateStep(initialState, 0));
        visited.add(initialState.getStateKey());

        while (!queue.isEmpty()) {
            StateStep current = queue.poll();

            if (current.state.isComplete()) {
                return current.steps;
            }

            // Generate all possible next states
            for (State nextState : generateNextStates(current.state)) {
                String key = nextState.getStateKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.offer(new StateStep(nextState, current.steps + 1));
                }
            }
        }

        return -1; // Should not happen
    }

    private List<State> generateNextStates(State state) {
        List<State> nextStates = new ArrayList<>();
        int currentFloor = state.getElevatorFloor();

        // Get all items on current floor
        List<Item> items = state.getItemsOnFloor(currentFloor);

        // Try moving 1 or 2 items up or down
        for (int direction : new int[]{-1, 1}) {
            int targetFloor = currentFloor + direction;
            if (targetFloor < 0 || targetFloor >= 4) continue;

            // Try moving 1 item
            for (Item item : items) {
                State newState = state.copy();
                newState.moveItem(currentFloor, targetFloor, item);
                newState.setElevatorFloor(targetFloor);

                if (newState.isValidState()) {
                    nextStates.add(newState);
                }
            }

            // Try moving 2 items
            for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    State newState = state.copy();
                    newState.moveItem(currentFloor, targetFloor, items.get(i));
                    newState.moveItem(currentFloor, targetFloor, items.get(j));
                    newState.setElevatorFloor(targetFloor);

                    if (newState.isValidState()) {
                        nextStates.add(newState);
                    }
                }
            }
        }

        return nextStates;
    }

    static class StateStep {
        State state;
        int steps;

        StateStep(State state, int steps) {
            this.state = state;
            this.steps = steps;
        }
    }

    static class Item {
        String element;
        ItemType type;

        Item(String element, ItemType type) {
            this.element = element;
            this.type = type;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Item item = (Item) obj;
            return Objects.equals(element, item.element) && type == item.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, type);
        }

        @Override
        public String toString() {
            return element + "_" + type;
        }
    }

    enum ItemType {
        GENERATOR, MICROCHIP
    }

    static class State {
        private int elevatorFloor = 0;
        private Map<Integer, Set<Item>> floors = new HashMap<>();

        State() {
            for (int i = 0; i < 4; i++) {
                floors.put(i, new HashSet<>());
            }
        }

        void addItem(int floor, String element, ItemType type) {
            floors.get(floor).add(new Item(element, type));
        }

        void moveItem(int fromFloor, int toFloor, Item item) {
            floors.get(fromFloor).remove(item);
            floors.get(toFloor).add(item);
        }

        List<Item> getItemsOnFloor(int floor) {
            return new ArrayList<>(floors.get(floor));
        }

        int getElevatorFloor() {
            return elevatorFloor;
        }

        void setElevatorFloor(int floor) {
            this.elevatorFloor = floor;
        }

        boolean isComplete() {
            // All items should be on floor 3 (4th floor)
            for (int i = 0; i < 3; i++) {
                if (!floors.get(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        boolean isValidState() {
            // Check each floor for radiation safety
            for (int floor = 0; floor < 4; floor++) {
                if (!isFloorSafe(floor)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isFloorSafe(int floor) {
            Set<Item> items = floors.get(floor);
            Set<String> generators = new HashSet<>();
            Set<String> microchips = new HashSet<>();

            for (Item item : items) {
                if (item.type == ItemType.GENERATOR) {
                    generators.add(item.element);
                } else {
                    microchips.add(item.element);
                }
            }

            // If there are no generators, it's safe
            if (generators.isEmpty()) {
                return true;
            }

            // Check if any microchip is unprotected
            for (String chip : microchips) {
                if (!generators.contains(chip)) {
                    return false; // Unprotected microchip
                }
            }

            return true;
        }

        State copy() {
            State newState = new State();
            newState.elevatorFloor = this.elevatorFloor;
            for (int floor = 0; floor < 4; floor++) {
                newState.floors.get(floor).addAll(this.floors.get(floor));
            }
            return newState;
        }

        String getStateKey() {
            // Create a canonical representation for memoization
            // We can use element pairs instead of specific elements for better pruning
            StringBuilder sb = new StringBuilder();
            sb.append(elevatorFloor).append("|");

            for (int floor = 0; floor < 4; floor++) {
                Set<Item> items = floors.get(floor);
                Map<String, Integer> elementFloors = new HashMap<>();

                for (Item item : items) {
                    elementFloors.merge(item.element,
                        item.type == ItemType.GENERATOR ? 1000 + floor : floor,
                        Integer::sum);
                }

                List<Integer> pairs = new ArrayList<>(elementFloors.values());
                Collections.sort(pairs);
                sb.append(pairs).append("|");
            }

            return sb.toString();
        }
    }
}
