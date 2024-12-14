package info.jab.aoc.day2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Gatherers;

//https://docs.oracle.com/en/java/javase/22/core/stream-gatherers.html
//Not working as expected
public class ReportAnalyzer2 {

    enum Order { ASCENDING, DESCENDING, INVALID }

    public static Integer solvePart1(List<String> list) {
        return (int) list.stream()
            .filter(l -> Arrays.stream(l.split(" "))
            .map(Integer::parseInt)
            .gather(Gatherers.windowSliding(2))
            .map(pair -> {
                return switch (pair.getFirst() - pair.getLast()) {
                    case -3, -2, -1 -> Order.DESCENDING;
                    case 1, 2, 3 -> Order.DESCENDING;
                    default -> Order.INVALID;
                };
            })
            .reduce((o, o2) -> o != o2 ? Order.INVALID : o)
            .map(o -> o != Order.INVALID).orElse(true))
            .count();
    }

    public static Integer solvePart12(List<String> list) {
        return (int) list.stream()
            .filter(l -> {
                // Split the line into integers
                int[] levels = Arrays.stream(l.split(" "))
                                     .mapToInt(Integer::parseInt)
                                     .toArray();

                // Check if the sequence is strictly increasing or decreasing
                boolean isValid = true;
                Order currentOrder = null;

                for (int i = 0; i < levels.length - 1; i++) {
                    int diff = levels[i + 1] - levels[i];
                    if (diff >= 1 && diff <= 3) {
                        if (currentOrder == null) {
                            currentOrder = Order.ASCENDING;
                        } else if (currentOrder == Order.DESCENDING) {
                            isValid = false;
                            break;
                        }
                    } else if (diff <= -1 && diff >= -3) {
                        if (currentOrder == null) {
                            currentOrder = Order.DESCENDING;
                        } else if (currentOrder == Order.ASCENDING) {
                            isValid = false;
                            break;
                        }
                    } else {
                        isValid = false;
                        break;
                    }
                }

                return isValid && currentOrder != null;
            })
            .count();
    }

}
