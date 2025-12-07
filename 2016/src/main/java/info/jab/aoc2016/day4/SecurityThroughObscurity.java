package info.jab.aoc2016.day4;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

/**
 * Solver for Day 4: Security Through Obscurity
 * Validates and decrypts room names based on checksums.
 */
public final class SecurityThroughObscurity implements Solver<Integer> {

    public boolean isRealRoom(final String roomLine) {
        return Room.parse(roomLine).isReal();
    }

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return sumRealRoomSectorIds(lines);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return findNorthPoleObjectsRoom(lines);
    }

    public int sumRealRoomSectorIds(final List<String> roomLines) {
        return roomLines.stream()
            .map(Room::parse)
            .filter(Room::isReal)
            .mapToInt(Room::sectorId)
            .sum();
    }
    
    public String decryptRoomName(final String encryptedName, final int sectorId) {
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
    
    private int findNorthPoleObjectsRoom(final List<String> roomLines) {
        return roomLines.stream()
            .map(Room::parse)
            .filter(Room::isReal)
            .filter(room -> {
                String decryptedName = decryptRoomName(room.encryptedName(), room.sectorId());
                return decryptedName.contains("northpole object") || decryptedName.contains("north pole object");
            })
            .mapToInt(Room::sectorId)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("North Pole objects room not found"));
    }
}

