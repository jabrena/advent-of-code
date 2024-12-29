package info.jab.aoc.day1;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class Lisp2 implements Solver<Integer> {

    private enum Commands {
        UP('(', 1),
        DOWN(')', -1);
        
        private final char symbol;
        private final int value;
        
        Commands(char symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }
        
        public static Commands fromChar(char symbol) {
            return valueOf(symbol);
        }
        
        private static Commands valueOf(char symbol) {
            return Arrays.stream(values())
                .filter(cmd -> cmd.symbol == symbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid symbol: " + symbol));
        }
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);

        return input.chars()
            .mapToObj(ch -> Commands.fromChar((char) ch))
			.map(direction -> direction.value)
            .reduce(0, Integer::sum);  
	}

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);

		record FloorPosition(int floor, int position) {}
		return IntStream.range(0, input.length()).boxed()
			.reduce(
				new FloorPosition(0, 0),
				(acc, i) -> {
					int currentFloor = acc.floor() + Commands.fromChar(input.charAt(i)).value;
					return currentFloor == -1 && acc.position() == 0
						? new FloorPosition(currentFloor, i + 1)
						: new FloorPosition(currentFloor, acc.position());
				},
				(a, b) -> a)
			.position();
	}
}
