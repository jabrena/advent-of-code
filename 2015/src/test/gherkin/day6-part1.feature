#language: en

Feature: Christmas Light Configuration
  As a competitive resident
  I want to follow Santa's light instructions
  In order to win the holiday decoration contest

  Background:
    Given I have a 1000x1000 grid of lights
    And all lights are initially turned off

  Scenario Outline: Execute different lighting commands
    When I execute the command "<command>" from <x1>,<y1> through <x2>,<y2>
    Then I should see <result> lights turned on

  Examples:
    | command  | x1  | y1  | x2  | y2  | result  |
    | turn on  | 0   | 0   | 999 | 999 | 1000000 |
    | toggle   | 0   | 0   | 999 | 0   | 1000    |
    | turn off | 499 | 499 | 500 | 500 | 0       |
