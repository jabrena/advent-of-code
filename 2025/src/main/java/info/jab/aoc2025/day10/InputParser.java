package info.jab.aoc2025.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputParser {

    private static final Pattern BUTTONS_PATTERN = Pattern.compile("\\(([0-9,]+)\\)");

    private InputParser() {}

    public static Part1Problem parsePart1(String line) {
        int openBracket = line.indexOf('[');
        int closeBracket = line.indexOf(']');
        String diagram = line.substring(openBracket + 1, closeBracket);

        long target = 0;
        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) == '#') {
                target |= (1L << i);
            }
        }

        int openBrace = line.indexOf('{');
        String buttonsPart = line.substring(closeBracket + 1, openBrace).trim();

        List<Long> buttonsList = new ArrayList<>();
        Matcher m = BUTTONS_PATTERN.matcher(buttonsPart);
        while (m.find()) {
            String content = m.group(1);
            long buttonMask = 0;
            String[] parts = content.split(",");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    int bit = Integer.parseInt(part);
                    buttonMask |= (1L << bit);
                }
            }
            buttonsList.add(buttonMask);
        }

        long[] buttons = buttonsList.stream().mapToLong(l -> l).toArray();
        return new Part1Problem(target, buttons);
    }

    public static Part2Problem parsePart2(String line) {
        int openBrace = line.indexOf('{');
        int closeBrace = line.indexOf('}');
        String joltagePart = line.substring(openBrace + 1, closeBrace);
        String[] joltageStrs = joltagePart.split(",");
        int[] targets = new int[joltageStrs.length];
        for (int i = 0; i < joltageStrs.length; i++) {
            targets[i] = Integer.parseInt(joltageStrs[i].trim());
        }

        int closeBracket = line.indexOf(']');
        int openBraceIdx = line.indexOf('{');
        String buttonsPart = line.substring(closeBracket + 1, openBraceIdx).trim();
        List<int[]> buttonsList = new ArrayList<>();
        Matcher m = BUTTONS_PATTERN.matcher(buttonsPart);
        while (m.find()) {
            String content = m.group(1);
            List<Integer> buttonList = new ArrayList<>();
            String[] parts = content.split(",");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    buttonList.add(Integer.parseInt(part.trim()));
                }
            }
            buttonsList.add(buttonList.stream().mapToInt(i -> i).toArray());
        }

        int[][] buttons = buttonsList.toArray(new int[0][]);
        return new Part2Problem(targets, buttons);
    }
}
