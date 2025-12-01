package info.jab.aoc2024.day5;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class PrintQueue2 implements Solver<Integer> {

    private Map<Integer, Set<Integer>> getRules(List<String> lines) {
        return lines.stream()
                .filter(line -> line.contains("|"))
                .map(line -> line.split("\\|", 2))
                .collect(groupingBy(
                        pair -> Integer.parseInt(pair[0]),
                        mapping(pair -> Integer.parseInt(pair[1]), toSet())
                ));
    }

    record Update(List<Integer> pageNumbers, boolean isValid) {
        public Integer getMiddlePageNumber() {
            return pageNumbers.get(pageNumbers.size() / 2);
        }
    }

    private List<Update> getUpdates(List<String> lines, Map<Integer, Set<Integer>> rules) {
        return lines.stream()
                .filter(line -> !line.isEmpty() && !line.contains("|"))
                .map(line -> {
                    var pageNumbers = Arrays.stream(line.split(","))
                            .map(Integer::parseInt)
                            .toList();

                    var isValid = pageNumbers.stream()
                            .limit(pageNumbers.size() - 1)
                            .allMatch(page -> {
                                var acceptedFollowers = rules.get(page);
                                var nextPage = pageNumbers.get(pageNumbers.indexOf(page) + 1);
                                return acceptedFollowers != null && acceptedFollowers.contains(nextPage);
                            });

                    return new Update(pageNumbers, isValid);
                })
                .toList();
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        var rules = getRules(lines);
        return getUpdates(lines, rules).stream()
                .filter(Update::isValid)
                .mapToInt(Update::getMiddlePageNumber)
                .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        var rules = getRules(lines);
        return getUpdates(lines, rules).stream()
                .filter(update -> !update.isValid())
                .mapToInt(update -> {
                    var pageNumbers = new ArrayList<>(update.pageNumbers());
                    pageNumbers.sort((o1, o2) -> rules.getOrDefault(o1, new HashSet<>()).contains(o2) ? -1 : Integer.MAX_VALUE);
                    return pageNumbers.get(pageNumbers.size() / 2);
                })
                .sum();
    }
}
