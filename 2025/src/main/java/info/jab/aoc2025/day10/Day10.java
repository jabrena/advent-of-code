package info.jab.aoc2025.day10;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return lines.stream()
            .filter(line -> line.contains("["))
            .mapToLong(this::solveMachine)
            .sum();
    }

    @Override
    public Long getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    private long solveMachine(String line) {
        // Parse line
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
        
        List<Long> buttons = new ArrayList<>();
        Pattern p = Pattern.compile("\\(([0-9,]+)\\)");
        Matcher m = p.matcher(buttonsPart);
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
            buttons.add(buttonMask);
        }
        
        return findMinPresses(target, buttons);
    }
    
    private long findMinPresses(long target, List<Long> buttons) {
        int n = buttons.size();
        long minPresses = Long.MAX_VALUE;
        
        long limit = 1L << n;
        for (long i = 0; i < limit; i++) {
            long current = 0;
            int presses = 0;
            for (int j = 0; j < n; j++) {
                if ((i & (1L << j)) != 0) {
                    current ^= buttons.get(j);
                    presses++;
                }
            }
            
            if (current == target) {
                if (presses < minPresses) {
                    minPresses = presses;
                }
            }
        }
        
        return minPresses == Long.MAX_VALUE ? 0 : minPresses;
    }
}
