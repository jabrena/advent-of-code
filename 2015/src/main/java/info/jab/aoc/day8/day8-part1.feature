Feature: String Literal Space Calculator
  As Santa
  I need to calculate the difference between code representation and memory representation of strings
  So that I can determine how much space my digital list will take up

  Background:
    Given I have a file containing string literals
    And each string literal is on a separate line
    And the file uses standard escape sequences
      | Escape Sequence | Representation              |
      | \\             | single backslash            |
      | \"             | double-quote character      |
      | \x[0-9A-F]{2}  | ASCII character (hex code)  |

  Rule: String literals must be properly counted both in code and memory representation

  Scenario Outline: Calculate space difference for various string literals
    Given I have a string literal "<code_string>"
    When I count the characters in code representation
    And I count the characters in memory representation
    Then the code character count should be <code_count>
    And the memory character count should be <memory_count>

    Examples:
      | code_string | code_count | memory_count | description                    |
      | ""         | 2          | 0            | empty string                   |
      | "abc"      | 5          | 3            | simple string                  |
      | "aaa\"aaa" | 10         | 7            | string with escaped quote      |
      | "\x27"     | 6          | 1            | string with hex escape         |

  Scenario: Calculate total difference for multiple strings
    Given I have the following string literals in my file:
      | String     |
      | ""        |
      | "abc"     |
      | "aaa\"aaa"|
      | "\x27"    |
    When I calculate the total characters of code
    And I calculate the total characters in memory
    Then the total code character count should be 23
    And the total memory character count should be 11
    And the difference should be 12

  Rule: Special escape sequences must be properly processed

  Scenario Outline: Handle escape sequences correctly
    Given I have a string with escape sequence "<escape_sequence>"
    When I process the escape sequence
    Then it should be interpreted as "<interpretation>"

    Examples:
      | escape_sequence | interpretation        |
      | \\             | single backslash      |
      | \"             | double-quote          |
      | \x27          | apostrophe character  |

  Rule: Whitespace should be ignored in calculations

  Scenario: Ignore whitespace in file
    Given I have a file with string literals separated by whitespace:
      """
      ""
      "abc"
      "aaa\"aaa"
      "\x27"
      """
    When I calculate the total difference
    Then the result should be the same as without whitespace
    And the difference should be 12