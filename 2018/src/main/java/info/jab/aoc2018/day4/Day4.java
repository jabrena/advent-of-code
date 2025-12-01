package info.jab.aoc2018.day4;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 implements Solver<Integer> {

    private static final Pattern LOG_PATTERN = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})\\] (.+)");
    private static final Pattern GUARD_PATTERN = Pattern.compile("Guard #(\\d+) begins shift");

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        // Parse and sort log entries
        List<LogEntry> entries = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = LOG_PATTERN.matcher(line);
            if (matcher.matches()) {
                String timestamp = matcher.group(1);
                String action = matcher.group(2);
                entries.add(new LogEntry(timestamp, action));
            }
        }
        
        entries.sort(Comparator.comparing(e -> e.timestamp));
        
        // Process entries to track guard sleep times
        Map<Integer, int[]> guardSleepMinutes = new HashMap<>(); // guardId -> array of 60 minutes
        int currentGuard = -1;
        int sleepStart = -1;
        
        for (LogEntry entry : entries) {
            Matcher guardMatcher = GUARD_PATTERN.matcher(entry.action);
            if (guardMatcher.matches()) {
                currentGuard = Integer.parseInt(guardMatcher.group(1));
            } else if ("falls asleep".equals(entry.action)) {
                sleepStart = extractMinute(entry.timestamp);
            } else if ("wakes up".equals(entry.action)) {
                int sleepEnd = extractMinute(entry.timestamp);
                guardSleepMinutes.putIfAbsent(currentGuard, new int[60]);
                int[] minutes = guardSleepMinutes.get(currentGuard);
                for (int i = sleepStart; i < sleepEnd; i++) {
                    minutes[i]++;
                }
            }
        }
        
        // Find guard with most total minutes asleep
        int maxGuard = -1;
        int maxTotalMinutes = -1;
        for (Map.Entry<Integer, int[]> entry : guardSleepMinutes.entrySet()) {
            int total = 0;
            for (int count : entry.getValue()) {
                total += count;
            }
            if (total > maxTotalMinutes) {
                maxTotalMinutes = total;
                maxGuard = entry.getKey();
            }
        }
        
        // Find the minute that guard is most often asleep
        int[] minutes = guardSleepMinutes.get(maxGuard);
        int maxMinute = 0;
        int maxCount = -1;
        for (int i = 0; i < 60; i++) {
            if (minutes[i] > maxCount) {
                maxCount = minutes[i];
                maxMinute = i;
            }
        }
        
        return maxGuard * maxMinute;
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        // Parse and sort log entries
        List<LogEntry> entries = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = LOG_PATTERN.matcher(line);
            if (matcher.matches()) {
                String timestamp = matcher.group(1);
                String action = matcher.group(2);
                entries.add(new LogEntry(timestamp, action));
            }
        }
        
        entries.sort(Comparator.comparing(e -> e.timestamp));
        
        // Process entries to track guard sleep times
        Map<Integer, int[]> guardSleepMinutes = new HashMap<>(); // guardId -> array of 60 minutes
        int currentGuard = -1;
        int sleepStart = -1;
        
        for (LogEntry entry : entries) {
            Matcher guardMatcher = GUARD_PATTERN.matcher(entry.action);
            if (guardMatcher.matches()) {
                currentGuard = Integer.parseInt(guardMatcher.group(1));
            } else if ("falls asleep".equals(entry.action)) {
                sleepStart = extractMinute(entry.timestamp);
            } else if ("wakes up".equals(entry.action)) {
                int sleepEnd = extractMinute(entry.timestamp);
                guardSleepMinutes.putIfAbsent(currentGuard, new int[60]);
                int[] minutes = guardSleepMinutes.get(currentGuard);
                for (int i = sleepStart; i < sleepEnd; i++) {
                    minutes[i]++;
                }
            }
        }
        
        // Find the guard-minute combination with the highest frequency
        int maxGuard = -1;
        int maxMinute = -1;
        int maxCount = -1;
        
        for (Map.Entry<Integer, int[]> entry : guardSleepMinutes.entrySet()) {
            int guardId = entry.getKey();
            int[] minutes = entry.getValue();
            for (int minute = 0; minute < 60; minute++) {
                if (minutes[minute] > maxCount) {
                    maxCount = minutes[minute];
                    maxGuard = guardId;
                    maxMinute = minute;
                }
            }
        }
        
        return maxGuard * maxMinute;
    }
    
    private int extractMinute(String timestamp) {
        // timestamp format: "1518-11-01 00:05"
        String[] parts = timestamp.split(" ");
        String timePart = parts[1];
        String[] timeParts = timePart.split(":");
        return Integer.parseInt(timeParts[1]);
    }
    
    private static class LogEntry {
        final String timestamp;
        final String action;
        
        LogEntry(String timestamp, String action) {
            this.timestamp = timestamp;
            this.action = action;
        }
    }
}
