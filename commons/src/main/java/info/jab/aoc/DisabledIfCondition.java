package info.jab.aoc;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

/**
 * Execution condition for {@link DisabledIf} annotation.
 * Disables tests when the specified system property matches the expected value.
 */
class DisabledIfCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<DisabledIf> disabledIf = findAnnotation(context);

        if (disabledIf.isEmpty()) {
            return ConditionEvaluationResult.enabled("@DisabledIf not present");
        }

        DisabledIf annotation = disabledIf.get();
        String propertyName = annotation.name();
        String expectedValue = annotation.value();
        String reason = annotation.reason();

        String actualValue = System.getProperty(propertyName);

        // If property is not set, treat it as "false"
        if (actualValue == null) {
            actualValue = "false";
        }

        // Disable if the actual value matches the expected value (which means condition is met)
        if (expectedValue.equalsIgnoreCase(actualValue)) {
            String message = reason.isEmpty()
                ? String.format("System property '%s' is '%s'", propertyName, actualValue)
                : reason;
            return ConditionEvaluationResult.disabled(message);
        }

        // Enable if the actual value doesn't match (condition not met, so test runs)
        return ConditionEvaluationResult.enabled(
            String.format("System property '%s' is '%s' (not '%s')", propertyName, actualValue, expectedValue)
        );
    }

    private Optional<DisabledIf> findAnnotation(ExtensionContext context) {
        // Check method first
        Optional<DisabledIf> methodAnnotation = context.getTestMethod()
            .map(method -> method.getAnnotation(DisabledIf.class));

        if (methodAnnotation.isPresent()) {
            return methodAnnotation;
        }

        // Then check class
        return context.getTestClass()
            .map(clazz -> clazz.getAnnotation(DisabledIf.class));
    }
}

