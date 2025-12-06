package info.jab.aoc2023.day3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import info.jab.aoc.Day;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

/**
 * Solution for AOC 2023, Day 3
 * https://adventofcode.com/2023/day/3
 *
 */
class Day3  implements Day<Long> {

    Function<List<String>, Set<String>> findDifferentSymbols = param -> {
        var nonSymbols = List.of("0","1","2","3","4","5","6","7","8","9",".");
        Set<String> symbols = new HashSet<>();

        param.stream()
            .forEach(list -> {
                var symbolsInLine = list.chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .filter(ch -> nonSymbols.stream().noneMatch(s -> s.equals(ch)))
                    .distinct()
                    .collect(Collectors.toSet());

                symbols.addAll(symbolsInLine);
            });
        return symbols;
    };

    record MatrixDimension(int x, int y) {}

    Function<List<String>, MatrixDimension> getMatrixDimension = param -> {
        var x = param.get(0).length();
        var y = param.size();
        return new MatrixDimension(x,y);
    };

    Function<List<String>, Grid> createMatrix = param -> new Grid(GridUtils.of(param));

    record Symbol(String symbol, int x, int y) {
        int[][] getAdjacents() {
            return new int[][] {
                    {x - 1, y - 1},
                    {x, y - 1},
                    {x + 1, y - 1},
                    {x - 1, y},
                    {x + 1, y},
                    {x - 1, y + 1},
                    {x, y + 1},
                    {x + 1, y + 1}
            };
        }
    }

    //TODO Improve this part
    private int findNumber(Set<String> symbols, Grid grid, int x, int y) {
        // go all the way left until we hit a ., x=minX, or a symbol knowing we are past the left end of the number
        while (x >= grid.minX() && grid.get(x, y) != '.' && !symbols.contains(String.valueOf(grid.get(x, y)))) {
            x--;
        }

        // go back to the first digit of the number
        x++;

        StringBuilder number = new StringBuilder();

        // start moving right to read the entire number, stopping at a . or if we hit the right end of the grid
        while (x < grid.maxX() && grid.get(x, y) != '.' && !symbols.contains(String.valueOf(grid.get(x, y)))) {
            number.append(grid.get(x, y));
            x++;
        }

        return Integer.parseInt(number.toString());
    }

    private Set<Integer> findNumbersAdjacentTo(Set<String> symbols, Grid grid, Symbol symbol) {
        int[][] directions = symbol.getAdjacents();

        Set<Integer> numbers = new HashSet<>();

        for (int[] direction : directions) {
            int x = direction[0];
            int y = direction[1];

            if (grid.contains(x, y)) {
                char atPosition = grid.get(x, y);
                if (Character.isDigit(atPosition)) {
                    numbers.add(findNumber(symbols, grid, x, y));
                }
            }
        }

        return numbers;
    }

    private List<Symbol> getSymbols(Set<String> symbols, Grid grid) {

        List<Symbol> list = new ArrayList<>();

        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                String element = String.valueOf(grid.get(x, y));
                if(symbols.contains(element)) {
                    list.add(new Symbol(element, x, y));
                }
            }
        }

        return list;
    }

    @Override
    public Long getPart1Result(String fileName) {

        var lines = ResourceLines.list("/" + fileName);
        Grid matrix = createMatrix.apply(lines);

        Set<String> symbols = findDifferentSymbols.apply(lines);
        System.out.println("Combined Set: " + symbols);

        var symbols2 = getSymbols(symbols, matrix);

        var result = String.valueOf(symbols2.stream()
                .flatMap(sym -> findNumbersAdjacentTo(symbols, matrix, sym).stream())
                .reduce(0, Integer::sum));

        return Long.valueOf(result);
    }

    // @formatter:off

    @Override
    public Long getPart2Result(String fileName) {

        var lines = ResourceLines.list("/" + fileName);
        Set<String> symbols = findDifferentSymbols.apply(lines);
        System.out.println("Combined Set: " + symbols);

        Grid matrix = createMatrix.apply(lines);
        var symbols2 = getSymbols(symbols, matrix);

        List<Symbol> gears = symbols2.stream()
            .filter(symbol -> symbol.symbol.equals("*"))
            .toList();

        var result = String.valueOf(gears.stream()
                .map(symbol -> findNumbersAdjacentTo(symbols, matrix, symbol))
                .filter(set -> set.size() == 2)
                .map(set -> {
                    List<Integer> nums = set.stream().toList();
                    return nums.get(0) * nums.get(1);
                })
                .reduce(0, Integer::sum));

        return Long.valueOf(result);
    }

    // @formatter:on

}
