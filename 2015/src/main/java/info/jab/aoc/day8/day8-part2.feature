Feature: String encoding calculator
  As a programmer
  I want to calculate the difference between encoded and original characters
  To solve day 8 puzzle part 2

  Background:
    Given a list of literal text strings

  Scenario Outline: Calculate additional characters after encoding
    When I encode the string "<original_string>"
    Then the encoded string should be "<encoded_string>"
    And the character increase should be <increase>

    Examples:
      | original_string | encoded_string    | increase |
      | ""             | "\"\""            | 4        |
      | "abc"          | "\"abc\""         | 4        |
      | "aaa\"aaa"     | "\"aaa\\\"aaa\""  | 6        |
      | "\x27"         | "\"\\x27\""       | 5        |

  Scenario: Calculate total difference
    Given the following strings:
      | ""         |
      | "abc"      |
      | "aaa\"aaa" |
      | "\x27"     |
    When I encode all strings
    Then the total encoded length should be 42
    And the original length should be 23
    And the total difference should be 19
