package jab.aoc.day9;

record Movement(Direction direction, Integer number) {
    public static Movement fromString(String str) {
        String[] parts = str.split(" ");
        return new Movement(Direction.fromValue(parts[0]), Integer.parseInt(parts[1]));
    }
}
