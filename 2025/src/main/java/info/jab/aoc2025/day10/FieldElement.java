package info.jab.aoc2025.day10;

public interface FieldElement<T> {
    T add(T other);
    T subtract(T other);
    T multiply(T other);
    T divide(T other);
    boolean isZero();
    boolean isOne();
}

