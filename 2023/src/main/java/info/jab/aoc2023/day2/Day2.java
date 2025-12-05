package info.jab.aoc2023.day2;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Solution for AOC 2023, Day 2
 * https://adventofcode.com/2023/day/2
 *
 */
public class Day2 implements Day<Long> {

    enum CubeColor {
        RED(12),
        GREEN(13),
        BLUE(14);

        private final int maxValue;

        CubeColor(int maxValue) {
            this.maxValue = maxValue;
        }

        int getMaxValue() {
            return maxValue;
        }

        static CubeColor getColorFromName(String colorName) {
            return switch (colorName.toLowerCase()) {
                case "red" -> RED;
                case "green" -> GREEN;
                case "blue" -> BLUE;
                default -> throw new RuntimeException("Katakroker");
            };
        }
    }

    Predicate<String> isValidGame = game -> {
        for (String cubeSet : game.split(":")[1].split(";")) {
            var cubeSetParts = cubeSet.split(",");
            for (String item : cubeSetParts) {
                var tuple = item.trim().split(" ");
                String colorName = tuple[1];

                CubeColor cubeColor = CubeColor.getColorFromName(colorName);
                int value = Integer.parseInt(tuple[0]);
                int maxValue = cubeColor.getMaxValue();

                if (value > maxValue) {
                    return false;
                }
            }
        }
        return true;
    };

    Function<String, Long> extractGameId = param -> {
        return Long.valueOf(param.split(":")[0].split(" ")[1]);
    };

    // @formatter:off

    @Override
    public Long getPart1Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
            .filter(isValidGame)
            .map(extractGameId)
            .reduce(0L, Long::sum);
    }

    // @formatter:on

    Function<String, Long> calculatePower = param -> {
        long red = 0L;
        long green = 0L;
        long blue = 0L;

        for (String cubeSet : param.split(":")[1].split(";")) {
            for (String item : cubeSet.split(",")) {
                var tuple = item.trim().split(" ");
                int value = Integer.parseInt(tuple[0]);

                switch (tuple[1]) {
                    case "red" -> red = Math.max(red, value);
                    case "green" -> green = Math.max(green, value);
                    case "blue" -> blue = Math.max(blue, value);
                    default -> throw new RuntimeException("Katakroker");
                }
            }
        }
        return red * green * blue;
    };

    // @formatter:off

    @Override
    public Long getPart2Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
            .map(calculatePower)
            .reduce(0L, Long::sum);
    }
    // @formatter:on

}
