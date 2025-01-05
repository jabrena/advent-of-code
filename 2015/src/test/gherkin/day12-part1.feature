Feature: JSAbacusFramework.io JSON Sum Calculator
  As Santa's Accounting-Elf
  I want to sum all numbers in a JSON document
  So that I can balance the books

  Scenario Outline: Sum numbers in different JSON structures
    Given a JSON document "<json>"
    When I calculate the sum of all numbers
    Then the result should be <sum>

    Examples:
      | json                     | sum |
      | [1,2,3]                  | 6   |
      | {"a":2,"b":4}            | 6   |
      | [[[3]]]                  | 3   |
      | {"a":{"b":4},"c":-1}     | 3   |
      | {"a":[-1,1]}             | 0   |
      | [-1,{"a":1}]             | 0   |
      | []                       | 0   |
      | {}                       | 0   |

  Scenario: Process complex JSON document
    Given a complex JSON document with multiple levels
    When I calculate the sum of all numbers
    Then I should get the total sum of all numbers found
