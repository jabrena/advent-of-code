package info.jab.aoc.day4;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/4
 */
public class SoupLetter {

    private static final String XMAS_REGEX = "(XMAS|SAMX)";
    private static final Pattern XMAS_PATTERN = Pattern.compile(XMAS_REGEX);
    private static final String X_MAS_REGEX = "(MAS|SAM)";
    private static final Pattern X_MAS_PATTERN = Pattern.compile(X_MAS_REGEX);

    private final List<List<Character>> data;

    public SoupLetter(String fileName) {
        var list = ResourceLines.list(fileName);
        data = list.stream()
                .map(line -> line.chars().mapToObj(ch -> (char) ch).toList())
                .toList();
    }

    private int findWordInPart(String linePart) {
        return XMAS_PATTERN.matcher(linePart).find() ? 1 : 0;
    }

    private int getHorizontalCount() {
        return data.stream()
                .mapToInt(characters -> IntStream.range(0, characters.size() - 3)
                        .mapToObj(index -> "" + characters.get(index) + characters.get(index + 1) + characters.get(index + 2) + characters.get(index + 3))
                        .mapToInt(this::findWordInPart)
                        .sum())
                .sum();
    }

    private int getVerticalCount() {
        return IntStream.range(0, data.getFirst().size())
                .map(columnIndex -> IntStream.range(0, data.size() - 3)
                        .mapToObj(lineIndex -> ""
                                + data.get(lineIndex).get(columnIndex)
                                + data.get(lineIndex + 1).get(columnIndex)
                                + data.get(lineIndex + 2).get(columnIndex)
                                + data.get(lineIndex + 3).get(columnIndex))
                        .mapToInt(this::findWordInPart)
                        .sum())
                .sum();
    }

    private int getForwardDiagonalCount() {
        return IntStream.range(0, data.size() - 3)
                .map(lineIndex -> IntStream.range(3, data.getFirst().size())
                        .mapToObj(columnIndex -> ""
                                + data.get(lineIndex).get(columnIndex)
                                + data.get(lineIndex + 1).get(columnIndex - 1)
                                + data.get(lineIndex + 2).get(columnIndex - 2)
                                + data.get(lineIndex + 3).get(columnIndex - 3))
                        .mapToInt(this::findWordInPart)
                        .sum())
                .sum();
    }

    private int getBackwardDiagonalCount() {
        return IntStream.range(0, data.size() - 3)
                .map(lineIndex -> IntStream.range(0, data.getFirst().size() - 3)
                        .mapToObj(columnIndex -> ""
                                + data.get(lineIndex).get(columnIndex)
                                + data.get(lineIndex + 1).get(columnIndex + 1)
                                + data.get(lineIndex + 2).get(columnIndex + 2)
                                + data.get(lineIndex + 3).get(columnIndex + 3))
                        .mapToInt(this::findWordInPart)
                        .sum())
                .sum();
    }

    public int xmasCount() {
        return getHorizontalCount() + getVerticalCount() + getForwardDiagonalCount() + getBackwardDiagonalCount();
    }

    public int xDashMasCount() {
        return IntStream.range(1, data.size() - 1)
                .map(lineIndex -> IntStream.range(1, data.getFirst().size() - 1)
                        .map(columnIndex -> data.get(lineIndex).get(columnIndex) == 'A' && this.findWordInX(
                                "" + data.get(lineIndex - 1).get(columnIndex - 1) + 'A' + data.get(lineIndex + 1).get(columnIndex + 1),
                                "" + data.get(lineIndex - 1).get(columnIndex + 1) + 'A' + data.get(lineIndex + 1).get(columnIndex - 1))
                                ? 1 : 0)
                        .sum())
                .sum();
    }

    private boolean findWordInX(String word1, String word2) {
        return X_MAS_PATTERN.matcher(word1).find() && X_MAS_PATTERN.matcher(word2).find();
    }
}
