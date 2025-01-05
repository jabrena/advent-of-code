Feature: Knights of the Dinner Table
  As a Christmas dinner host
  I want to find the optimal seating arrangement
  To maximize the total happiness of the guests

  Background:
    Given a circular table with enough space for all guests
    And each person will have exactly two neighbors

  Scenario: Calculate optimal happiness for a small example group
    Given the following happiness levels between guests:
      | Person1 | Person2 | Happiness |
      | Alice   | Bob     | 54        |
      | Alice   | Carol   | -79       |
      | Alice   | David   | -2        |
      | Bob     | Alice   | 83        |
      | Bob     | Carol   | -7        |
      | Bob     | David   | -63       |
      | Carol   | Alice   | -62       |
      | Carol   | Bob     | 60        |
      | Carol   | David   | 55        |
      | David   | Alice   | 46        |
      | David   | Bob     | -7        |
      | David   | Carol   | 41        |
    When I calculate all possible seating arrangements
    Then the optimal arrangement should result in a total happiness change of 330

  Scenario: Calculate optimal happiness for the real guest list
    Given the real guest list and their happiness levels
    When I calculate all possible seating arrangements
    Then I should find the total happiness change for the optimal arrangement
