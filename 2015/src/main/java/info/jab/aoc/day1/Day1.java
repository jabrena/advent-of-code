package info.jab.aoc.day1;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2015/day/1
 **/
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var parenthesis = ResourceLines.line(fileName);

        return parenthesis.chars()
            .filter(ch -> ch == '(' || ch == ')')
            .map(ch -> ch == '(' ? 1 : -1)
            .sum();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var parenthesis = ResourceLines.line(fileName);

        int floor = 0;
		int step = 0;
		for (int i = 0; i < parenthesis.length(); i++) {
			if (parenthesis.charAt(i) == '(')
				floor++;
			else if (parenthesis.charAt(i) == ')')
				floor--;
			if (floor == -1) {
				step = i + 1;
				break;
			}
		}
		return step;
    }
}
