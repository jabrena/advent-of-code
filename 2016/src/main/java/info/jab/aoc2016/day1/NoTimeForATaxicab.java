package info.jab.aoc2016.day1;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashSet;
import java.util.Set;

/**
 * Solver for Day 1: No Time for a Taxicab
 * Calculates distances based on navigation instructions.
 */
public final class NoTimeForATaxicab implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateDistance(lines.get(0));
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateFirstRevisitedDistance(lines.get(0));
    }

    public Integer calculateDistance(final String instructionLine) {
        var instructions = instructionLine.split(", ");
        
        Position position = new Position(0, 0);
        Direction direction = Direction.NORTH;
        
        for (String instruction : instructions) {
            char turn = instruction.charAt(0);
            int steps = Integer.parseInt(instruction.substring(1));
            
            direction = (turn == 'L') ? direction.turnLeft() : direction.turnRight();
            position = position.move(direction, steps);
        }
        
        return position.manhattanDistance();
    }
    
    public Integer calculateFirstRevisitedDistance(final String instructionLine) {
        var instructions = instructionLine.split(", ");
        
        Position position = new Position(0, 0);
        Direction direction = Direction.NORTH;
        Set<Position> visited = new HashSet<>();
        visited.add(position);
        
        for (String instruction : instructions) {
            char turn = instruction.charAt(0);
            int steps = Integer.parseInt(instruction.substring(1));
            
            direction = (turn == 'L') ? direction.turnLeft() : direction.turnRight();
            
            // Move step by step to check for first revisited location
            for (int i = 0; i < steps; i++) {
                position = position.move(direction, 1);
                if (!visited.add(position)) {
                    return position.manhattanDistance();
                }
            }
        }
        
        throw new IllegalStateException("No location visited twice");
    }
}

