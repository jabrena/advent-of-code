package info.jab.aoc.day18;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.junit.jupiter.api.Disabled;

import com.putoet.utils.Timer;

class Day18Test {

    @Test
    void should_solve_day18_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day18/day18-input-sample.txt";

            //When
            var day = new Day18();
            var result = day.getPart1Result(fileName);

            //System.out.println(VM.current().details());
            //System.out.println(ClassLayout.parseInstance(day).toPrintable());
            //System.out.println(GraphLayout.parseInstance(day).toFootprint());

            //Then
            then(result).isEqualTo("4,6,3,5,6,3,5,2,1,0");
        });
    }

}
