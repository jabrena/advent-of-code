package info.jab.aoc.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        //Load file
        var lines = ResourceLines.list(fileName);

        //Load 2 lists
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();
        lines.stream()
            .forEach(line -> {
                var parts = line.split("   ");
                list1.add(Integer.parseInt(parts[0]));
                list2.add(Integer.parseInt(parts[1]));
            });
        //Sort
        var list12 = list1.stream().sorted().toList();
        var list22 = list2.stream().sorted().toList();

        //Calculate distance
        //Sum
        AtomicInteger index = new AtomicInteger();
        return list12.stream()
            .mapToInt(element -> {
                int param1 = element;
                int param2 = list22.get(index.get());
                int distance = param1 - param2;
                index.addAndGet(+1);
                return Math.abs(distance);
            })
            .sum();
    }

    public static int countOccurrences(int target, List<Integer> list) {
        int count = 0;
        for (int number : list) {
            if (number == target) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        //Load file
        var lines = ResourceLines.list(fileName);

        //Load 2 lists
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();
        lines.stream()
            .forEach(line -> {
                var parts = line.split("   ");
                list1.add(Integer.parseInt(parts[0]));
                list2.add(Integer.parseInt(parts[1]));
            });
        //Sort
        var list12 = list1.stream().sorted().toList();
        var list22 = list2.stream().sorted().toList();

        //Calculate ocurrences
        //Sum
        AtomicInteger index = new AtomicInteger();
        return list12.stream()
            .mapToInt(element -> {
                int param1 = element;
                //int param2 = list22.get(index.get());
                int param2 = countOccurrences(param1, list22);
                int distance = param1 * param2;
                index.addAndGet(+1);
                return distance;
            })
            .sum();
    }

}
