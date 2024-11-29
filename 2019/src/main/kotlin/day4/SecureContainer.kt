package day4

/**
 * --- Day 4: Secure Container ---
 * You arrive at the Venus fuel depot only to discover it's protected by a password.
 * The Elves had written the password on a sticky note, but someone threw it out.
 *
 * However, they do remember a few key facts about the password:
 *
 * It is a six-digit number.
 * The value is within the range given in your puzzle input.
 * Two adjacent digits are the same (like 22 in 122345).
 * Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
 *
 * Other than the range rule, the following are true:
 * 111111 meets these criteria (double 11, never decreases).
 * 223450 does not meet these criteria (decreasing pair of digits 50).
 * 123789 does not meet these criteria (no double).
 *
 * How many different passwords within the range given in your puzzle input meet these criteria?
 *
 * Your puzzle input is 246540-787419.
 *
 * --- Part Two ---
 * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of matching digits.
 *
 * Given this additional criterion, but still ignoring the range rule, the following are now true:
 *
 * 112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
 * 123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
 * 111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
 * How many different passwords within the range given in your puzzle input meet all of the criteria?
 *
 * Your puzzle input is still 246540-787419.
 */
class SecureContainer() {

    fun `six-digit`(param : Int) = param.toString().length == 6

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

    fun getPasswords(range: Pair<Int, Int>): List<Int> {
        return IntRange(range.first, range.second)
                .filter {        `six-digit`(it)
                            .and(`two adjacent digits are the same`(it))
                            .and(`the digits never decrease`(it))
                        }
    }
}