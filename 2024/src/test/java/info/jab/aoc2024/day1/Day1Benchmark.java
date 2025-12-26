package info.jab.aoc2024.day1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day1Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        HistorianHysteria historianHysteria = new HistorianHysteria();
        String fileName = "/day1/day1-input.txt";
    }

    @Benchmark
    public void alternative1(St st) {
        st.historianHysteria.loadFle.andThen(st.historianHysteria.splitInto2Lists).apply(st.fileName);
    }

    @Benchmark
    public void alternative2(St st) {
        st.historianHysteria.loadFle.andThen(st.historianHysteria.splitInto2Lists2).apply(st.fileName);
    }

    @Benchmark
    public void alternative3(St st) {
        st.historianHysteria.loadFle.andThen(st.historianHysteria.splitInto2Lists3).apply(st.fileName);
    }
}
