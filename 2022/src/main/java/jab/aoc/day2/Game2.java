package jab.aoc.day2;

/**
 * Player 1 options: A for Rock, B for Paper, and C for Scissors
 * Player 2 options: X for Rock, Y for Paper, and Z for Scissors
 * Shape you selected (1 for Rock, 2 for Paper, and 3 for Scissors)
 * Outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you won)
 *
 * Part 2:
 * <p>
 * A Y
 * B X
 * C Z
 * <p>
 * The second column says how the round needs to end:
 * X means you need to lose,
 * Y means you need to end the round in a draw, and
 * Z means you need to win.
 *
 * Examples:
 * In the first round, your opponent will choose Rock (A),
 * and you need the round to end in a draw (Y), so you also choose Rock.
 * This gives you a score of 1 + 3 = 4.
 * In the second round, your opponent will choose Paper (B),
 * and you choose Rock so you lose (X) with
 * a score of 1 + 0 = 1.
 * In the third round, you will defeat your opponent's Scissors with Rock for
 * a score of 1 + 6 = 7.
 *
 * @param column1 column1
 * @param column2 column2
 */
record Game2(Column1 column1, Column2 column2) {
    // @formatter:off
    public Integer getScore() {
        //Lost
        if (column2() == Column2.fromValue("X") && column1() == Column1.ROCK) {
            //Scissor
            return ShapeScores.THREE.getValue() + GameOutcomes.LOST.getValue();
        } else if (column2() == Column2.fromValue("X") && column1() == Column1.PAPER) {
            //Rock
            return ShapeScores.ONE.getValue() + GameOutcomes.LOST.getValue();
        } else if (column2() == Column2.fromValue("X") && column1() == Column1.SCISSOR) {
            //Paper
            return ShapeScores.TWO.getValue() + GameOutcomes.LOST.getValue();
        //Draw
        } else if (column2() == Column2.fromValue("Y") && column1() == Column1.ROCK) {
            //Rock
            return ShapeScores.ONE.getValue() + GameOutcomes.DRAW.getValue();
        } else if (column2() == Column2.fromValue("Y") && column1() == Column1.PAPER) {
            //Paper
            return ShapeScores.TWO.getValue() + GameOutcomes.DRAW.getValue();
        } else if (column2() == Column2.fromValue("Y") && column1() == Column1.SCISSOR) {
            //Scissor
            return ShapeScores.THREE.getValue() + GameOutcomes.DRAW.getValue();
        //Win
        } else if (column2() == Column2.fromValue("Z") && column1() == Column1.ROCK) {
            //Paper
            return ShapeScores.TWO.getValue() + GameOutcomes.WIN.getValue();
        } else if (column2() == Column2.fromValue("Z") && column1() == Column1.PAPER) {
            //Scissor
            return ShapeScores.THREE.getValue() + GameOutcomes.WIN.getValue();
        } else if (column2() == Column2.fromValue("Z") && column1() == Column1.SCISSOR) {
            //Rock
            return ShapeScores.ONE.getValue() + GameOutcomes.WIN.getValue();
        } else {
            throw new RuntimeException("Option not identified");
        }
    }
    // @formatter:on

}
