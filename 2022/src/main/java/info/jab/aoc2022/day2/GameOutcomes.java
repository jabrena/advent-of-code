package info.jab.aoc2022.day2;

enum GameOutcomes {
    LOST(0),
    DRAW(3),
    WIN(6);

    private int value;

    GameOutcomes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
