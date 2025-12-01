package info.jab.aoc2022.day2;

enum ShapeScores {
    ONE(1),
    TWO(2),
    THREE(3);

    private int value;

    ShapeScores(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
