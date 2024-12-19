package info.jab.aoc.day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import com.putoet.resources.ResourceLines;

public class TowelArrangement {

    // Function to determine if a design can be formed using the available patterns
    public boolean canFormDesign(String design, Set<String> towelPatterns) {
        int n = design.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // Empty design can always be formed

        for (int i = 1; i <= n; i++) {
            for (String pattern : towelPatterns) {
                int len = pattern.length();
                if (i >= len && design.substring(i - len, i).equals(pattern)) {
                    dp[i] = dp[i] || dp[i - len];
                }
            }
        }
        return dp[n];
    }

    private record InputData(Set<String> towelPatterns, List<String> designList) {}

    private InputData getInputData(String fileName) {
        List<String> list = ResourceLines.list(fileName);
        List<String> patternsList = new ArrayList<>();
        List<String> designsList = new ArrayList<>();

        boolean isPatternSection = true;
        for (String line : list) {
            if (line.isEmpty()) {
                isPatternSection = false; // Switch to designs section
                continue;
            }
            if (isPatternSection) {
                patternsList.addAll(Arrays.asList(line.split(",\\s*"))); // Split by comma and trim spaces
            } else {
                designsList.add(line);
            }
        }
        Set<String> towelPatterns = new HashSet<>(patternsList);

        return new InputData(towelPatterns, designsList);
    }

    public Long solve1(String fileName) {
        var inputData = getInputData(fileName);

        // Count how many designs are possible
        return inputData.designList().stream()
            .filter(design -> canFormDesign(design, inputData.towelPatterns()))
            .count();
    }

    // Function to calculate the number of ways to form a design
    public long countWaysToFormDesign(String design, Set<String> towelPatterns) {
        int n = design.length();
        long[] dp = new long[n + 1];
        dp[0] = 1; // There's one way to form an empty design

        for (int i = 1; i <= n; i++) {
            for (String pattern : towelPatterns) {
                int len = pattern.length();
                if (i >= len && design.substring(i - len, i).equals(pattern)) {
                    dp[i] += dp[i - len]; // Add the number of ways to form the remaining design
                }
            }
        }
        return dp[n];
    }

    public Long solve2(String fileName) {
        var inputData = getInputData(fileName);

        // Calculate the total number of ways to form all designs
        return inputData.designList().stream()
            .mapToLong(design -> countWaysToFormDesign(design, inputData.towelPatterns()))
            .sum();
    }

    //Maintaining the main class to test JOL features
    public static void main(String[] args) {
        // Input towel patterns
        String[] patternsArray = {"r", "wr", "b", "g", "bwu", "rb", "gb", "br"};
        Set<String> towelPatterns = new HashSet<>(Arrays.asList(patternsArray));

        // Input desired designs
        String[] designsArray = {
            "brwrr",
            "bggr",
            "gbbr",
            "rrbgbr",
            "ubwu",
            "bwurrg",
            "brgr",
            "bbrgwb"
        };

        TowelArrangement towelArrangement = new TowelArrangement();
        // Count how many designs are possible
        int possibleCount = 0;
        for (String design : designsArray) {
            if (towelArrangement.canFormDesign(design, towelPatterns)) {
                possibleCount++;
            }
        }

        // Output the result
        System.out.println("Number of possible designs: " + possibleCount);

        //Memory layout
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseInstance(towelArrangement).toPrintable());
        System.out.println(GraphLayout.parseInstance(towelArrangement).toFootprint());
    }
}

