package info.jab.aoc.day13;

//TODO Refactor with java records style
//https://math.libretexts.org/Bookshelves/Precalculus/Precalculus_1e_(OpenStax)/09%3A_Systems_of_Equations_and_Inequalities/9.08%3A_Solving_Systems_with_Cramer's_Rule
class Claw {
    Button a;
    Button b;
    Prize p;

    Claw(String in) {
        String[] lines = in.split("\n");
        a = Button.of(lines[0]);
        b = Button.of(lines[1]);
        p = Prize.of(lines[2]);
    }

    void update() {
        p = new Prize(p.x() + 10000000000000L, p.y() + 10000000000000L);
    }

    /**
     * The solve() method calculates the minimal cost to win the prize if it's possible to do so by pressing Button A and Button B a certain number of times.
     * It uses a system of linear equations to figure out the number of presses required for both buttons.
     *
     * The Equations:
     * For each claw machine, we have the following equations based on the movement of the claw:
     *
     * Let i be the number of presses for Button A.
     * Let j be the number of presses for Button B.
     * From the problem, we know that the claw must land at the exact coordinates of the prize, i.e., the claw's movement must satisfy the following system:
     *
     * i * a.x() + j * b.x() = p.x()
     * i * a.y() + j * b.y() = p.y()
     * Where:
     *
     * a.x() and a.y() are the X and Y displacements of Button A, respectively.
     * b.x() and b.y() are the X and Y displacements of Button B, respectively.
     * p.x() and p.y() are the X and Y coordinates of the prize.
     * The goal is to find integer values of i and j such that the system is satisfied.
     *
     * Formula Derivation:
     * We can solve this system of linear equations using Cramer's rule. This method allows us to solve a system of linear equations in the form Ax = b. For our case, the system can be represented as:
     *
     * | a.x() b.x() |   | i |   = | p.x() |
     * | a.y() b.y() |   | j |     | p.y() |
     * Using Cramer's rule, the solution for i and j can be computed as:
     * j = (p.y() * a.x() - a.y() * p.x()) / (b.y() * a.x() - a.y() * b.x())
     * i = (p.x() - b.x() * j) / a.x()
     * This is exactly what is computed in the solve() method.
     *
     * Checking for Integer Solutions:
     * The key insight here is that for the solution to be valid, both i and j must be integers. The code checks this by verifying:
     *
     * If j - (long) j == 0 (i.e., j is an integer).
     * If i - (long) i == 0 (i.e., i is an integer).
     * If both i and j are integers, the method calculates the total cost of the operation:
     *
     * cost = i * 3 + j
     * Where:
     *
     * Each press of Button A costs 3 tokens.
     * Each press of Button B costs 1 token.
     * The result is returned as the cost in tokens.
     *
     * Return 0 if No Valid Solution:
     * If either i or j is not an integer, the method returns 0, indicating that it is impossible to win the prize for that machine using any combination of button presses.
     *
     * 3. Summary of the solve() Method:
     * The method computes the values of i and j using the system of linear equations.
     * It checks if both i and j are integers (valid solutions).
     * If valid, it calculates the cost in tokens and returns it.
     * If not valid, it returns 0, indicating no solution exists for that machine.
     */
    long solve() {
        double j = (double) (p.y() * a.x() - a.y() * p.x()) / (b.y() * a.x() - a.y() * b.x());
        double i = (p.x() - b.x() * j) / a.x();
        if(j - (long) j == 0 && i - (long) i == 0) {
            return (long) (i * 3 + j);
        }
        return 0;
    }
}
