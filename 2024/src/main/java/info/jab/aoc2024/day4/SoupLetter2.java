package info.jab.aoc2024.day4;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class SoupLetter2 implements Solver<Integer> {

    private int wordSearch(final String word, final char[][] puzzle) {
        int wordCount = 0;

        for (int y = 0; y < puzzle.length; y++) {
            for (int x = 0; x < puzzle[y].length; x++) {
                if (puzzle[y][x] == word.charAt(0)) {
                    wordCount += countIf(isWordMatch(word, puzzle, x, y, -1, -1))
                            + countIf(isWordMatch(word, puzzle, x, y, -1, 0))
                            + countIf(isWordMatch(word, puzzle, x, y, -1, 1))
                            + countIf(isWordMatch(word, puzzle, x, y, 0, -1))
                            + countIf(isWordMatch(word, puzzle, x, y, 0, 1))
                            + countIf(isWordMatch(word, puzzle, x, y, 1, -1))
                            + countIf(isWordMatch(word, puzzle, x, y, 1, 0))
                            + countIf(isWordMatch(word, puzzle, x, y, 1, 1));
                }
            }
        }

        return wordCount;
    }

    private int wordSearchX(final String word, final char[][] puzzle) {
        final int middlePosition = word.length() / 2;
        final char middleLetter = word.charAt(middlePosition);

        int wordCount = 0;
        for (int y = 0; y < puzzle.length; y++) {
            for (int x = 0; x < puzzle[y].length; x++) {
                if (puzzle[y][x] == middleLetter) {
                    wordCount += countIf(
                            (isWordMatch(word, puzzle, x - middlePosition, y - middlePosition, 1, 1) ||
                             isWordMatch(word, puzzle, x + middlePosition, y + middlePosition, -1, -1)) &&
                            (isWordMatch(word, puzzle, x + middlePosition, y - middlePosition, -1, 1) ||
                             isWordMatch(word, puzzle, x - middlePosition , y + middlePosition, 1, -1))
                    );
                }
            }
        }
        return wordCount;
    }

    private boolean isWordMatch(
            final String word, final char[][] puzzle,
            final int x, final int y,
            final int xDir, final int yDir) {

        if (word.isEmpty()) {
            return true;
        }

        if (!locationInBound(puzzle, x, y) || word.charAt(0) != puzzle[y][x]) {
            return false;
        }

        return isWordMatch(word.substring(1), puzzle, x + xDir, y + yDir, xDir, yDir);
    }

    private int countIf(boolean count) {
        return count ? 1 : 0;
    }

    private boolean locationInBound(final char[][] puzzle, int x, int y) {
        return x >= 0 && x < puzzle[0].length && y >= 0 && y < puzzle.length;
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return wordSearch("XMAS", grid.grid());
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return wordSearchX("MAS", grid.grid());
    }
}
