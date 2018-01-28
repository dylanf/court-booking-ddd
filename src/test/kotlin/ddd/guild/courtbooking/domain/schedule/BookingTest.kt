package ddd.guild.courtbooking.domain.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class BookingTest {
    val today = LocalDate.now()
    val period1 = LocalTime.of(9,0)
    val period2 = LocalTime.of(9,40)

    private val slot1_court1_member1 = Booking(Slot(today, period1), 1, "mem-1")
    private val slot1_court1_member2 = Booking(Slot(today, period1), 1, "mem-2")
    private val slot2_court1_member1 = Booking(Slot(today, period2), 1, "mem-1")
    private val slot1_court2_member1 = Booking(Slot(today, period1), 2, "mem-1")
    private val slot1_court2_member2 = Booking(Slot(today, period1), 2, "mem-2")

    @Test
    fun `two members cannot book the same time on the same court`() {
        val conflict = slot1_court1_member1.conflictsWith(slot1_court1_member2)

        assertThat(conflict).isTrue()
    }

    @Test
    fun `a member cannot book two courts at the same time`() {
        val conflict = slot1_court1_member1.conflictsWith(slot1_court2_member1)

        assertThat(conflict).isTrue()
    }

    @Test
    fun `different members can book different courts at the same time`() {
        val conflict = slot1_court1_member1.conflictsWith(slot1_court2_member2)

        assertThat(conflict).isFalse()
    }

    @Test
    fun `a member can book the same court at different times`() {
        val conflict = slot1_court1_member1.conflictsWith(slot2_court1_member1)

        assertThat(conflict).isFalse()
    }

}