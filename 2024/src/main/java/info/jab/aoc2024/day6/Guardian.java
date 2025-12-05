package info.jab.aoc2024.day6;

import com.putoet.grid.Grid;
import com.putoet.grid.Point;

public class Guardian {

    private Direction direction;
    private Point point;
    private Grid map;

    private Boolean exitState = false;

    public Guardian(Direction direction, Point point, Grid map) {
        this.direction = direction;
        this.point = point;
        this.map = map;
    }

    public boolean isOut() {
        return exitState;
    }

    char obstacleSymbol = '#';
    char walked = 'X';

    Integer iterations = 0;

    public void walk() {
        try {
            if(direction == Direction.NORTH) {
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.NORTH);
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.RIGHT;
                        point = point.sub(Point.SOUTH); //Correction
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.RIGHT) {
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.add(Point.EAST);
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.SOUTH;
                        point = point.add(Point.WEST); //Correction
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.SOUTH) {
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.SOUTH);
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.LEFT;
                        point = point.sub(Point.NORTH); //Correction
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.LEFT) {
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.EAST);
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.NORTH;
                        point = point.sub(Point.WEST); //Correction
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            }

            iterations ++;

        } catch (AssertionError ex) {
            exitState = true;
        }
    }

    public Integer getSteps() {
        int steps = 0;
        for (int i = 0; i < map.grid().length; i++) { // Iterate through rows
            for (int j = 0; j < map.grid()[i].length; j++) { // Iterate through columns
                if (map.grid()[i][j] == walked) {
                    steps++;
                }
            }
        }

        return steps;
    }
}
