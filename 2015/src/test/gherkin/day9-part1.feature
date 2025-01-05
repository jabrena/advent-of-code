Feature: Santa's Route Optimization
  As Santa
  I want to find the shortest possible route visiting all locations exactly once
  So that I can deliver presents efficiently in a single night

  Background:
    Given I have a list of distances between locations

  Scenario: Calculate shortest route for sample input
    Given the following distances between cities:
      | From   | To      | Distance |
      | London | Dublin  | 464      |
      | London | Belfast | 518      |
      | Dublin | Belfast | 141      |
    When I calculate all possible routes
    Then I should get the following routes and distances:
      | Route                        | Distance |
      | Dublin -> London -> Belfast  | 982      |
      | London -> Dublin -> Belfast  | 605      |
      | London -> Belfast -> Dublin  | 659      |
      | Dublin -> Belfast -> London  | 659      |
      | Belfast -> Dublin -> London  | 605      |
      | Belfast -> London -> Dublin  | 982      |
    And the shortest route should be "London -> Dublin -> Belfast"
    And the shortest distance should be 605

  Scenario Outline: Verify individual route calculations
    Given the following distances between cities:
      | From   | To      | Distance |
      | London | Dublin  | 464      |
      | London | Belfast | 518      |
      | Dublin | Belfast | 141      |
    When I calculate the distance for route "<route>"
    Then the total distance should be <distance>

    Examples:
      | route                        | distance |
      | Dublin -> London -> Belfast  | 982      |
      | London -> Dublin -> Belfast  | 605      |
      | London -> Belfast -> Dublin  | 659      |
      | Dublin -> Belfast -> London  | 659      |
      | Belfast -> Dublin -> London  | 605      |
      | Belfast -> London -> Dublin  | 982      |

  Scenario: Validate input constraints
    Given I have a list of distances
    Then each location should be visited exactly once
    And the start and end locations must be different
    And all distances should be positive integers

  Scenario: Handle invalid inputs
    Given I have a list of distances
    When the distance data is incomplete or invalid
    Then appropriate error messages should be displayed
    And the system should not calculate routes with missing distances

  Scenario: Performance test with larger dataset
    Given I have a list of distances between many locations
    When I calculate all possible routes
    Then the solution should be found in a reasonable time
    And memory usage should remain within acceptable limits