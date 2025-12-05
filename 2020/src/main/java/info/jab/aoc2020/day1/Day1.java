package info.jab.aoc2020.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import org.paukov.combinatorics3.Generator;

/**
 * https://adventofcode.com/2020/day/1
 **/
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var numbers = ResourceLines.list(fileName, Integer::parseInt);

        return Generator.combination(numbers)
            .simple(2)
            .stream()
            .filter(list -> {
                var param1 = list.get(0);
                var param2 = list.get(1);
                if (param1 + param2 == 2020) {
                    System.out.println("Combination: " + param1 + " " + param2);
                    return true;
                }
                return false;
            })
            .map(list -> {
                var param1 = list.get(0);
                var param2 = list.get(1);
                return param1 * param2;
            })
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var numbers = ResourceLines.list(fileName, Integer::parseInt);

        return Generator.combination(numbers)
            .simple(3)
            .stream()
            .filter(list -> {
                var param1 = list.get(0);
                var param2 = list.get(1);
                var param3 = list.get(2);
                if (param1 + param2 + param3 == 2020) {
                    System.out.println("Combination: " + param1 + " " + param2 + " " + param3);
                    return true;
                }
                return false;
            })
            .map(list -> {
                var param1 = list.get(0);
                var param2 = list.get(1);
                var param3 = list.get(2);
                return param1 * param2 * param3;
            })
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Not found"));
    }
}
