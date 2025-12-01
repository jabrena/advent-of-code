package info.jab.aoc2024.day13;

record Button(int x, int y) {
    public static Button of(String in) {
        String[] parts = in.split("Button [AB]: X\\+|, Y\\+");
        return new Button(Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()));
    }
}
