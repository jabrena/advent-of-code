package info.jab.aoc2022.day6;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Solution for AOC 2022, Day 6
 * https://adventofcode.com/2022/day/6
 *
 */
public class Day6 implements Day<List<Integer>> {

    private static final Pattern STRING_SPLIT_PATTERN = Pattern.compile("(?!^)");

    private static Set<String> getUniqueCharactersAsHashSet(String string) {
        return Arrays.stream(STRING_SPLIT_PATTERN.split(string))
                .collect(Collectors.toUnmodifiableSet());
    }

    // @formatter:off
    private Predicate<String> areUniqueCharacters = param ->
            getUniqueCharactersAsHashSet(param).size() == param.length();

    // @formatter:on

    private BiFunction<String, Integer, Integer> detectMarker = (line, markerLength) -> {
        Integer result = 0;

        //Sometimes is not possible to use Streams ¯\_(ツ)_/¯
        for (int i = 0; i < line.length(); i++) {
            String chunkToEvaluate = line.substring(i, i + markerLength);
            result = i + markerLength;
            if (areUniqueCharacters.test(chunkToEvaluate)) {
                break;
            }
        }
        return result;
    };

    // @formatter:off
    @Override
    public List<Integer> getPart1Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
                .map(line -> detectMarker.apply(line, 4))
                .toList();
    }

    @Override
    public List<Integer> getPart2Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
                .map(str -> detectMarker.apply(str, 14))
                .toList();
    }
    // @formatter:on
}
