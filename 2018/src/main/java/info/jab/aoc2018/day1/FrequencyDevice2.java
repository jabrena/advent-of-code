package info.jab.aoc2018.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class FrequencyDevice2 implements Solver<Integer> {
    
    private enum Operation {
        PLUS('+'),
        MINUS('-');
        
        Operation(char symbol) {}
        
        private static Operation from(char symbol) {
            return switch (symbol) {
                case '+' -> PLUS;
                case '-' -> MINUS;
                default -> throw new IllegalArgumentException("Invalid operation symbol: " + symbol);
            };
        }
    }

    private record Tuple(Operation operation, int value) {
        public static Tuple from(String change) {
            if (change == null || change.isEmpty())
                throw new IllegalArgumentException("Change cannot be null or empty");
                
            Operation operation = Operation.from(change.charAt(0));
            int value = Integer.parseInt(change.substring(1));
            
            return new Tuple(operation, value);
        }
    }

    private int calculateChange(Tuple tuple) {
        return tuple.operation() == Operation.PLUS ? tuple.value() : -tuple.value();
    }

    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.list(fileName);
        return input.stream()
                   .map(Tuple::from)
                   .map(this::calculateChange)
                   .reduce(0, Integer::sum);
    }
    
    private int findFirstDuplicate(List<String> changes) {
        List<Tuple> tuples = changes.stream().map(Tuple::from).toList();
         
        Set<Integer> frequencies = new HashSet<>();
        int currentFrequency = 0;
        frequencies.add(currentFrequency);
        while (true) { 
            for (Tuple tuple : tuples) {
                currentFrequency += calculateChange(tuple);;
                if (!frequencies.add(currentFrequency)) {
                    return currentFrequency;
                }
            }
        }
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list(fileName);
        return findFirstDuplicate(input);
    }
}
