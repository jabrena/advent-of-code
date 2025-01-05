package info.jab.aoc.day11;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/10
 */
public class Day11 implements Day<String> {

    @Override
    public String getPart1Result(String input) {
        PasswordValidator validator = new PasswordValidator();
        String nextPassword = validator.findNextValidPassword(input);
        return nextPassword;
    }

    @Override
    public String getPart2Result(String input) {
        var newPassword = this.getPart1Result(input);
        return this.getPart1Result(newPassword);
    }
}
