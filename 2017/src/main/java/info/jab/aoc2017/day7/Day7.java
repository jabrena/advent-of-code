package info.jab.aoc2017.day7;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day7 implements Day<String> {

    private static class Program {
        String name;
        int weight;
        List<String> children = new ArrayList<>();
    }

    @Override
    public String getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        Set<String> allPrograms = new HashSet<>();
        Set<String> children = new HashSet<>();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            
            String[] parts = line.split("->");
            String firstPart = parts[0].trim();
            
            // Parse program name and weight
            String programName = firstPart.split("\\s+")[0];
            allPrograms.add(programName);
            
            // If there are children, add them to the children set
            if (parts.length > 1) {
                String childrenPart = parts[1].trim();
                String[] childNames = childrenPart.split(",\\s*");
                for (String childName : childNames) {
                    children.add(childName.trim());
                }
            }
        }
        
        // The root is the program that is not a child of any other program
        for (String program : allPrograms) {
            if (!children.contains(program)) {
                return program;
            }
        }
        
        return "";
    }

    @Override
    public String getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        Map<String, Program> programs = new HashMap<>();
        Set<String> children = new HashSet<>();
        
        // Parse all programs
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            
            Program program = new Program();
            String[] parts = line.split("->");
            String firstPart = parts[0].trim();
            
            // Parse program name and weight
            String[] nameWeight = firstPart.split("\\s+");
            program.name = nameWeight[0];
            program.weight = Integer.parseInt(nameWeight[1].substring(1, nameWeight[1].length() - 1));
            
            // Parse children if any
            if (parts.length > 1) {
                String childrenPart = parts[1].trim();
                String[] childNames = childrenPart.split(",\\s*");
                for (String childName : childNames) {
                    program.children.add(childName.trim());
                    children.add(childName.trim());
                }
            }
            
            programs.put(program.name, program);
        }
        
        // Find root
        String rootName = null;
        for (String programName : programs.keySet()) {
            if (!children.contains(programName)) {
                rootName = programName;
                break;
            }
        }
        
        // Find the unbalanced node and calculate the correct weight
        int[] result = findUnbalanced(programs, rootName);
        return String.valueOf(result[1]);
    }
    
    private int[] findUnbalanced(Map<String, Program> programs, String nodeName) {
        Program node = programs.get(nodeName);
        int totalWeight = node.weight;
        
        if (node.children.isEmpty()) {
            return new int[]{totalWeight, 0}; // [total weight, correction needed (0 means no correction)]
        }
        
        List<Integer> childWeights = new ArrayList<>();
        List<String> childNames = new ArrayList<>();
        
        for (String childName : node.children) {
            int[] childResult = findUnbalanced(programs, childName);
            childWeights.add(childResult[0]);
            childNames.add(childName);
            totalWeight += childResult[0];
            
            // If a child found a correction, propagate it up
            if (childResult[1] != 0) {
                return new int[]{totalWeight, childResult[1]};
            }
        }
        
        // Check if children are balanced
        Map<Integer, Integer> weightCount = new HashMap<>();
        for (int weight : childWeights) {
            weightCount.put(weight, weightCount.getOrDefault(weight, 0) + 1);
        }
        
        if (weightCount.size() > 1) {
            // Find the odd one out
            int correctWeight = 0;
            int wrongWeight = 0;
            String wrongChild = null;
            
            for (Map.Entry<Integer, Integer> entry : weightCount.entrySet()) {
                if (entry.getValue() == 1) {
                    wrongWeight = entry.getKey();
                } else {
                    correctWeight = entry.getKey();
                }
            }
            
            // Find which child has the wrong weight
            for (int i = 0; i < childWeights.size(); i++) {
                if (childWeights.get(i) == wrongWeight) {
                    wrongChild = childNames.get(i);
                    break;
                }
            }
            
            // Calculate the difference and the correct weight for the wrong child
            int difference = wrongWeight - correctWeight;
            Program wrongProgram = programs.get(wrongChild);
            int correctProgramWeight = wrongProgram.weight - difference;
            
            return new int[]{totalWeight, correctProgramWeight};
        }
        
        return new int[]{totalWeight, 0};
    }
}
