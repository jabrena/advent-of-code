package info.jab.aoc2016.day4;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2016/day/4
 **/
public class Day4 implements Day<Integer> {

    private static final Pattern ROOM_PATTERN = Pattern.compile("^([a-z-]+)-(\\d+)\\[([a-z]+)\\]$");

    private record Room(String encryptedName, int sectorId, String checksum) {
        static Room parse(String line) {
            var matcher = ROOM_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid room format: " + line);
            }
            return new Room(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
        }
        
        boolean isReal() {
            // Count frequency of each letter (excluding dashes)
            Map<Character, Integer> letterCounts = new HashMap<>();
            for (char c : encryptedName.toCharArray()) {
                if (c != '-') {
                    letterCounts.merge(c, 1, Integer::sum);
                }
            }
            
            // Get the 5 most common letters, with ties broken alphabetically
            String expectedChecksum = letterCounts.entrySet().stream()
                .sorted((e1, e2) -> {
                    int countCompare = Integer.compare(e2.getValue(), e1.getValue()); // descending by count
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

    public boolean isRealRoom(String roomLine) {
        return Room.parse(roomLine).isReal();
    }
    
    public int sumRealRoomSectorIds(List<String> roomLines) {
        return roomLines.stream()
            .map(Room::parse)
            .filter(Room::isReal)
            .mapToInt(Room::sectorId)
            .sum();
    }
    
    public String decryptRoomName(String encryptedName, int sectorId) {
        StringBuilder decrypted = new StringBuilder();
        
        for (char c : encryptedName.toCharArray()) {
            if (c == '-') {
                decrypted.append(' ');
            } else {
                // Shift the letter forward by sectorId positions
                int shifted = ((c - 'a' + sectorId) % 26);
                decrypted.append((char) ('a' + shifted));
            }
        }
        
        return decrypted.toString();
    }
    
    public int findNorthPoleObjectsRoom(List<String> roomLines) {
        return roomLines.stream()
            .map(Room::parse)
            .filter(Room::isReal)
            .filter(room -> {
                String decryptedName = decryptRoomName(room.encryptedName, room.sectorId);
                return decryptedName.contains("northpole object") || decryptedName.contains("north pole object");
            })
            .mapToInt(Room::sectorId)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("North Pole objects room not found"));
    }

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return sumRealRoomSectorIds(lines);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return findNorthPoleObjectsRoom(lines);
    }
}