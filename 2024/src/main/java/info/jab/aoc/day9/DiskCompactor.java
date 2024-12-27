package info.jab.aoc.day9;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class DiskCompactor implements Solver<Long> {

    // Function to populate the disk with file IDs and free spaces (-1)
    Function<int[], int[]> populateDisk = parts ->
        IntStream.range(0, parts.length)
            .flatMap(i -> {
                int value = (i % 2 == 1) ? -1 : i / 2; // -1 for gaps, file ID for files
                int start = IntStream.range(0, i).map(idx -> parts[idx]).sum();
                return IntStream.range(start, start + parts[i]).map(j -> value);
            })
            .toArray();

    // Function to compact the disk
    BiFunction<int[], Integer, int[]> compactDisk = (disk, totalBlocks) -> {
        AtomicInteger backIndex = new AtomicInteger(totalBlocks - 1);
        return IntStream.range(0, totalBlocks)
            .map(i -> {
                if (disk[i] == -1) {
                    // Move backIndex until a valid block is found
                    while (backIndex.get() > i && disk[backIndex.get()] == -1) {
                        backIndex.decrementAndGet();
                    }
                    if (backIndex.get() > i) {
                        int value = disk[backIndex.get()];
                        disk[backIndex.getAndDecrement()] = -1; // Update backIndex after use
                        return value;
                    } else {
                        return -1;
                    }
                } else {
                    return disk[i];
                }
            })
            .toArray();
    };

    // Function to calculate the checksum
    BiFunction<int[], Integer, Long> calculateChecksum = (compactedDisk, totalBlocks) ->
        IntStream.range(0, totalBlocks)
            .filter(i -> compactedDisk[i] != -1)
            .mapToLong(i -> (long) i * compactedDisk[i])
            .sum();

    public long computeChecksum(String input, boolean isPart2) {
        int[] parts = input.chars().map(c -> Character.getNumericValue(c)).toArray();
        int totalBlocks = IntStream.of(parts).sum();

        if (!isPart2) {
            // Part 1: Compact disk and calculate checksum
            int[] disk = populateDisk.apply(parts);
            int[] compactedDisk = compactDisk.apply(disk, totalBlocks);
            return calculateChecksum.apply(compactedDisk, totalBlocks);
        } else {
            // Part 2: Gap and file reorganization with checksum calculation

            // Populate maps with file and gap information
            Map<Integer, int[]> starting = new HashMap<>();
            Map<Integer, int[]> gaps = new HashMap<>();
            IntStream.range(0, parts.length).forEach(i -> {
                int start = IntStream.range(0, i).map(idx -> parts[idx]).sum();
                if (i % 2 == 0) {
                    starting.put(i / 2, new int[]{start, parts[i]});
                } else {
                    gaps.put(i / 2, new int[]{start, parts[i]});
                }
            });

            // Reassign files to gaps
            IntStream.iterate(parts.length / 2, fileId -> fileId - 1)
                    .limit(parts.length / 2 + 1)
                    .filter(fileId -> starting.containsKey(fileId))
                    .forEach(fileId -> {
                        int fileSize = starting.get(fileId)[1];

                        IntStream.range(0, fileId)
                                .filter(gapId -> gaps.containsKey(gapId) && gaps.get(gapId)[1] >= fileSize)
                                .findFirst()
                                .ifPresent(gapId -> {
                                    starting.put(fileId, new int[]{gaps.get(gapId)[0], fileSize});
                                    gaps.put(gapId, new int[]{
                                            gaps.get(gapId)[0] + fileSize,
                                            gaps.get(gapId)[1] - fileSize
                                    });
                                });
                    });

            // Calculate checksum
            return starting.entrySet().stream()
                    .flatMapToLong(entry -> {
                        int fileId = entry.getKey();
                        int[] fileInfo = entry.getValue();
                        int start = fileInfo[0];
                        int size = fileInfo[1];
                        return IntStream.range(start, start + size).mapToLong(i -> (long) i * fileId);
                    })
                    .sum();
        }
    }

    @Override
    public Long solvePartOne(String fileName) {
        var line = ResourceLines.line(fileName);
        var diskCompactor = new DiskCompactor();
        return diskCompactor.computeChecksum(line, false);
    }

    @Override
    public Long solvePartTwo(String fileName) {
        var line = ResourceLines.line(fileName);
        var diskCompactor = new DiskCompactor();
        return diskCompactor.computeChecksum(line, true);
    }
}
