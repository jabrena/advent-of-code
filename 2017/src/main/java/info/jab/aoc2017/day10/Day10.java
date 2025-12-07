package info.jab.aoc2017.day10;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;

public class Day10 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var input = ResourceLines.line(fileName);
        var lengths = parseLengths(input);
        var list = createList(256);
        knotHashRound(list, lengths, 0, 0);
        return list.get(0) * list.get(1);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private List<Integer> parseLengths(String input) {
        var lengths = new ArrayList<Integer>();
        for (var part : input.split(",")) {
            lengths.add(Integer.parseInt(part.trim()));
        }
        return lengths;
    }

    private List<Integer> createList(int size) {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    private void knotHashRound(List<Integer> list, List<Integer> lengths, int startPos, int startSkip) {
        int currentPos = startPos;
        int skipSize = startSkip;
        
        for (var length : lengths) {
            reverseSublist(list, currentPos, length);
            currentPos = (currentPos + length + skipSize) % list.size();
            skipSize++;
        }
    }

    private void reverseSublist(List<Integer> list, int start, int length) {
        for (int i = 0; i < length / 2; i++) {
            int idx1 = (start + i) % list.size();
            int idx2 = (start + length - 1 - i) % list.size();
            int temp = list.get(idx1);
            list.set(idx1, list.get(idx2));
            list.set(idx2, temp);
        }
    }
}
