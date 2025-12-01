package info.jab.aoc2024.day13;

record Prize(long x, long y) {
    public static Prize of(String in) {
        String[] parts = in.split("Prize: X=|, Y=");
        return new Prize(Long.parseLong(parts[1].trim()), Long.parseLong(parts[2].trim()));
    }
}
