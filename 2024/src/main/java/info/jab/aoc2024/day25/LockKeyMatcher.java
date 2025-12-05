package info.jab.aoc2024.day25;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

public class LockKeyMatcher {

    // Enum to represent the type of schematic: KEY or LOCK
    enum Type {
        LOCK, KEY
    }

    private Map<Type, List<List<String>>> loadData(String fileName) {
        List<String> lines = new ArrayList<>(ResourceLines.list(fileName));
        lines.add(""); // Add a trailing empty line to process the last schematic

        Map<Type, List<List<String>>> schematics = new EnumMap<>(Type.class);
        schematics.put(Type.LOCK, new ArrayList<>());
        schematics.put(Type.KEY, new ArrayList<>());

        List<String> currentSchematic = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                if (!currentSchematic.isEmpty()) {
                    Type type = currentSchematic.get(0).startsWith("#") ? Type.LOCK : Type.KEY;
                    schematics.get(type).add(new ArrayList<>(currentSchematic));
                    currentSchematic.clear();
                }
            } else {
                currentSchematic.add(line);
            }
        }
        return schematics;
    }


    private Integer calculateValidKeys(Map<Type, List<List<String>>> schematics) {
        Set<String> validPairs = new HashSet<>();
        List<List<String>> locks = schematics.get(Type.LOCK);
        List<List<String>> keys = schematics.get(Type.KEY);
        for (int i = 0; i < locks.size(); i++) {
            int[] lockHeights = convertToHeights(locks.get(i), Type.LOCK);
            for (int j = 0; j < keys.size(); j++) {
                int[] keyHeights = convertToHeights(keys.get(j), Type.KEY);
                if (fits(lockHeights, keyHeights)) {
                    validPairs.add(i + "," + j);
                }
            }
        }
        return validPairs.size();
    }

    /**
     * Converts a schematic to a list of heights based on the schematic type.
     *
     * @param schematic The schematic representation as a list of strings.
     * @param type The type of schematic (LOCK or KEY).
     * @return An array of heights for each column.
     */
    private int[] convertToHeights(List<String> schematic, Type type) {
        int width = schematic.get(0).length();
        int[] heights = new int[width];

        for (int col = 0; col < width; col++) {
            int height = 0;
            if (type == Type.LOCK) {
                for (int row = 0; row < schematic.size(); row++) {
                    if (schematic.get(row).charAt(col) == '#') {
                        height = row;
                    }
                }
            } else { // Key type
                for (int row = schematic.size() - 1; row >= 0; row--) {
                    if (schematic.get(row).charAt(col) == '#') {
                        height = schematic.size() - row - 1;
                    }
                }
            }
            heights[col] = height;
        }
        return heights;
    }

    /**
     * Checks whether a key fits a lock based on their heights.
     *
     * @param lockHeights The heights of the lock's pins.
     * @param keyHeights The heights of the key's pins.
     * @return true if the key fits the lock; false otherwise.
     */
    private boolean fits(int[] lockHeights, int[] keyHeights) {
        return IntStream.range(0, lockHeights.length)
            .allMatch(i -> lockHeights[i] + keyHeights[i] <= 5);
    }

    public Integer solvePartOne(String fileName) {
        Map<Type, List<List<String>>> schematics = loadData(fileName);
        return calculateValidKeys(schematics);
    }
}
