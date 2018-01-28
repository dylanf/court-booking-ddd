package ddd.guild.courtbooking.domain.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ScheduleTest {
    var schedule = Schedule()
    var booking: Booking? = null
    var changedCourt: Booking? = null
    var changedTime: Booking? = null
    var cancelledBooking: Booking? = null
    var unconfirmedBooking: Booking? = null
    var elapsedBooking: Booking? = null

    val today = LocalDate.now()
    val period1 = LocalTime.of(9,0)
    val period2 = LocalTime.of(9,40)

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

    private val slot1_court1_member1 = Booking(Slot(today, period1), 1, "mem-1")
    private val slot1_court1_member2 = Booking(Slot(today, period1), 1, "mem-2")
    private val slot2_court1_member1 = Booking(Slot(today, period2), 1, "mem-1")
    private val slot1_court2_member1 = Booking(Slot(today, period1), 2, "mem-1")

    @Test
    fun `a member can create a booking`(){
        schedule.createBooking(slot1_court1_member1)

        assertThat(booking).isEqualTo(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot1_court1_member1)
    }

    @Test
    fun `a member cannot create a booking that causes a conflict`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.createBooking(slot1_court1_member2)

        assertThat(schedule.bookings).contains(slot1_court1_member1)
        assertThat(schedule.bookings).doesNotContain(slot1_court1_member2)
    }

    @Test
    fun `a member should be able to change the court of a booking`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.updateBooking(slot1_court1_member1, slot1_court2_member1)

        assertThat(changedCourt).isEqualTo(slot1_court2_member1)
        assertThat(schedule.bookings).doesNotContain(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot1_court2_member1)
    }

    @Test
    fun `a member should be able to change the time of a booking`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.updateBooking(slot1_court1_member1, slot2_court1_member1)

        assertThat(changedTime).isEqualTo(slot2_court1_member1)
        assertThat(schedule.bookings).doesNotContain(slot1_court1_member1)
        assertThat(schedule.bookings).contains(slot2_court1_member1)
    }

    @Test
    fun `a member can cancel a booking`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.cancelBooking(slot1_court1_member1)

        assertThat(schedule.bookings).doesNotContain(slot1_court1_member1)
        assertThat(cancelledBooking).isEqualTo(slot1_court1_member1)
    }

    @Test
    fun `unconfirmed bookings should trigger unconfirmed notifications`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.notifyUnconfirmedBookings()

        assertThat(unconfirmedBooking).isEqualTo(slot1_court1_member1)
    }

    @Test
    fun `confirmed bookings should not trigger unconfirmed notifications`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.confirmBooking(slot1_court1_member1)

        schedule.notifyUnconfirmedBookings()

        assertThat(unconfirmedBooking).isNull()
    }

    @Test
    fun `elapsed bookings should trigger elapsed notifications`() {
        schedule.createBooking(slot1_court1_member1)

        schedule.notifyElapsedBookings()

        assertThat(elapsedBooking).isEqualTo(slot1_court1_member1)
    }

    @Test
    fun `confirmed bookings should not trigger elapsed notifications`() {
        schedule.createBooking(slot1_court1_member1)
        schedule.confirmBooking(slot1_court1_member1)

        schedule.notifyElapsedBookings()

        assertThat(elapsedBooking).isNull()
    }

}