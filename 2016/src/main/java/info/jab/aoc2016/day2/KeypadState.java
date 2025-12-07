package info.jab.aoc2016.day2;

/**
 * Represents the state during keypad navigation.
 */
public record KeypadState(KeypadDirection current, String result) {
    public KeypadState append(char character) {
        return new KeypadState(current, result + character);
    }
    
    public KeypadState withCurrent(KeypadDirection newCurrent) {
        return new KeypadState(newCurrent, result);
    }
}

