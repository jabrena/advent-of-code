package info.jab.aoc2022.day13;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Day13 implements Day<Integer> {

    private static List<Object> buildList(String line) {
        List<Object> list = new ArrayList<>();
        Deque<List<Object>> stack = new ArrayDeque<>();
        char[] charArray = line.toCharArray();
        int i = 1;
        while (i < charArray.length - 1) {
            char c = charArray[i];
            if (c == ',') {
                i++;
                continue;
            }
            if (c == '[') {
                stack.addLast(new ArrayList<>());
                i++;
                continue;
            }
            if (c == ']') {
                List<Object> stackTop = stack.removeLast();
                if (!stack.isEmpty()) {
                    stack.getLast().add(stackTop);
                } else {
                    list.add(stackTop);
                }
                i++;
                continue;
            }
            StringBuilder numStr = new StringBuilder();
            while (i < charArray.length - 1 && Character.isDigit(charArray[i])) {
                numStr.append(charArray[i]);
                i++;
            }
            Integer num = Integer.parseInt(numStr.toString());
            if (!stack.isEmpty()) {
                stack.getLast().add(num);
            } else {
                list.add(num);
            }
        }
        return list;
    }

    private boolean comparePackets(Queue<List<Object>> queue) {
        if (queue.isEmpty()) {
            return false;
        }
        List<Object> left = queue.poll();
        List<Object> right = queue.poll();
        return compare(left, right) != -1;
    }

    private int compare(Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) {
            return Integer.compare((Integer) left, (Integer) right) * -1;
        }
        if (left instanceof Integer) {
            return compare(List.of(left), right);
        }
        if (right instanceof Integer) {
            return compare(left, List.of(right));
        }

        // both are lists
        List<Integer> leftList = (List<Integer>) left;
        List<Integer> rightList = (List<Integer>) right;
        for (int i = 0; i < leftList.size(); i++) {
            if (i >= rightList.size()) {
                // Ran out of objects on the right
                return -1;
            }
            int res = compare(leftList.get(i), rightList.get(i));
            if (res != 0) {
                return res;
            }
        }

        //Magic
        return compare(leftList.size(), rightList.size());
    }

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> input = ResourceLines.list("/" + fileName);
        Queue<List<Object>> queue = new ArrayDeque<>();
        int total = 0;
        int pairIndex = 1;
        for (String line : input) {
            if (line.isEmpty()) {
                if (comparePackets(queue)) {
                    total += pairIndex;
                }
                pairIndex++;
                continue;
            }
            queue.add(buildList(line));
        }
        if (comparePackets(queue)) {
            total += pairIndex;
        }
        return total;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        List<String> input = ResourceLines.list("/" + fileName);
        List<List<Object>> list = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                continue;
            }
            list.add(buildList(line));
        }
        list.add(List.of(List.of(2)));
        list.add(List.of(List.of(6)));
        list.sort((a, b) -> compare(b, a));
        int res = 1;
        for (int j = 0, listSize = list.size(); j < listSize; j++) {
            List<Object> i = list.get(j);
            if (i.equals(List.of(List.of(6))) || i.equals(List.of(List.of(2)))) {
                res *= j + 1;
            }
        }
        return res;
    }
}
