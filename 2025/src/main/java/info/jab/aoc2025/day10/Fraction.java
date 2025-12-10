package info.jab.aoc2025.day10;

public record Fraction(long numerator, long denominator) {
    static final Fraction ZERO = new Fraction(0, 1);
    static final Fraction ONE = new Fraction(1, 1);

    Fraction(long value) {
        this(value, 1);
    }

    public Fraction {
        if (denominator == 0) throw new IllegalArgumentException("Denominator cannot be zero");
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        long gcd = gcd(Math.abs(numerator), denominator);
        numerator /= gcd;
        denominator /= gcd;
    }

    Fraction subtract(Fraction other) {
        return new Fraction(numerator * other.denominator - other.numerator * denominator,
                denominator * other.denominator);
    }

    Fraction multiply(Fraction other) {
        return new Fraction(numerator * other.numerator, denominator * other.denominator);
    }

    Fraction divide(Fraction other) {
        return new Fraction(numerator * other.denominator, denominator * other.numerator);
    }
    
    boolean isZero() {
        return numerator == 0;
    }
    
    boolean isInteger() {
        return denominator == 1;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

