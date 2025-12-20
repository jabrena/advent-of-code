package info.jab.aoc2016.day4;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents an encrypted room with validation logic.
 */
public record Room(String encryptedName, int sectorId, String checksum) {
    private static final Pattern ROOM_PATTERN = Pattern.compile("^([a-z-]+)-(\\d+)\\[([a-z]+)\\]$");
    
    static Room parse(final String line) {
        var matcher = ROOM_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid room format: " + line);
        }
        return new Room(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
    }
    
    boolean isReal() {
        // Count frequency of each letter (excluding dashes) using Stream API
        String expectedChecksum = encryptedName.chars()
            .filter(c -> c != '-')
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(
                c -> c,
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted((e1, e2) -> {
                int countCompare = Long.compare(e2.getValue(), e1.getValue()); // descending by count
                if (countCompare == 0) {
                    return Character.compare(e1.getKey(), e2.getKey()); // ascending alphabetically
                }
                return countCompare;
            })
            .limit(5)
            .map(Map.Entry::getKey)
            .map(String::valueOf)
            .collect(Collectors.joining());
        
        return checksum.equals(expectedChecksum);
    }
}

