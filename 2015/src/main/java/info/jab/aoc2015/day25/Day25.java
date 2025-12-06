package info.jab.aoc2015.day25;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.stream.Collectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2015/day/25
 */
public class Day25 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        String input = ResourceLines.stream("/" + fileName)
                .collect(Collectors.joining("\n"));
        
        // Parse the target row and column from input
        Pattern pattern = Pattern.compile("row (\\d+), column (\\d+)");
        Matcher matcher = pattern.matcher(input);
        
        if (!matcher.find()) {
            throw new RuntimeException("Could not parse row and column from input");
        }
        
        int targetRow = Integer.parseInt(matcher.group(1));
        int targetColumn = Integer.parseInt(matcher.group(2));
        
        // Calculate the position in the diagonal sequence
        // The formula to get the position (1-indexed) for row r, column c is:
        // position = (r + c - 2) * (r + c - 1) / 2 + c
        long position = (long)(targetRow + targetColumn - 2) * (targetRow + targetColumn - 1) / 2 + targetColumn;
        
        // Generate the code at that position using modular exponentiation
        // Formula: code = (20151125 * 252533^(position-1)) mod 33554393
        // This reduces complexity from O(p) to O(log p)
        long base = 252533L;
        long modulus = 33554393L;
        long exponent = position - 1;
        
        // Fast modular exponentiation: base^exponent mod modulus
        long power = modPow(base, exponent, modulus);
        long code = (20151125L * power) % modulus;
        
        return code;
    }
    
    /**
     * Fast modular exponentiation: calculates (base^exponent) mod modulus in O(log exponent) time
     */
    private long modPow(long base, long exponent, long modulus) {
        long result = 1L;
        base = base % modulus;
        
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }
        
        return result;
    }

    @Override
    public Long getPart2Result(String fileName) {
        // Day 25 typically only has part 1 in Advent of Code
        return null;
    }
}