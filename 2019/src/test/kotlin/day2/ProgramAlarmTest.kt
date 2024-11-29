package day2

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ProgramAlarmTest {

    @Test
    fun parserTest() {

        val program = ProgramAlarm()

        then(program.compute(mutableListOf(1,0,0,0,99))).isEqualTo(listOf(2,0,0,0,99))
        then(program.compute(mutableListOf(2,3,0,3,99))).isEqualTo(listOf(2,3,0,6,99))
        then(program.compute(mutableListOf(2,4,4,5,99,0))).isEqualTo(listOf(2,4,4,5,99,9801))
        then(program.compute(mutableListOf(1,1,1,4,99,5,6,0,99))).isEqualTo(listOf(30,1,1,4,2,5,6,0,99))

        then(program.compute(mutableListOf(99,0,0,0,99))).isEqualTo(listOf(99,0,0,0,99))
    }

    @Test
    fun getPosition0Test() {

        val program = ProgramAlarm()

        then(program.getPosition0()).isEqualTo(3058646)
    }

    @Disabled
    @Test
    fun getPairTest() {

        val program = ProgramAlarm()

        then(program.getPair()).isEqualTo(100)
    }

}