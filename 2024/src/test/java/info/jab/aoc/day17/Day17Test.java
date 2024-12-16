package info.jab.aoc.day17;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day17Test {

    @Test
    void should_solve_day17_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day17/day17-input-sample.txt";

            //When
            var day = new Day17();
            var result = day.getPart1Result(fileName);

            System.out.println(VM.current().details());
            System.out.println(ClassLayout.parseInstance(day).toPrintable());
            System.out.println(GraphLayout.parseInstance(day).toFootprint());

            //Then
            then(result).isEqualTo(2028);
        });
    }

}
