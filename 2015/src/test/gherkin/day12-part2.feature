Feature: JSON Number Sum Without Red Objects
  As an Accounting-Elf
  I want to sum all numbers in a JSON document ignoring red objects
  So that I can get the correct total without double-counting

  Rule: Objects with "red" value in any property should be ignored entirely
    
  Scenario: Sum numbers in a simple array
    Given I have the JSON document "[1,2,3]"
    When I calculate the sum ignoring red objects
    Then the result should be 6

  Scenario: Sum numbers in array with red object
    Given I have the JSON document '[1,{"c":"red","b":2},3]'
    When I calculate the sum ignoring red objects
    Then the result should be 4

  Scenario: Sum numbers in object with red property
    Given I have the JSON document '{"d":"red","e":[1,2,3,4],"f":5}'
    When I calculate the sum ignoring red objects
    Then the result should be 0

  Scenario: Sum numbers in array with red string
    Given I have the JSON document '[1,"red",5]'
    When I calculate the sum ignoring red objects
    Then the result should be 6

  Rule: Red values in arrays do not affect the sum
    Scenario: Array containing red string
      Given I have the JSON document '["red",1,2,3]'
      When I calculate the sum ignoring red objects
      Then the result should be 6

  Rule: Nested objects with red should be ignored
    Scenario: Nested object with red property
      Given I have the JSON document '{"a":1,"b":{"c":"red","d":2},"e":3}'
      When I calculate the sum ignoring red objects
      Then the result should be 4
