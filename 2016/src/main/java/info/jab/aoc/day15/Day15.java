package info.jab.aoc.day15;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/15
 */
public class Day15 implements Day<Integer> {

    private static final Pattern DISC_PATTERN = Pattern.compile(
            "Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+)\\."
    );

    record Disc(int number, int positions, int initialPosition) {
    }

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Disc> discs = parseDiscs(lines);
        return findFirstValidTime(discs);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Disc> discs = parseDiscs(lines);
        // Add disc 7 for part 2
        discs.add(new Disc(7, 11, 0));
        return findFirstValidTime(discs);
    }

    private List<Disc> parseDiscs(List<String> lines) {
        List<Disc> discs = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = DISC_PATTERN.matcher(line);
            if (matcher.matches()) {
                int number = Integer.parseInt(matcher.group(1));
                int positions = Integer.parseInt(matcher.group(2));
                int initialPosition = Integer.parseInt(matcher.group(3));
                discs.add(new Disc(number, positions, initialPosition));
            }
        }
        return discs;
    }

    private int findFirstValidTime(List<Disc> discs) {
        int time = 0;
        while (true) {
            boolean allAligned = true;
            for (Disc disc : discs) {
                // The capsule reaches disc i at time (buttonTime + i)
                // Disc position at that time: (initialPosition + buttonTime + i) % positions
                int arrivalTime = time + disc.number();
                int position = (disc.initialPosition() + arrivalTime) % disc.positions();
                if (position != 0) {
                    allAligned = false;
                    break;
                }
            }
            if (allAligned) {
                return time;
            }
            time++;
        }
    }
}
