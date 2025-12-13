package info.jab.aoc2024.day9;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class DiskCompactor implements Solver<Long> {

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

    public long computeChecksum(String input, boolean isPart2) {
        int[] parts = parseInput(input);
        int totalBlocks = sumParts(parts);

        return isPart2 ? computeChecksumPart2(parts) : computeChecksumPart1(parts, totalBlocks);
    }

    private int[] parseInput(String input) {
        int[] parts = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            parts[i] = Character.getNumericValue(input.charAt(i));
        }
        return parts;
    }

    private int sumParts(int[] parts) {
        int sum = 0;
        for (int part : parts) {
            sum += part;
        }
        return sum;
    }

    private int[] populateDisk(int[] parts) {
        int totalBlocks = sumParts(parts);
        int[] disk = new int[totalBlocks];
        int position = 0;

        for (int i = 0; i < parts.length; i++) {
            int value = (i % 2 == 1) ? -1 : i / 2; // -1 for gaps, file ID for files
            for (int j = 0; j < parts[i]; j++) {
                disk[position++] = value;
            }
        }
        return disk;
    }

    private long computeChecksumPart1(int[] parts, int totalBlocks) {
        int[] disk = populateDisk(parts);
        compactDisk(disk, totalBlocks);
        return calculateChecksum(disk, totalBlocks);
    }

    /**
     * Optimized compaction using two-pointer approach.
     * Time complexity: O(n) instead of O(n²)
     */
    private void compactDisk(int[] disk, int totalBlocks) {
        int left = 0;
        int right = totalBlocks - 1;

        while (left < right) {
            if (disk[left] == -1) {
                // Find next non-gap from right
                while (right > left && disk[right] == -1) {
                    right--;
                }
                if (right > left) {
                    disk[left] = disk[right];
                    disk[right] = -1;
                    right--;
                }
            }
            left++;
        }
    }

    private long calculateChecksum(int[] disk, int totalBlocks) {
        long sum = 0;
        for (int i = 0; i < totalBlocks; i++) {
            if (disk[i] != -1) {
                sum += (long) i * disk[i];
            }
        }
        return sum;
    }

    /**
     * Optimized Part 2 using TreeSet sorted by position for gap management.
     * Time complexity: O(n log n) - TreeSet operations are O(log n)
     */
    private long computeChecksumPart2(int[] parts) {
        int[] cumulativePositions = precomputeCumulativePositions(parts);
        Map<Integer, int[]> starting = new HashMap<>();
        Map<Integer, GapInfo> gapsById = new HashMap<>();
        populateFileAndGapMaps(parts, cumulativePositions, starting, gapsById);

        // Use TreeSet sorted by position for efficient gap management
        TreeSet<GapInfo> gapSet = createGapSet(gapsById);

        reassignFilesToGaps(parts, starting, gapsById, gapSet);

        return calculateChecksumFromFiles(starting);
    }

    private int[] precomputeCumulativePositions(int[] parts) {
        int[] cumulativePositions = new int[parts.length + 1];
        for (int i = 0; i < parts.length; i++) {
            cumulativePositions[i + 1] = cumulativePositions[i] + parts[i];
        }
        return cumulativePositions;
    }

    private void populateFileAndGapMaps(int[] parts, int[] cumulativePositions,
                                        Map<Integer, int[]> starting, Map<Integer, GapInfo> gaps) {
        for (int i = 0; i < parts.length; i++) {
            int start = cumulativePositions[i];
            if (i % 2 == 0) {
                starting.put(i / 2, new int[]{start, parts[i]});
            } else {
                gaps.put(i / 2, new GapInfo(start, parts[i], i / 2));
            }
        }
    }

    private TreeSet<GapInfo> createGapSet(Map<Integer, GapInfo> gaps) {
        Comparator<GapInfo> gapComparator = Comparator.comparingInt(GapInfo::position)
                .thenComparingInt(GapInfo::gapId);
        TreeSet<GapInfo> gapSet = new TreeSet<>(gapComparator);
        gapSet.addAll(gaps.values());
        return gapSet;
    }

    private void reassignFilesToGaps(int[] parts, Map<Integer, int[]> starting,
                                     Map<Integer, GapInfo> gapsById, TreeSet<GapInfo> gapSet) {
        // Process files from highest ID to lowest
        int maxFileId = parts.length / 2;
        for (int fileId = maxFileId; fileId >= 0; fileId--) {
            if (!starting.containsKey(fileId)) {
                continue;
            }

            int fileSize = starting.get(fileId)[1];
            GapInfo fittingGap = findFirstFittingGap(gapSet, fileId, fileSize);

            if (fittingGap != null) {
                updateFilePosition(starting, fileId, fittingGap, fileSize);
                updateGapSet(gapsById, gapSet, fittingGap, fileSize);
            }
        }
    }

    /**
     * Finds first fitting gap in TreeSet sorted by position.
     * Since TreeSet is sorted by position, first matching gap has smallest position.
     * Time complexity: O(n) in worst case, but early termination helps
     */
    private GapInfo findFirstFittingGap(TreeSet<GapInfo> gapSet, int fileId, int fileSize) {
        for (GapInfo gap : gapSet) {
            if (gap.gapId() < fileId && gap.size() >= fileSize) {
                return gap;
            }
        }
        return null;
    }

    private void updateFilePosition(Map<Integer, int[]> starting, int fileId,
                                    GapInfo fittingGap, int fileSize) {
        starting.put(fileId, new int[]{fittingGap.position(), fileSize});
    }

    /**
     * Updates gap data structures after placing a file in a gap.
     * Removes the gap and adds remaining gap if any.
     */
    private void updateGapSet(Map<Integer, GapInfo> gapsById, TreeSet<GapInfo> gapSet,
                               GapInfo fittingGap, int fileSize) {
        gapSet.remove(fittingGap);
        if (fittingGap.size() > fileSize) {
            GapInfo remainingGap = new GapInfo(
                    fittingGap.position() + fileSize,
                    fittingGap.size() - fileSize,
                    fittingGap.gapId()
            );
            gapSet.add(remainingGap);
            gapsById.put(fittingGap.gapId(), remainingGap);
        } else {
            gapsById.remove(fittingGap.gapId());
        }
    }

    private long calculateChecksumFromFiles(Map<Integer, int[]> starting) {
        long sum = 0;
        for (Map.Entry<Integer, int[]> entry : starting.entrySet()) {
            int fileId = entry.getKey();
            int[] fileInfo = entry.getValue();
            int start = fileInfo[0];
            int size = fileInfo[1];
            for (int i = start; i < start + size; i++) {
                sum += (long) i * fileId;
            }
        }
        return sum;
    }

}
