Feature: Include Yourself in the Seating Arrangement
  As a Christmas dinner host
  I want to include myself in the seating arrangement
  So that I can calculate the optimal happiness including myself

  Background:
    Given a circular table with enough space for all guests
    And each person will have exactly two neighbors

  Scenario: Calculate optimal happiness including myself
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
    And I add myself to the guest list with happiness 0 with everyone
    When I calculate all possible seating arrangements including myself
    Then the optimal arrangement should result in a total happiness change including myself
