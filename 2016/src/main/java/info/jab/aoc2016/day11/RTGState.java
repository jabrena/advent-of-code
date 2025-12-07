package info.jab.aoc2016.day11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents the state of the RTG facility.
 */
public final class RTGState {
    private final int elevatorFloor;
    private final Map<Integer, Set<Item>> floors;

    public RTGState() {
        this(0, createEmptyFloors());
    }

    private RTGState(final int elevatorFloor, final Map<Integer, Set<Item>> floors) {
        this.elevatorFloor = elevatorFloor;
        this.floors = Map.copyOf(floors);
    }

    private static Map<Integer, Set<Item>> createEmptyFloors() {
        return IntStream.range(0, 4)
                .boxed()
                .collect(Collectors.toUnmodifiableMap(
                        floor -> floor,
                        floor -> Set.of()
                ));
    }

    public RTGState addItem(final int floor, final String element, final ItemType type) {
        Item newItem = new Item(element, type);
        Map<Integer, Set<Item>> newFloors = IntStream.range(0, 4)
                .boxed()
                .collect(Collectors.toUnmodifiableMap(
                        f -> f,
                        f -> f == floor
                                ? Stream.concat(
                                        floors.getOrDefault(f, Set.of()).stream(),
                                        Stream.of(newItem)
                                ).collect(Collectors.toUnmodifiableSet())
                                : Set.copyOf(floors.getOrDefault(f, Set.of()))
                ));
        return new RTGState(elevatorFloor, newFloors);
    }

    public RTGState moveItem(final int fromFloor, final int toFloor, final Item item) {
        Map<Integer, Set<Item>> newFloors = IntStream.range(0, 4)
                .boxed()
                .collect(Collectors.toUnmodifiableMap(
                        f -> f,
                        f -> {
                            if (f == fromFloor) {
                                return floors.getOrDefault(f, Set.of()).stream()
                                        .filter(i -> !i.equals(item))
                                        .collect(Collectors.toUnmodifiableSet());
                            } else if (f == toFloor) {
                                return Stream.concat(
                                        floors.getOrDefault(f, Set.of()).stream(),
                                        Stream.of(item)
                                ).collect(Collectors.toUnmodifiableSet());
                            } else {
                                return Set.copyOf(floors.getOrDefault(f, Set.of()));
                            }
                        }
                ));
        return new RTGState(elevatorFloor, newFloors);
    }

    public List<Item> getItemsOnFloor(final int floor) {
        return floors.getOrDefault(floor, Set.of()).stream()
                .toList();
    }

    public int getElevatorFloor() {
        return elevatorFloor;
    }

    public RTGState withElevatorFloor(final int floor) {
        return new RTGState(floor, floors);
    }

    public boolean isComplete() {
        // All items should be on floor 3 (4th floor)
        return IntStream.range(0, 3)
                .allMatch(i -> floors.getOrDefault(i, Set.of()).isEmpty());
    }

    public boolean isValidState() {
        // Check each floor for radiation safety
        return IntStream.range(0, 4)
                .allMatch(this::isFloorSafe);
    }

    private boolean isFloorSafe(final int floor) {
        Set<Item> items = floors.getOrDefault(floor, Set.of());
        
        Set<String> generators = items.stream()
                .filter(item -> item.type() == ItemType.GENERATOR)
                .map(Item::element)
                .collect(Collectors.toUnmodifiableSet());
        
        Set<String> microchips = items.stream()
                .filter(item -> item.type() == ItemType.MICROCHIP)
                .map(Item::element)
                .collect(Collectors.toUnmodifiableSet());

        // If there are no generators, it's safe
        if (generators.isEmpty()) {
            return true;
        }

        // Check if any microchip is unprotected
        return microchips.stream()
                .allMatch(generators::contains);
    }

    public String getStateKey() {
        // Create a canonical representation for memoization
        // We can use element pairs instead of specific elements for better pruning
        return IntStream.range(0, 4)
                .boxed()
                .map(floor -> {
                    Set<Item> items = floors.getOrDefault(floor, Set.of());
                    Map<String, Integer> elementFloors = items.stream()
                            .collect(Collectors.toMap(
                                    Item::element,
                                    item -> item.type() == ItemType.GENERATOR ? 1000 + floor : floor,
                                    Integer::sum
                            ));
                    
                    return elementFloors.values().stream()
                            .sorted()
                            .toList();
                })
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        pairs -> elevatorFloor + "|" + pairs.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining("|")) + "|"
                ));
    }
}

