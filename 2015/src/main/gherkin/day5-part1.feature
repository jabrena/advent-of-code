Feature: String Validator for Santa
  As Santa
  I want to validate strings in my text file
  To identify which ones are naughty or nice

  Background:
    Given I have a string validator

  Rule: A nice string must have three properties
    - Contains at least three vowels (aeiou only)
    - Contains at least one letter that appears twice in a row
    - Does not contain the strings ab, cd, pq, or xy

  Scenario: Validate a nice string with all required properties
    When I validate the string "ugknbfddgicrmopn"
    Then the string should be considered nice
    And it should have at least three vowels
    And it should have a double letter
    And it should not contain forbidden substrings

  Scenario: Validate a nice string with overlapping letters
    When I validate the string "aaa"
    Then the string should be considered nice

  Scenario: Validate a naughty string without double letter
    When I validate the string "jchzalrnumimnmhp"
    Then the string should be considered naughty

  Scenario: Validate a naughty string with forbidden substring
    When I validate the string "haegwjzuvuyypxyu"
    Then the string should be considered naughty

  Scenario: Validate a naughty string with only one vowel
    When I validate the string "dvszwmarrgswjxmb"
    Then the string should be considered naughty
