package info.jab.aoc2019.day4;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.opentest4j.AssertionFailedError;

class SecureContainerTest {

    private boolean twoAdjacentDigitsAreTheSame(int password) {
        char[] passwordDigits = String.valueOf(password).toCharArray();
        for (int i = 0; i < passwordDigits.length - 1; i++) {
            if (passwordDigits[i] == passwordDigits[i + 1]) {
                return true;
            }
        }
        return false;
    }

    private boolean theDigitsNeverDecrease(int password) {
        char[] passwordDigits = String.valueOf(password).toCharArray();
        for (int i = 0; i < passwordDigits.length - 1; i++) {
            if (passwordDigits[i] > passwordDigits[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private void passAllRules(int password, int start, int end) {
        then(String.valueOf(password).length()).isEqualTo(6);
        then(password).isGreaterThanOrEqualTo(start);
        then(password).isLessThanOrEqualTo(end);
        then(twoAdjacentDigitsAreTheSame(password)).isTrue();
        then(theDigitsNeverDecrease(password)).isTrue();
    }

    @Test
    @Timeout(30)
    void should_validate_password_rules() {
        //Given
        SecureContainer secureContainer = new SecureContainer();

        //When & Then
        passAllRules(111111, 1, 999999);

        //TODO Use the features from JUnit5
        try {
            passAllRules(223450, 1, 999999);
            passAllRules(123789, 1, 999999);
        } catch (AssertionFailedError e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Timeout(30)
    void should_get_passwords() {
        //Given
        int start = 246540;
        int end = 787419;
        SecureContainer secureContainer = new SecureContainer();

        //When
        List<Integer> list = secureContainer.getPasswords(start, end);

        //Then
        then(list.size()).isEqualTo(1063);
        list.forEach(password -> passAllRules(password, start, end));
    }
}

