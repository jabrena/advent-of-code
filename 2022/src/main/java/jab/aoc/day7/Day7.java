package jab.aoc.day7;

import jab.aoc.Day;
import jab.aoc.Utils;

public class Day7 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        // @formatter:off
        var fileSystem = Utils.readFileToListF
                .andThen(FileSystemRecreation.from)
                .apply(fileName);

        return fileSystem.values().stream()
                .filter(value -> value <= 100_000)
                .mapToLong(value -> value)
                .sum();
        // @formatter:on
    }

    @Override
    public Long getPart2Result(String fileName) {
        // @formatter:off
        var fileSystemRecreation = Utils.readFileToListF
                .andThen(FileSystemRecreation.from)
                .apply(fileName);

        final Long totalSize = 70_000_000L;
        final Long treeSpaceNeeded = 30_000_000L;
        long minToFree = fileSystemRecreation.getOrDefault("/", 0L) - (totalSize - treeSpaceNeeded);

        return fileSystemRecreation.values().stream()
                .filter(x -> x >= minToFree)
                .mapToLong(x -> x)
                .min()
                .orElseThrow();
        // @formatter:on
    }
}
