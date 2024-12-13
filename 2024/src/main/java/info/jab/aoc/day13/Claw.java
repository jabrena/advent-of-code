package info.jab.aoc.day13;

class Claw{
    Button a;
    Button b;
    Prize p;

    Claw(String in) {
        String[] lines = in.split("\n");
        a = Button.of(lines[0]);
        b = Button.of(lines[1]);
        p = Prize.of(lines[2]);
    }

    void update() {
        p = new Prize(p.x() + 10000000000000L, p.y() + 10000000000000L);
    }

    long solve() {
        double j = (double) (p.y() * a.x() - a.y() * p.x()) / (b.y() * a.x() - a.y() * b.x());
        double i = (p.x() - b.x() * j) / a.x();
        if(j - (long) j == 0 && i - (long) i == 0) {
            return (long) (i * 3 + j);
        }
        return 0;
    }
}
