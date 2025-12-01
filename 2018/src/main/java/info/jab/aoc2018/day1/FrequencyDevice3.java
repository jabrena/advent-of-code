package info.jab.aoc2018.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class FrequencyDevice3 implements Solver<Integer> {
    
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
    
    private record State(Set<Integer> frequencies, int currentFrequency) {
        public State next(int change) {
            int newFrequency = currentFrequency + change;
            return new State(
                new HashSet<>(frequencies) {{ add(newFrequency); }},
                newFrequency
            );
        }
        
        public boolean isDuplicate(int newFrequency) {
            return frequencies.contains(newFrequency);
        }
    }

    //TODO Improve the functional approach without mutability
    private int findFirstDuplicate(List<String> changes) {
        Set<Integer> frequencies = new HashSet<>();
        int[] currentFrequency = {0};  // Usando array para permitir modificación en lambda
        frequencies.add(currentFrequency[0]);
        
        List<Tuple> tuples = changes.stream()
                                  .map(Tuple::from)
                                  .toList();
                                  
        return Stream.generate(() -> tuples)
                    .flatMap(List::stream)
                    .map(this::calculateChange)
                    .map(change -> {
                        currentFrequency[0] += change;
                        return currentFrequency[0];
                    })
                    .filter(freq -> !frequencies.add(freq))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No duplicate frequency found"));
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list(fileName);
        return findFirstDuplicate(input);
    }
}
