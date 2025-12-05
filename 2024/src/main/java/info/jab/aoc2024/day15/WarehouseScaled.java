package info.jab.aoc2024.day15;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

//WIP, it solves the problem, but it is possible to improve the code. Tomorrow, it will be a new day.
public class WarehouseScaled {

    public record InputData(List<String> grid, char[] commands) {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            InputData inputData = (InputData) obj;
            return Objects.equals(grid, inputData.grid) &&
                   Arrays.equals(commands, inputData.commands);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(grid);
            result = 31 * result + Arrays.hashCode(commands);
            return result;
        }

        @Override
        public String toString() {
            return "InputData{" +
                   "grid=" + grid +
                   ", commands=" + Arrays.toString(commands) +
                   '}';
        }
    }

    public static InputData prepareInputData(List<String> inputLines) {
        List<String> grid = inputLines.stream()
            .takeWhile(line -> !line.isEmpty())
            .toList();

        String commands = inputLines.stream()
            .skip(grid.size() + 1)
            .collect(Collectors.joining());

        return new InputData(grid, commands.toCharArray());
    }

    //Convert original grid to another using some rules
    private Grid expandGrid(Grid grid) {
        // Determine the dimensions of the expanded grid
        int newWidth = grid.width() * 2;
        int newHeight = grid.height();
        char[][] newArray = new char[newHeight][newWidth];

        // Populate the expanded grid based on the original grid's content
        for (int y = grid.minY(); y < grid.maxY(); ++y) {
            for (int x = grid.minX(); x < grid.maxX(); ++x) {
                char current = grid.get(x, y);
                int newX = (x - grid.minX()) * 2; // Adjust x coordinate for expansion

                if (current == '#') {
                    newArray[y - grid.minY()][newX] = '#';
                    newArray[y - grid.minY()][newX + 1] = '#';
                } else if (current == '.') {
                    newArray[y - grid.minY()][newX] = '.';
                    newArray[y - grid.minY()][newX + 1] = '.';
                } else if (current == 'O') {
                    newArray[y - grid.minY()][newX] = '[';
                    newArray[y - grid.minY()][newX + 1] = ']';
                } else if (current == '@') {
                    newArray[y - grid.minY()][newX] = '@';
                    newArray[y - grid.minY()][newX + 1] = '.';
                }
            }
        }
        return new Grid(grid.minX(), grid.minX() + newWidth, grid.minY(), grid.maxY(), newArray);
    }

    final int[][] dirs = {{-1,0}, {0,1}, {1,0}, {0,-1}};
    //TODO Evolution
    final Point[] dirs2 = {Point.NORTH, Point.EAST, Point.SOUTH, Point.WEST};
    final char[] d = new char[]{'^','>','v','<'};

    private int getDirIndex(char m) {
        int dirIndex = 0;
        for (int i = 0; i < 4; ++i) {
            if (m == d[i]) {
                dirIndex = i;
                break;
            }
        }
        return dirIndex;
    }

    private record WalkState(Grid currentMap, Optional<Point> currentPoint) {}

    private Grid walk(Grid grid, char[] movements) {
        // Mutable map
        var map = grid.grid();
        var currentPoint = grid.findFirst(c -> c == '@').orElseThrow();

        //TODO Unify char[][] model using Grid
        currentPoint = new Point(currentPoint.y(), currentPoint.x());

        // Process all movements in a mutable map
        WalkState walkState = null;//
        for (char m : movements) {
            int dir = getDirIndex(m);
            walkState = mutateMap(new Grid(map), currentPoint, dir);
            Optional<Point> nextPoint = walkState.currentPoint();
            if (nextPoint.isPresent()) {
                currentPoint = nextPoint.get();
            }
        }
        return walkState.currentMap();
    }

    private WalkState mutateMap(Grid grid, Point currentPoint, int dir) {
        int nx = currentPoint.x() + dirs[dir][0];
        int ny = currentPoint.y() + dirs[dir][1];

        //Maintain this reference because mutate in moveHorizontal & moveVertical
        var map = grid.grid();
        if (inRange(ny, nx, grid) && grid.grid()[nx][ny] != '#') {
            if (dir % 2 == 1) {
                if (moveHorizontal(map, nx, ny, dir)) {
                    var tempGrid = updateMap(map, currentPoint, nx, ny);
                    return new WalkState(tempGrid, Optional.of(new Point(nx, ny)));
                }
            } else {
                if (moveVertical(map, nx, ny, dir)) {
                    var tempGrid = updateMap(map, currentPoint, nx, ny);
                    return new WalkState(tempGrid, Optional.of(new Point(nx, ny)));
                }
            }
        }
        return new WalkState(new Grid(map), Optional.empty()); // No valid movement
    }

    private Grid updateMap(char[][] map, Point currenPoint, int nx, int ny) {
        map[nx][ny] = '@';
        map[currenPoint.x()][currenPoint.y()] = '.';
        return new Grid(map);
    }

    public static boolean inRange(int x, int y, Grid grid) {
        return x >= grid.minX() && x < grid.maxX() && y >= grid.minY() && y < grid.maxY();
    }

    //TODO Refactor
    //Mutability
    //Methods doing so much work (Question & Map mutation & Points)

    //Simulates horizontal movement in the grid (map) along a row.
    //Scans in the given direction (dir) to find an open space ('.').
    //If no walls ('#') block the path, it shifts elements horizontally towards the open space and clears their original positions.
    //Returns true if movement is successful; otherwise, false.
    private boolean moveHorizontal(char[][] map, int x, int y, int dir) {
        int len = 1;
        while (true) {
            if (map[x][y] == '.') {
                while (len-- > 0) {
                    map[x][y] = map[x - dirs[dir][0]][y - dirs[dir][1]];
                    x -= dirs[dir][0];
                    y -= dirs[dir][1];
                }
                break;
            } else if (map[x][y] == '#') {
                return false;
            } else {
                x += dirs[dir][0];
                y += dirs[dir][1];
                ++len;
            }
        }
        return true;
    }

    //Handles vertical movement in the grid, considering single or multi-column elements (like '[' and ']').
    //Tracks movable positions across rows and validates movement by checking for open spaces in the next row.
    //Moves elements vertically while maintaining column relationships, clearing original positions.
    //Returns true if movement succeeds or false if blocked.
    private boolean moveVertical(char[][] map, int x, int y, int dir) {
        if (map[x][y] == '#') {
            return false;
        } else if (map[x][y] == '.') {
            return true;
        }
        Map<Integer, Set<Integer>> movablePositions = new HashMap<>();
        movablePositions.put(x, new HashSet<>());
        movablePositions.get(x).add(y);
        if (map[x][y] == '[') {
            movablePositions.get(x).add(y + 1);
        } else if (map[x][y] == ']') {
            movablePositions.get(x).add(y - 1);
        }
        int prevX = x;
        x += dirs[dir][0];
        while (true) {
            boolean freeRow = true;
            for (int i : movablePositions.get(prevX)) {
                if (map[x][i] == '.')
                    continue;

                if (map[x][i] == '#') {
                    return false;
                } else {
                    freeRow = false;
                    movablePositions.computeIfAbsent(x, k -> new HashSet<>());
                    if (map[x][i] == '[') {
                        movablePositions.get(x).add(i + 1);
                    } else if (map[x][i] == ']') {
                        movablePositions.get(x).add(i - 1);
                    }
                    movablePositions.get(x).add(i);
                    prevX = x;
                }
            }

            if (freeRow){
                break;
            } else {
                x += dirs[dir][0];
            }
        }

        while (movablePositions.size() > 0) {
            int prev = x - dirs[dir][0];
            for (int p : movablePositions.get(prev)) {
                map[x][p] = map[prev][p];
                map[prev][p] = '.';
            }
            movablePositions.remove(prev);
            x = prev;
        }
        return true;
    }

    private int countScore(Grid grid) {
        var map = grid.grid();
        return IntStream.range(0, map.length)
                .flatMap(i -> IntStream.range(0, map[i].length)
                        .filter(j -> map[i][j] == 'O' || map[i][j] == '[')
                        .map(j -> i * 100 + j))
                .sum();
    }

    public Integer solve1(String fileName) {
        var list = ResourceLines.list(fileName);
        var inputData = prepareInputData(list);
        Grid grid = new Grid(GridUtils.of(inputData.grid()));
        Grid grid2 = walk(grid, inputData.commands());
        return countScore(grid2);
    }

    public Integer solve2(String fileName) {
        var list = ResourceLines.list(fileName);
        var inputData = prepareInputData(list);
        Grid grid = new Grid(GridUtils.of(inputData.grid()));
        Grid grid2 = expandGrid(grid);
        Grid grid3 = walk(grid2, inputData.commands());
        return  countScore(grid3);
    }
}
