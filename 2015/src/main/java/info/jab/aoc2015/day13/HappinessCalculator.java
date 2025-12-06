package info.jab.aoc2015.day13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public class HappinessCalculator implements Solver<Integer> {
    
    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking with alternation patterns.
     */
    private static final int MAX_INPUT_LENGTH = 10_000;
    
    /**
     * Maximum number of regex matches to prevent DoS attacks.
     * This limit prevents excessive iterations in regex find() operations.
     */
    private static final int MAX_MATCHES = 1_000;
    
    // Format: Alice would gain 54 happiness units by sitting next to Bob.
    // or: Alice would lose 79 happiness units by sitting next to Carol.    
    private static final String REGEX = "^(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)\\.$";
    private static final Pattern PATTERN_COMPILED = Pattern.compile(REGEX);

    private int findOptimalHappinessRecursive(
        List<String> remaining,
        List<String> currentArrangement,
        Map<String, Map<String, Integer>> happinessMap,
        int currentHappiness,
        int bestHappiness) {
        
        if (remaining.isEmpty()) {
            // Calculate final happiness including connection from last to first
            int size = currentArrangement.size();
            if (size > 1) {
                String last = currentArrangement.get(size - 1);
                String first = currentArrangement.get(0);
                currentHappiness += happinessMap.get(last).get(first);
                currentHappiness += happinessMap.get(first).get(last);
            }
            return Math.max(bestHappiness, currentHappiness);
        }
        
        // Branch-and-bound: if current partial happiness + optimistic estimate can't beat best, prune
        // For circular arrangements, we can't easily estimate, so we skip pruning for now
        // but the recursive approach is more memory efficient than generating all permutations
        
        for (int i = 0; i < remaining.size(); i++) {
            String nextPerson = remaining.get(i);
            // Create new list without the current element using IntStream
            final int indexToSkip = i;
            List<String> newRemaining = java.util.stream.IntStream.range(0, remaining.size())
                .filter(idx -> idx != indexToSkip)
                .mapToObj(remaining::get)
                .toList();
            
            List<String> newArrangement = new ArrayList<>(currentArrangement);
            newArrangement.add(nextPerson);
            
            int newHappiness = currentHappiness;
            if (currentArrangement.size() > 0) {
                String prevPerson = currentArrangement.get(currentArrangement.size() - 1);
                newHappiness += happinessMap.get(prevPerson).get(nextPerson);
                newHappiness += happinessMap.get(nextPerson).get(prevPerson);
            }
            
            bestHappiness = findOptimalHappinessRecursive(
                newRemaining,
                newArrangement,
                happinessMap,
                newHappiness,
                bestHappiness
            );
        }
        
        return bestHappiness;
    }

    public int findOptimalHappiness(Set<String> people, Map<String, Map<String, Integer>> happinessMap) {
        List<String> peopleList = new ArrayList<>(people);
        // Fix the first person to reduce redundant permutations from n! to (n-1)!
        String firstPerson = peopleList.remove(0);
        
        List<String> initialArrangement = new ArrayList<>();
        initialArrangement.add(firstPerson);
        
        return findOptimalHappinessRecursive(
            peopleList,
            initialArrangement,
            happinessMap,
            0,
            Integer.MIN_VALUE
        );
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

    private Map<String, Map<String, Integer>> getHappinessMap(List<String> lines) {
        AtomicInteger matchCount = new AtomicInteger(0);
        return lines.stream()
            .filter(line -> {
                if (line.length() > MAX_INPUT_LENGTH) {
                    throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
                }
                return true;
            })
            .map(PATTERN_COMPILED::matcher)
            .filter(matcher -> {
                boolean found = matcher.find();
                if (found) {
                    int count = matchCount.incrementAndGet();
                    if (count > MAX_MATCHES) {
                        throw new IllegalStateException("Exceeded maximum number of matches: " + MAX_MATCHES);
                    }
                }
                return found;
            })
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
