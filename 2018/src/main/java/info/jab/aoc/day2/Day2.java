package info.jab.aoc.day2;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day2 implements Solver<String> {

    @Override
    public String solvePartOne(String fileName) {
        List<String> boxIds = ResourceLines.list(fileName);
        
        long countWithTwo = 0;
        long countWithThree = 0;
        
        for (String boxId : boxIds) {
            Map<Character, Long> charCounts = boxId.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            
            boolean hasExactlyTwo = charCounts.values().stream().anyMatch(count -> count == 2);
            boolean hasExactlyThree = charCounts.values().stream().anyMatch(count -> count == 3);
            
            if (hasExactlyTwo) {
                countWithTwo++;
            }
            if (hasExactlyThree) {
                countWithThree++;
            }
        }
        
        return String.valueOf(countWithTwo * countWithThree);
    }

    @Override
    public String solvePartTwo(String fileName) {
        List<String> boxIds = ResourceLines.list(fileName);
        
        for (int i = 0; i < boxIds.size(); i++) {
            String id1 = boxIds.get(i);
            for (int j = i + 1; j < boxIds.size(); j++) {
                String id2 = boxIds.get(j);
                String common = findCommonLetters(id1, id2);
                if (common != null) {
                    return common;
                }
            }
        }
        
        return null;
    }
    
    private String findCommonLetters(String id1, String id2) {
        if (id1.length() != id2.length()) {
            return null;
        }
        
        int differences = 0;
        int diffIndex = -1;
        
        for (int i = 0; i < id1.length(); i++) {
            if (id1.charAt(i) != id2.charAt(i)) {
                differences++;
                if (differences > 1) {
                    return null;
                }
                diffIndex = i;
            }
        }
        
        if (differences == 1) {
            return id1.substring(0, diffIndex) + id1.substring(diffIndex + 1);
        }
        
        return null;
    }
}
