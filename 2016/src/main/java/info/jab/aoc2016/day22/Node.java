package info.jab.aoc2016.day22;

/**
 * Represents a node in the grid computing system.
 */
public record Node(int x, int y, int size, int used, int avail) {
    boolean isEmpty() {
        return used == 0;
    }

    boolean canFit(int dataSize) {
        return avail >= dataSize;
    }
}

