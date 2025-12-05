package info.jab.aoc;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class TrampolineTest {

    @Test
    void should_return_result_when_done() {
        //Given
        Trampoline<Integer> done = new Trampoline.Done<>(42);

        //When
        Integer result = Trampoline.run(done);

        //Then
        then(result).isEqualTo(42);
    }

    @Test
    void should_return_result_when_more_returns_done() {
        //Given
        Trampoline<Integer> more = new Trampoline.More<>(() -> new Trampoline.Done<>(100));

        //When
        Integer result = Trampoline.run(more);

        //Then
        then(result).isEqualTo(100);
    }

    @Test
    void should_compute_factorial_using_trampoline() {
        //Given
        int n = 5;
        Trampoline<Long> factorialTrampoline = factorial(n, 1L);

        //When
        Long result = Trampoline.run(factorialTrampoline);

        //Then
        then(result).isEqualTo(120L);
    }

    @Test
    void should_compute_large_factorial_without_stack_overflow() {
        //Given
        int n = 20; // 20! = 2432902008176640000, fits in long
        Trampoline<Long> factorialTrampoline = factorial(n, 1L);

        //When
        Long result = Trampoline.run(factorialTrampoline);

        //Then
        then(result).isPositive();
        // Verify it's a large number (20! = 2432902008176640000)
        then(result).isEqualTo(2432902008176640000L);
    }

    @Test
    void should_compute_sum_using_trampoline() {
        //Given
        int n = 10;
        Trampoline<Integer> sumTrampoline = sum(n, 0);

        //When
        Integer result = Trampoline.run(sumTrampoline);

        //Then
        then(result).isEqualTo(55); // 1+2+3+4+5+6+7+8+9+10 = 55
    }

    @Test
    void should_compute_sum_of_large_number_without_stack_overflow() {
        //Given
        int n = 10000;
        Trampoline<Integer> sumTrampoline = sum(n, 0);

        //When
        Integer result = Trampoline.run(sumTrampoline);

        //Then
        then(result).isEqualTo(50005000); // n*(n+1)/2 = 10000*10001/2
    }

    @Test
    void should_handle_string_result() {
        //Given
        Trampoline<String> stringTrampoline = new Trampoline.More<>(
            () -> new Trampoline.More<>(
                () -> new Trampoline.Done<>("Hello, Trampoline!")
            )
        );

        //When
        String result = Trampoline.run(stringTrampoline);

        //Then
        then(result).isEqualTo("Hello, Trampoline!");
    }

    @Test
    void should_handle_nested_more_continuations() {
        //Given
        Trampoline<Integer> nested = new Trampoline.More<>(
            () -> new Trampoline.More<>(
                () -> new Trampoline.More<>(
                    () -> new Trampoline.Done<>(999)
                )
            )
        );

        //When
        Integer result = Trampoline.run(nested);

        //Then
        then(result).isEqualTo(999);
    }

    /**
     * Helper method to compute factorial using trampoline pattern
     * This would normally cause stack overflow for large numbers
     */
    private Trampoline<Long> factorial(int n, long acc) {
        if (n <= 1) {
            return new Trampoline.Done<>(acc);
        }
        return new Trampoline.More<>(() -> factorial(n - 1, acc * n));
    }

    /**
     * Helper method to compute sum of numbers from 1 to n using trampoline pattern
     * This would normally cause stack overflow for large numbers
     */
    private Trampoline<Integer> sum(int n, int acc) {
        if (n <= 0) {
            return new Trampoline.Done<>(acc);
        }
        return new Trampoline.More<>(() -> sum(n - 1, acc + n));
    }
}

