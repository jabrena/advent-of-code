Feature: Santa's Longest Route Calculator
  Como Santa Claus
  Quiero encontrar la ruta más larga posible
  Para demostrar mi habilidad de navegación

  Background:
    Given un conjunto de distancias entre ciudades
    And debo visitar cada ciudad exactamente una vez
    And puedo empezar y terminar en cualquier ciudad

  Scenario: Encontrar la ruta más larga
    Given las siguientes distancias entre ciudades:
      | Ciudad1  | Ciudad2  | Distancia |
      | Dublin   | London   | 464       |
      | London   | Belfast  | 518       |
      | Dublin   | Belfast  | 141       |
    When calculo todas las rutas posibles
    Then la ruta más larga debe ser 982
    And debe incluir un camino como "Dublin -> London -> Belfast"

  Scenario: Validar restricciones de la ruta
    Given un conjunto de ciudades y distancias
    When genero una ruta
    Then cada ciudad debe ser visitada exactamente una vez
    And la ruta debe comenzar y terminar en ciudades diferentes
