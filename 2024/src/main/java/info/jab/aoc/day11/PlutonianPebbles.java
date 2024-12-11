package info.jab.aoc.day11;

import java.util.ArrayList;
import java.util.List;

public class PlutonianPebbles {

    public static List<String> blink(List<String> stones) {
        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                newStones.add("1");
            } else if (stone.length() % 2 == 0) {
                // Split the stone into two halves
                int mid = stone.length() / 2;
                String leftHalf = stone.substring(0, mid);
                String rightHalf = stone.substring(mid);

                // Remove leading zeroes from each half and ensure non-empty result
                String left = removeLeadingZeroes(leftHalf);
                String right = removeLeadingZeroes(rightHalf);

                newStones.add(left.isEmpty() ? "0" : left);
                newStones.add(right.isEmpty() ? "0" : right);
            } else {
                // Replace with a new stone engraved with the number * 2024
                long number = Long.parseLong(stone);
                newStones.add(String.valueOf(number * 2024));
            }
        }

        return newStones;
    }

    private static String removeLeadingZeroes(String s) {
        return s.replaceFirst("^0+", "");
    }
}

