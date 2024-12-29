package info.jab.aoc.day1;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class Lisp implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);

        return input.chars()
            .filter(ch -> ch == '(' || ch == ')')
            .map(ch -> ch == '(' ? 1 : -1)
            .sum();    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);

        int floor = 0;
		int step = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '(')
				floor++;
			else if (input.charAt(i) == ')')
				floor--;
			if (floor == -1) {
				step = i + 1;
				break;
			}
		}
		return step;    }

}
