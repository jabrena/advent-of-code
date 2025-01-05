Feature: Corporate Password Policy Validation
  As a Security-Elf
  I want to enforce strict password policies
  So that Santa's password remains secure

  Background:
    Given passwords must be exactly 8 lowercase letters
    And passwords cannot contain the letters 'i', 'o', or 'l'
    And the current password is "hepxcrrq"

  Rule: Password must contain an increasing straight of three letters

  Scenario: Password contains valid straight sequence
    Given a password "abcdefgh"
    When I check for a straight sequence
    Then it should be valid because it contains "abc"

  Scenario: Password lacks straight sequence
    Given a password "abbceffg"
    When I check for a straight sequence
    Then it should be invalid
    And the validation should indicate "missing straight sequence of three letters"

  Rule: Password must contain two different non-overlapping pairs

  Scenario: Password contains two different pairs
    Given a password "abcdffaa"
    When I check for letter pairs
    Then it should be valid because it contains "ff" and "aa"

  Scenario: Password contains only one pair
    Given a password "abbcegjk"
    When I check for letter pairs
    Then it should be invalid
    And the validation should indicate "needs two different pairs"

  Rule: Password incrementing follows specific rules

  Scenario Outline: Incrementing password characters
    Given a password "<initial>"
    When I increment the password
    Then the result should be "<result>"

    Examples:
      | initial  | result   |
      | xx       | xy       |
      | xz       | ya       |
      | ya       | yb       |

  Scenario: Skip prohibited letters when incrementing
    Given a password "ghijklmn"
    When I increment the password
    Then it should skip all passwords containing 'i'
    And the result should be "ghjaabcc"

  Rule: Complete password update process

  Scenario: Finding next valid password
    Given the current password "abcdefgh"
    When I search for the next valid password
    Then I should find "abcdffaa"
    And it should contain a straight sequence
    And it should contain two different pairs
    And it should not contain prohibited letters

  Scenario: Finding next valid password from puzzle input
    Given the current password "hepxcrrq"
    When I search for the next valid password
    Then the result should meet all validation rules
    And it should be the first valid password after "hepxcrrq"
