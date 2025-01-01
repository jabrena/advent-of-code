Feature: Circuit Assembly and Signal Processing
  As little Bobby Tables
  I want to assemble a circuit with wires and logic gates
  So that I can determine the signal provided to wire 'a'

  Background:
    Given the circuit supports 16-bit signals from 0 to 65535
    And the circuit can process the following operations:
      | Operation | Description                    |
      | ->        | Direct signal assignment       |
      | AND       | Bitwise AND                   |
      | OR        | Bitwise OR                    |
      | LSHIFT    | Left shift by specified bits  |
      | RSHIFT    | Right shift by specified bits |
      | NOT       | Bitwise complement            |

  Rule: Wires can only receive a signal from one source but can output to multiple destinations

  Rule: Gates provide no signal until all inputs have signals

  Scenario: Processing a simple circuit with various operations
    Given the following circuit instructions:
      """
      123 -> x
      456 -> y
      x AND y -> d
      x OR y -> e
      x LSHIFT 2 -> f
      y RSHIFT 2 -> g
      NOT x -> h
      NOT y -> i
      """
    When the circuit is fully processed
    Then the following wire signals should be present:
      | Wire | Signal |
      | d    | 72     |
      | e    | 507    |
      | f    | 492    |
      | g    | 114    |
      | h    | 65412  |
      | i    | 65079  |
      | x    | 123    |
      | y    | 456    |

  Scenario: Direct signal assignment
    Given the instruction "123 -> x"
    When the circuit is processed
    Then wire "x" should have signal 123

  Scenario: Bitwise AND operation
    Given wire "x" has signal 123
    And wire "y" has signal 456
    When processing instruction "x AND y -> d"
    Then wire "d" should have signal 72

  Scenario: Bitwise OR operation
    Given wire "x" has signal 123
    And wire "y" has signal 456
    When processing instruction "x OR y -> e"
    Then wire "e" should have signal 507

  Scenario: Left shift operation
    Given wire "x" has signal 123
    When processing instruction "x LSHIFT 2 -> f"
    Then wire "f" should have signal 492

  Scenario: Right shift operation
    Given wire "y" has signal 456
    When processing instruction "y RSHIFT 2 -> g"
    Then wire "g" should have signal 114

  Scenario: Bitwise NOT operation
    Given wire "x" has signal 123
    When processing instruction "NOT x -> h"
    Then wire "h" should have signal 65412

  Scenario: Gate with incomplete inputs
    Given wire "x" has signal 123
    And wire "y" has no signal
    When processing instruction "x AND y -> z"
    Then wire "z" should have no signal

  Scenario: Finding signal on wire 'a'
    Given a set of circuit instructions from the puzzle input
    When the circuit is fully processed
    Then I should be able to determine the signal on wire "a"