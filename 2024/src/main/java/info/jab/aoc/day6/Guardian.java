package info.jab.aoc.day6;

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
        //System.out.println(iterations);
        //System.out.println(direction);
        //System.out.println(point);

        try {
            //Walk north
            if(direction == Direction.NORTH) {
                //System.out.println("Walking to north");
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.NORTH);
                    //System.out.println(point);
                    //System.out.println(map.get(point.x(), point.y()));
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.RIGHT;
                        point = point.sub(Point.SOUTH); //Correction
                        //System.out.println(map);
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.RIGHT) {
                //System.out.println("Walking to right");
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.add(Point.EAST);
                    //System.out.println(point);
                    //System.out.println(map.get(point.x(), point.y()));
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.SOUTH;
                        point = point.add(Point.WEST); //Correction
                        //System.out.println(map);
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.SOUTH) {
                //System.out.println("Walking to south");
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.SOUTH);
                    //System.out.println(point);
                    //System.out.println(map.get(point.x(), point.y()));
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.LEFT;
                        point = point.sub(Point.NORTH); //Correction
                        //System.out.println(map);
                        break;
                    } else {
                        map.set(point.x(), point.y(), walked);
                    }
                }
            } else if (direction == Direction.LEFT) {
                //System.out.println("Walking to left");
                while(map.get(point.x(), point.y()) != obstacleSymbol) {
                    map.set(point.x(), point.y(), walked);
                    point = point.sub(Point.EAST);
                    //System.out.println(point);
                    //System.out.println(map.get(point.x(), point.y()));
                    if (map.get(point.x(), point.y()) == obstacleSymbol) {
                        direction = Direction.NORTH;
                        point = point.sub(Point.WEST); //Correction
                        //System.out.println(map);
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
        //System.out.println(map);
        //System.out.println(point);

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
