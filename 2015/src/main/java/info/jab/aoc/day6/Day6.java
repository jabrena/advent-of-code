package info.jab.aoc.day6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/6
 */
public class Day6 implements Day<Long> {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)");

    @Override
    public Long getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);

        LightController lightController = new LightController();

        for (String line : lines) {
            Matcher matcher = COMMAND_PATTERN.matcher(line);
            if (matcher.find()) {
                String command = matcher.group(1);      // "turn off", "turn on", o "toggle"
                String start = matcher.group(2);        // "301,3"
                String end = matcher.group(3); 
                var startParts = start.split(",");
                var endParts = end.split(",");
                lightController.executeCommand(command, 
                    Integer.parseInt(startParts[0]), Integer.parseInt(startParts[1]), 
                    Integer.parseInt(endParts[0]), Integer.parseInt(endParts[1]));
            }
        }

        return 0L + lightController.getTotalLightsOn();
    }

    @Override
    public Long getPart2Result(String fileName) {

        var lines = ResourceLines.list(fileName);

        LightGrid lightGrid = new LightGrid();

        for (String line : lines) {
            lightGrid.processInstructionsPart2(line);
        }

        return lightGrid.getTotalBrightness();
    }
}
