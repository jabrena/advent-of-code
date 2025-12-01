package info.jab.aoc2018.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;

public class Day5 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        String polymer = ResourceLines.line(fileName);
        return reducePolymer(polymer).length();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        String polymer = ResourceLines.line(fileName);
        
        int minLength = Integer.MAX_VALUE;
        
        // Try removing each unit type (a-z)
        for (char unitType = 'a'; unitType <= 'z'; unitType++) {
            String removed = removeUnitType(polymer, unitType);
            String reduced = reducePolymer(removed);
            minLength = Math.min(minLength, reduced.length());
        }
        
        return minLength;
    }
    
    private String removeUnitType(String polymer, char unitType) {
        StringBuilder sb = new StringBuilder();
        char upperType = Character.toUpperCase(unitType);
        
        for (char c : polymer.toCharArray()) {
            if (c != unitType && c != upperType) {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }

    private String reducePolymer(String polymer) {
        List<Character> result = new ArrayList<>();
        
        for (char c : polymer.toCharArray()) {
            if (result.isEmpty()) {
                result.add(c);
            } else {
                char last = result.get(result.size() - 1);
                if (reacts(last, c)) {
                    result.remove(result.size() - 1);
                } else {
                    result.add(c);
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (char c : result) {
            sb.append(c);
        }
        return sb.toString();
    }

    private boolean reacts(char a, char b) {
        return a != b && Character.toLowerCase(a) == Character.toLowerCase(b);
    }
}
