package jab.aoc.day8;

import jab.aoc.Utils;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

class MatrixHelper {

    public static Integer[][] getMatrix(String fileName) {
        // @formatter:off
        //Initialize Matrix
        var list = Utils.readFileToList(fileName);
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
        Utils.readFileToList(fileName).stream()
            .map(Utils::getCharactersAsList)
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
