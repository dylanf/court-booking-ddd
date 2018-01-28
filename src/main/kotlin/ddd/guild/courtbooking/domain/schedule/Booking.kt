package ddd.guild.courtbooking.domain.schedule

import org.joda.time.Interval

typealias CourtNumber = Int
typealias MemberId = String

/**
 * Represents a booking for a single court for a single period of time.
 */
data class Booking(
        val interval: Interval,
        val courtNumber: CourtNumber,
        private val memberId: MemberId,
        val confirmed: Boolean = false) {

    fun conflictsWith(other: Booking): Boolean =
            matchCourtAndTime(this, other) || matchTimeAndMember(this, other)

    private fun matchTimeAndMember(a: Booking, b: Booking) =
            a.interval.overlaps(b.interval) && b.memberId == a.memberId

    private fun matchCourtAndTime(a: Booking, b: Booking) =
            a.interval.overlaps(b.interval) && a.courtNumber == b.courtNumber
}