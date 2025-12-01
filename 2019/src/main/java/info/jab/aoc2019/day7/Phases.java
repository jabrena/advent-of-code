package info.jab.aoc2019.day7;

enum Phases {
    A(3),
    B(1),
    C(2),
    D(4),
    E(0);

    private final int phase;

    Phases(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }
}

