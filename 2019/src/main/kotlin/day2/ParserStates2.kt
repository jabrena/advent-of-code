package day2

sealed class ParserStates2 {

    object SUM : ParserStates2()
    object MULTPLY : ParserStates2()
    object HALT : ParserStates2()
    object UNKNOWN : ParserStates2()

}