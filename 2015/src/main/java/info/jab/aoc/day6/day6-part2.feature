Feature: Light Grid Brightness Control
  As Santa's helper
  I want to control individual light brightness levels
  So that I can follow Santa's correctly translated instructions

  Background:
    Given a 1000x1000 light grid where all lights start with brightness 0

  Scenario: Turning on lights increases brightness
    When I turn on lights from 0,0 through 0,0
    Then the total brightness should increase by 1

  Scenario: Toggling lights increases brightness by 2
    When I toggle lights from 0,0 through 999,999
    Then the total brightness should increase by 2000000

  Scenario: Turning off lights decreases brightness
    Given some lights with brightness greater than 0
    When I turn off lights from 0,0 through 0,0
    Then the brightness should decrease by 1
    And no light can have negative brightness

  Scenario: Following Santa's complete instructions
    When I follow all of Santa's brightness instructions
    Then I should get the total brightness of all lights combined
