package info.jab.aoc2015.day1;

import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class Lisp implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);

        return input.chars()
            .mapToObj(ch -> Command.fromChar((char) ch))
            .mapToInt(Command::value)
            .sum();
	}


    @SuppressWarnings("null")
    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);

        record FloorPosition(int floor, int position) {}
        return IntStream.range(0, input.length()).boxed()
            .reduce(
                new FloorPosition(0, 0),
                (acc, i) -> {
                    int currentFloor = acc.floor() + Command.fromChar(input.charAt(i)).value();
                    return currentFloor == -1 && acc.position() == 0
                        ? new FloorPosition(currentFloor, i + 1)
                        : new FloorPosition(currentFloor, acc.position());
                },
                (a, b) -> a)
            .position();
	}
}
