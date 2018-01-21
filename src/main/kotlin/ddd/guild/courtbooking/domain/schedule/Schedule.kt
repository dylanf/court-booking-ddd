package ddd.guild.courtbooking.domain.schedule

/**
 * Used to enable publishing and observation of events.
 */
class Event<TArgs> : ArrayList<(TArgs) -> Unit>() {
    internal operator fun invoke(e: TArgs) = forEach { it(e) }
}

/**
 * The Schedule represents all available slots on all courts.
 */
class Schedule {
    val bookingCreated = Event<Booking>()
    val courtChanged = Event<Booking>()
    val timeChanged = Event<Booking>()
    val bookingCanceled = Event<Booking>()
    val bookingUnconfirmed = Event<Booking>()
    val bookingElapsed = Event<Booking>()
    val bookings = arrayListOf<Booking>()

    fun createBooking(booking: Booking) {
        if (!bookings.any { it.conflictsWith(booking) }) {
            bookings += booking
            bookingCreated(booking)
        }
    }

    fun updateBooking(old: Booking, new: Booking) {
        if (!bookings.filter { it != old }.any { it.conflictsWith(new) }) {
            bookings -= old
            bookings += new
            if (old.slot != new.slot) timeChanged(new)
            if (old.courtNumber != new.courtNumber) courtChanged(new)
        }
    }

    fun cancelBooking(booking: Booking) {
        bookings -= booking
        bookingCanceled(booking)
    }

    fun confirmBooking(booking: Booking) {
        val unconfirmed = booking.copy(confirmed = false)
        val confirmed = booking.copy(confirmed = true)
        bookings -= unconfirmed
        bookings += confirmed
    }

    fun notifyUnconfirmedBookings() {
        bookings.filter { !it.confirmed }.forEach { bookingUnconfirmed(it) }
    }

    fun notifyElapsedBookings() {
        bookings.filter { !it.confirmed }.forEach { bookingElapsed (it) }
    }

    private fun Booking.conflictsWith(other: Booking): Boolean =
            matchCourtAndTime(this, other) || matchTimeAndMember(this, other)

    private fun matchTimeAndMember(a: Booking, b: Booking) =
            a.slot == b.slot && b.memberId == a.memberId

    private fun matchCourtAndTime(a: Booking, b: Booking) =
            a.slot == b.slot && a.courtNumber == b.courtNumber
}