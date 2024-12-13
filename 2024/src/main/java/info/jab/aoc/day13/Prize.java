package info.jab.aoc.day13;

record Prize(long x, long y) {
    public static Prize of(String in) {
         String[] parts = in.split("Prize: X=|, Y=");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Prize input: " + in);
        }
        return new Prize(Long.parseLong(parts[1].trim()), Long.parseLong(parts[2].trim()));
    }
}
