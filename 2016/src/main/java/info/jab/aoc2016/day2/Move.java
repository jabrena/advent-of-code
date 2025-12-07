package info.jab.aoc2016.day2;

/**
 * Represents a movement direction on the keypad.
 */
public enum Move {
    U, D, L, R;

    public static Move fromChar(char direction) {
        return Move.valueOf(String.valueOf(direction));
    }
}

