package info.jab.aoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to conditionally disable tests based on system properties.
 *
 * <p>Example usage:
 * <pre>
 * {@code @DisabledIf(name = "runBenchmarks", value = "false", reason = "Enable with: mvn test -DrunBenchmarks=true")}
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@org.junit.jupiter.api.extension.ExtendWith(DisabledIfCondition.class)
public @interface DisabledIf {

    /**
     * The name of the system property to check.
     */
    String name();

    /**
     * The expected value. If the system property equals this value, the test is disabled.
     * If the property is not set, it's considered as having value "false".
     */
    String value() default "false";

    /**
     * Reason for disabling the test.
     */
    String reason() default "";
}

