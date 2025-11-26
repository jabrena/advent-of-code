package info.jab.aoc.day1;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

import java.util.HashSet;
import java.util.Set;

/**
 * https://adventofcode.com/2016/day/1
 **/
public class Day1 implements Day<Integer> {

    private enum Direction {
        NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);
        
        final int dx, dy;
        
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        
        Direction turnLeft() {
            return switch (this) {
                case NORTH -> WEST;
                case WEST -> SOUTH;
                case SOUTH -> EAST;
                case EAST -> NORTH;
            };
        }
        
        Direction turnRight() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }
    }
    
    private record Position(int x, int y) {
        int manhattanDistance() {
            return Math.abs(x) + Math.abs(y);
        }
        
        Position move(Direction direction, int steps) {
            return new Position(x + direction.dx * steps, y + direction.dy * steps);
        }
    }

    public Integer calculateDistance(String instructionLine) {
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
    
    public Integer calculateFirstRevisitedDistance(String instructionLine) {
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

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateDistance(lines.get(0));
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateFirstRevisitedDistance(lines.get(0));
    }
}
