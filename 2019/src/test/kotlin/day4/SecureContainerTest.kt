package day4

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class SecureContainerTest {

    fun `two adjacent digits are the same`(password: Int) : Boolean {

        var counter = 0
        val passwordDigits = password.toString().toCharArray().map { it.toString().toInt() }

        if(passwordDigits.get(0) == passwordDigits.get(1)) {
            counter++
        }

        if(passwordDigits.get(1) == passwordDigits.get(2)) {
            counter++
        }

        if(passwordDigits.get(2) == passwordDigits.get(3)) {
            counter++
        }

        if(passwordDigits.get(3) == passwordDigits.get(4)) {
            counter++
        }

        if(passwordDigits.get(4) == passwordDigits.get(5)) {
            counter++
        }

        return counter > 0
    }

    fun `the digits never decrease`(password: Int): Boolean {

        val passwordDigits = password.toString().toCharArray().map { it.toString().toInt() }

        if(passwordDigits.get(0) <= passwordDigits.get(1)) {
        } else {
            return false
        }

        if(passwordDigits.get(1) <= passwordDigits.get(2)) {
        } else {
            return false
        }

        if(passwordDigits.get(2) <= passwordDigits.get(3)) {
        } else {
            return false
        }

        if(passwordDigits.get(3) <= passwordDigits.get(4)) {
        } else {
            return false
        }

        if(passwordDigits.get(4) <= passwordDigits.get(5)) {
        } else {
            return false
        }

        return true
    }

    fun passAllrules(password: Int, range: Pair<Int, Int> = Pair(1, 999999)) {
        //println(password)
        then(password.toString().length).isEqualTo(6)
        then(password).isGreaterThanOrEqualTo(range.first)
        then(password).isLessThanOrEqualTo(range.second)
        then(`two adjacent digits are the same`(password)).isTrue()
        then(`the digits never decrease`(password)).isTrue()
    }

    @Test
    fun passwordUnitTests () {
        passAllrules(111111)

        //TODO Use the features from JUnit5
        try {
            passAllrules(223450)
            passAllrules(123789)
        } catch (e: AssertionFailedError) {
            println(e.localizedMessage)
        }

    }

    @Test
    fun getPasswordTest() {

        //Given
        val input = Pair(246540,787419)

        //When
        val secureContainer = SecureContainer()
        val list = secureContainer.getPasswords(input)

        //Then
        then(list.size).isEqualTo(1063)
        list.map { passAllrules(it, input) }
    }

}