package info.jab.aoc.day1;

import java.util.List;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class FrequencyDevice implements Solver<Integer> {

    private int frequency = 0;
    
    public int getFrequency() {
        return frequency;
    }
    
    public void reset() {
        frequency = 0;
    }
    
    public void applyChange(String change) {
        if (change == null || change.isEmpty())
            throw new IllegalArgumentException("Change cannot be null or empty");
            
        char sign = change.charAt(0);
        int value = Integer.parseInt(change.substring(1));
        
        if (sign == '+')
            frequency += value;
        else if (sign == '-')
            frequency -= value;
        else
            throw new IllegalArgumentException("Invalid change format: " + change);
    }
    
    public void applyChanges(List<String> changes) {
        changes.forEach(this::applyChange);
    }

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> input = ResourceLines.list(fileName);
        
        FrequencyDevice device = new FrequencyDevice();
        device.applyChanges(input);
        
        return device.getFrequency();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        throw new UnsupportedOperationException();
    }
}
