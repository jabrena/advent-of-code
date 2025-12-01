package info.jab.aoc2019.day2;

import java.util.Arrays;
import java.util.List;

enum ParserStates {
    SUM(1),
    MULTIPLY(2),
    HALT(99),
    UNKNOWN(-1);

    private final int opcode;

    ParserStates(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public static ParserStates fromInt(int status) {
        return Arrays.stream(values())
                .filter(value -> value.opcode == status)
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static List<Integer> sum(List<Integer> positions, int start) {
        positions.set(positions.get(start + 3),
                positions.get(positions.get(start + 1)) + positions.get(positions.get(start + 2)));
        return positions;
    }

    public static List<Integer> multiply(List<Integer> positions, int start) {
        positions.set(positions.get(start + 3),
                positions.get(positions.get(start + 1)) * positions.get(positions.get(start + 2)));
        return positions;
    }
}

