package info.jab.aoc2016.day10;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bot in the factory simulation.
 * Bots hold chips and can process them when they have two chips.
 */
public final class Bot {
    private final int id;
    private final List<Integer> chips = new ArrayList<>();

    public Bot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addChip(int value) {
        chips.add(value);
    }

    public boolean hasTwo() {
        return chips.size() == 2;
    }

    public boolean isComparing(int value1, int value2) {
        if (chips.size() != 2) {
            return false;
        }
        return (chips.contains(value1) && chips.contains(value2));
    }

    public int getLowChip() {
        return Math.min(chips.get(0), chips.get(1));
    }

    public int getHighChip() {
        return Math.max(chips.get(0), chips.get(1));
    }

    public void clearChips() {
        chips.clear();
    }
}

