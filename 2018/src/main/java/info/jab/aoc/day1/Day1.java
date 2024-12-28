package info.jab.aoc.day1;

import info.jab.aoc.Day;

import java.util.List;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2018/day/1
 **/
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> input = ResourceLines.list(fileName);
        
        FrequencyDevice device = new FrequencyDevice();
        device.applyChanges(input);
        
        //System.out.println("Frecuencia resultante: " + device.getFrequency());
        return device.getFrequency();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}
