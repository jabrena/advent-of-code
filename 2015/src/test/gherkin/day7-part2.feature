Feature: Circuit Assembly with Signal Override
  As little Bobby Tables
  I want to reset and rerun the circuit with an overridden wire
  So that I can determine the new signal provided to wire 'a'

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

  Rule: When resetting the circuit, all wires except the overridden one are cleared

  Scenario: Override wire and reset circuit
    Given a set of circuit instructions from the puzzle input
    When the circuit is fully processed
    And I store the signal from wire "a"
    And I reset all wires except wire "b"
    And I override wire "b" with the stored signal
    And the circuit is fully processed again
    Then I should get a new signal on wire "a"

  Scenario: Verify circuit reset
    Given a set of circuit instructions
    And the circuit has been fully processed
    When I reset the circuit keeping wire "b" signal
    Then all wires except "b" should have no signal

  Scenario: Process circuit with overridden wire
    Given wire "b" is overridden with signal 123
    And the instruction "456 -> b" is provided
    When the circuit is processed
    Then wire "b" should maintain signal 123 