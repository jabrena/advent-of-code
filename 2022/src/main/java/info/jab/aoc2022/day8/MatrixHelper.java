package info.jab.aoc2022.day8;

import com.putoet.resources.ResourceLines;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

class MatrixHelper {

    private static final Pattern STRING_SPLIT_PATTERN = Pattern.compile("(?!^)");

    private static List<String> getCharactersAsList(String string) {
        return Arrays.stream(STRING_SPLIT_PATTERN.split(string)).toList();
    }

    public static Integer[][] getMatrix(String fileName) {
        // @formatter:off
        //Initialize Matrix
        var list = ResourceLines.list("/" + fileName);
        Integer noOfRows = list.size();
        Integer noOfColumns = list.stream()
                .limit(1)
                .mapToInt(String::length)
                .findAny()
                .orElseThrow();
        Integer[][] matrix = new Integer[noOfRows][noOfColumns];

        //Populate Matrix
        AtomicInteger x = new AtomicInteger(0);
        AtomicInteger y = new AtomicInteger(0);
        ResourceLines.list("/" + fileName).stream()
            .map(MatrixHelper::getCharactersAsList)
            .forEach(row -> {
                row.stream()
                   .forEach(column -> {
                        matrix[y.get()][x.get()] = Integer.valueOf(column);
                        x.incrementAndGet();
                    });
                x.set(0);
                y.incrementAndGet();
            });

        return matrix;
        // @formatter:on
    }
}
