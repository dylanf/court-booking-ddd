package ddd.guild.courtbooking.domain.schedule

import org.assertj.core.api.Assertions.assertThat
import org.joda.time.Interval
import org.junit.Test

class BookingTest {
    val period1 = Interval.parse("T09:00/T09:40")
    val period2 = Interval.parse("T09:40/T10:20")
    val longPeriod = Interval.parse("T09:40/T11:00")

    private val slot1_court1_member1 = Booking(period1, 1, "mem-1")
    private val slot1_court1_member2 = Booking(period1, 1, "mem-2")
    private val slot2_court1_member1 = Booking(period2, 1, "mem-1")
    private val slot1_court2_member1 = Booking(period1, 2, "mem-1")
    private val slot1_court2_member2 = Booking(period1, 2, "mem-2")
    private val longPeriod_court1_member1 = Booking(longPeriod, 1, "mem-1")

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

    @Test
    fun `overlapping periods cannot be booked`() {
        val conflict1 = longPeriod_court1_member1.conflictsWith(slot2_court1_member1)
        val conflict2 = slot2_court1_member1.conflictsWith(longPeriod_court1_member1)
        val conflict3 = longPeriod_court1_member1.conflictsWith(slot1_court1_member1)

        assertThat(conflict1).`as`("Long overlap must conflict with a slot in the same range").isTrue()
        assertThat(conflict2).`as`("A slot must conflict with a long period in the same range").isTrue()
        assertThat(conflict3).`as`("A slot adjacent to a period must not conflict").isFalse()
    }

}