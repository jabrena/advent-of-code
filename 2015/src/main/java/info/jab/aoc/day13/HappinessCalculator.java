package info.jab.aoc.day13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public class HappinessCalculator implements Solver<Integer> {
    
    // Format: Alice would gain 54 happiness units by sitting next to Bob.
    // or: Alice would lose 79 happiness units by sitting next to Carol.    
    private static final String REGEX = "^(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)\\.$";
    private static final Pattern PATTERN_COMPILED = Pattern.compile(REGEX);

    private int calculateHappiness(List<String> arrangement, Map<String, Map<String, Integer>> happinessMap) {
        return IntStream.range(0, arrangement.size())
            .map(i -> {
                int size = arrangement.size();
                String current = arrangement.get(i);
                String left = arrangement.get((i - 1 + size) % size);
                String right = arrangement.get((i + 1) % size);
                
                return happinessMap.get(current).get(left) + 
                       happinessMap.get(current).get(right);
            })
            .sum();
    }

    private List<List<String>> generatePermutations(List<String> items) {
        if (items.size() <= 1) {
            return Collections.singletonList(items);
        }

        List<List<String>> result = new ArrayList<>();
        String first = items.get(0);
        List<String> remaining = items.subList(1, items.size());
        
        for (List<String> perm : generatePermutations(remaining)) {
            for (int i = 0; i <= perm.size(); i++) {
                List<String> newPerm = new ArrayList<>(perm);
                newPerm.add(i, first);
                result.add(newPerm);
            }
        }
        return result;
    }

    public int findOptimalHappiness(Set<String> people, Map<String, Map<String, Integer>> happinessMap) {
        List<String> peopleList = new ArrayList<>(people);
        // Fix the first person to reduce redundant permutations
        String firstPerson = peopleList.remove(0);
        
        int maxHappiness = Integer.MIN_VALUE;
        
        for (List<String> perm : generatePermutations(peopleList)) {
            List<String> arrangement = new ArrayList<>();
            arrangement.add(firstPerson);
            arrangement.addAll(perm);
            
            int happiness = calculateHappiness(arrangement, happinessMap);
            maxHappiness = Math.max(maxHappiness, happiness);
        }
        return maxHappiness;
    }

    private Set<String> getPeople(Map<String, Map<String, Integer>> happinessMap) {
        final Set<String> people = new HashSet<>();
        people.addAll(happinessMap.keySet());
        happinessMap.values().stream()
                   .map(Map::keySet)
                   .flatMap(Set::stream)
                   .forEach(people::add);
        return people;
    }

    enum HappinessType {
        GAIN("gain"),
        LOSE("lose");
    
        private final String value;
    
        HappinessType(String value) {
            this.value = value;
        }
    
        public String getValue() {
            return value;
        }
    
        public static HappinessType from(String text) {
            return Stream.of(HappinessType.values())
                .filter(type -> type.value.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HappinessType with text " + text + " found"));
        }
    }

    private Map<String, Map<String, Integer>> getHappinessMap(List<String> lines) {
        return lines.stream()
            .map(PATTERN_COMPILED::matcher)
            .filter(Matcher::find)
            .collect(Collectors.groupingBy(
                matcher -> matcher.group(1),
                Collectors.toMap(
                    matcher -> matcher.group(4),
                    matcher -> {
                        HappinessType type = HappinessType.from(matcher.group(2));
                        int value = Integer.parseInt(matcher.group(3));
                        return type == HappinessType.GAIN ? value : -value;
                    }
                )
            ));
    }

    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        var happinessMap = getHappinessMap(lines);
        var people = getPeople(happinessMap);
        return findOptimalHappiness(people, happinessMap);
    }

    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        var happinessMap = getHappinessMap(lines);
        var people = getPeople(happinessMap);

        final String ME = "Me";
        // Create a new map instead of modifying the existing one
        Set<String> existingPeople = Set.copyOf(people);
        Map<String, Map<String, Integer>> extendedMap = new HashMap<>(happinessMap);
        for (String person : existingPeople) {
            extendedMap.computeIfAbsent(ME, k -> new HashMap<>()).put(person, 0);
            extendedMap.computeIfAbsent(person, k -> new HashMap<>()).put(ME, 0);
        }
        people.add(ME);

        return findOptimalHappiness(people, extendedMap);
    }
}
