Feature: Validation of naughty and nice strings

  Background:
    Given Santa needs to validate a list of text strings

  Scenario: Validate a nice string with all requirements
    Given I have the string "ugknbfddgicrmopn"
    When I verify if the string is nice
    Then it should be classified as "nice"
    And it should have at least three vowels
    And it should have at least one letter appearing twice in a row
    And it should not contain forbidden substrings

  Scenario: Validate a nice string with overlapping rules
    Given I have the string "aaa"
    When I verify if the string is nice
    Then it should be classified as "nice"
    And it should have at least three vowels
    And it should have at least one letter appearing twice in a row

  Scenario Outline: Validate naughty strings
    Given I have the string "<string>"
    When I verify if the string is nice
    Then it should be classified as "naughty"
    And it should fail because "<reason>"

    Examples:
      | string           | reason                               |
      | jchzalrnumimnmhp | it has no double letter              |
      | haegwjzuvuyypxyu | it contains forbidden substring 'xy' |
      | dvszwmarrgswjxmb | it has only one vowel                |

  Rule: Criteria for a nice string
    * Must contain at least three vowels (aeiou only)
    * Must contain at least one letter appearing twice in a row
    * Must not contain the substrings: ab, cd, pq, or xy

  Rule: New criteria for a nice string (Part 2)
    * Must contain a pair of letters that appears at least twice without overlapping
    * Must contain at least one letter that repeats with exactly one letter between them

  Scenario: Validate a nice string with non-overlapping pair and letter repeat
    Given I have the string "qjhvhtzxzqqjkmpb"
    When I verify if the string is nice using new rules
    Then it should be classified as "nice"
    And it should have a pair of letters that appears twice
    And it should have a letter that repeats with one letter between

  Scenario: Validate a nice string with overlapping rules
    Given I have the string "xxyxx"
    When I verify if the string is nice using new rules
    Then it should be classified as "nice"
    And it should have a pair of letters that appears twice
    And it should have a letter that repeats with one letter between

  Scenario Outline: Validate naughty strings with new rules
    Given I have the string "<string>"
    When I verify if the string is nice using new rules
    Then it should be classified as "naughty"
    And it should fail new rules because "<reason>"

    Examples:
      | string           | reason                                           |
      | uurcxstgmygtbstg | it has no letter repeating with one between     |
      | ieodomkazucvgmuy | it has no pair of letters appearing twice       |