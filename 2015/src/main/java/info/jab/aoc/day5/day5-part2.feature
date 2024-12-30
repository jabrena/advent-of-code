Feature: Enhanced String Validator for Santa
  As Santa
  I want to validate strings using new improved rules
  To better identify which ones are naughty or nice

  Background:
    Given I have an enhanced string validator

  Rule: A nice string must have two new properties
    - Contains a pair of letters that appears at least twice without overlapping
    - Contains at least one letter that repeats with exactly one letter between

  Scenario: Validate a nice string with both required properties
    When I validate the string "qjhvhtzxzqqjkmpb"
    Then the string should be considered nice
    And it should have a repeating pair
    And it should have a letter that repeats with one between

  Scenario: Validate a nice string with overlapping rules
    When I validate the string "xxyxx"
    Then the string should be considered nice
    And it should have a repeating pair
    And it should have a letter that repeats with one between

  Scenario: Validate a naughty string without letter repeat pattern
    When I validate the string "uurcxstgmygtbstg"
    Then the string should be considered naughty
    And it should have a repeating pair
    But it should not have a letter that repeats with one between

  Scenario: Validate a naughty string without repeating pair
    When I validate the string "ieodomkazucvgmuy"
    Then the string should be considered naughty
    And it should have a letter that repeats with one between
    But it should not have a repeating pair
