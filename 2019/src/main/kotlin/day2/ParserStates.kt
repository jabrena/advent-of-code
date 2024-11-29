package day2

enum class ParserStates(val opcode: Int) {
    SUM(1),
    MULTIPLY(2),
    HALT(99),
    UNKNOWN(-1);

    companion object {

        @JvmStatic
        fun fromInt(status: Int): ParserStates =
                values().find { value -> value.opcode == status } ?: UNKNOWN

        @JvmStatic
        fun sum(positions : MutableList<Int>, start : Int): MutableList<Int> {
            positions[positions[start + 3]] = positions[positions[start + 1]] + positions[positions[start + 2]]
            return positions
        }

        @JvmStatic
        fun multiply(positions : MutableList<Int>, start : Int): MutableList<Int> {
            positions[positions[start + 3]] = positions[positions[start + 1]] * positions[positions[start + 2]]
            return positions
        }
    }
}