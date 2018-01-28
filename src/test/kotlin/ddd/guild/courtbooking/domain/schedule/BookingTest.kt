package ddd.guild.courtbooking.domain.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BookingTest {
    var schedule = Schedule()
    var booking: Booking? = null
    var changedCourt: Booking? = null
    var changedTime: Booking? = null
    var cancelledBooking: Booking? = null
    var unconfirmedBooking: Booking? = null
    var elapsedBooking: Booking? = null

    @Before
    fun setup() {
        schedule = Schedule()
        schedule.bookingCreated += { booking = it }
        schedule.courtChanged += { changedCourt = it }
        schedule.timeChanged += { changedTime = it }
        schedule.bookingCanceled += { cancelledBooking = it }
        schedule.bookingUnconfirmed += { unconfirmedBooking = it }
        schedule.bookingElapsed += { elapsedBooking = it }
        booking = null
        changedCourt = null
        changedTime = null
        cancelledBooking = null
        elapsedBooking = null
    }

    private val slot1_court1_member1 = Booking(Slot(1, 1), 1, "mem-1")
    private val slot1_court1_member2 = Booking(Slot(1, 1), 1, "mem-2")
    private val slot2_court1_member1 = Booking(Slot(1, 2), 1, "mem-1")
    private val slot2_court2_member1 = Booking(Slot(1, 2), 2, "mem-1")
    private val slot1_court2_member1 = Booking(Slot(1, 1), 2, "mem-1")
    private val slot1_court2_member2 = Booking(Slot(1, 1), 2, "mem-2")

    @Test
    fun `two members cannot book the same time on the same court`(){
        schedule.createBooking(slot1_court1_member1)
        schedule.createBooking(slot1_court1_member2)

        assertThat(schedule.bookings).doesNotContain(slot1_court1_member2)
    }

    @Test
    fun `a member cannot book two courts at the same time`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.createBooking(slot1_court2_member1)

        assertThat(schedule.bookings).doesNotContain(slot1_court2_member1)
    }

    @Test
    fun `different members can book different courts at the same time`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.createBooking(slot1_court2_member2)

        assertThat(schedule.bookings).contains(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot1_court2_member2)
    }

    @Test
    fun `a member can book the same court at different times`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.createBooking(slot2_court1_member1)

        assertThat(schedule.bookings).contains(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot2_court1_member1)
    }

    @Test
    fun `a member cannot move a booking to conflict with an existing booking`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.createBooking(slot1_court2_member2)

        schedule.updateBooking(slot1_court1_member1, slot1_court2_member1)

        assertThat(changedCourt).isNull()
        assertThat(schedule.bookings).doesNotContain(slot1_court2_member1)
        assertThat(schedule.bookings).contains(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot1_court2_member2)
    }

    @Test
    fun `a member cannot move a booking to a time where they have another booking`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.createBooking(slot2_court2_member1)

        schedule.updateBooking(slot1_court1_member1, slot2_court1_member1)

        assertThat(changedCourt).isNull()
        assertThat(schedule.bookings).doesNotContain(slot2_court1_member1)
        assertThat(schedule.bookings).contains(slot2_court2_member1)
    }

}