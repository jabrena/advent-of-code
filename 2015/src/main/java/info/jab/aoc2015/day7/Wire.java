package info.jab.aoc2015.day7;

public class Wire {
    private final String name;
    private Integer signal;

    public Wire(String name) {
        this.name = name;
        this.signal = null;
    }

    public boolean hasSignal() {
        return signal != null;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal & 0xFFFF; // Ensure 16-bit
    }

    public String getName() {
        return name;
    }
}
