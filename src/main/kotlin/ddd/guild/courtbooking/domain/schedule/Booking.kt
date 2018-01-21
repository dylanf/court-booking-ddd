package ddd.guild.courtbooking.domain.schedule

typealias Day = Int
typealias TimePeriod = Int
typealias CourtNumber = Int
typealias MemberId = String

/**
 * Represents a period of time on a particular day.
 */
data class Slot (
        val day: Day,
        val period: TimePeriod)

/**
 * Represents a booking for a single court for a single period of time.
 */
data class Booking (
        val slot: Slot,
        val courtNumber: CourtNumber,
        private val memberId: MemberId,
        val confirmed: Boolean = false) {

    fun conflictsWith(other: Booking): Boolean =
            matchCourtAndTime(this, other) || matchTimeAndMember(this, other)

    private fun matchTimeAndMember(a: Booking, b: Booking) =
            a.slot == b.slot && b.memberId == a.memberId

    private fun matchCourtAndTime(a: Booking, b: Booking) =
            a.slot == b.slot && a.courtNumber == b.courtNumber
}