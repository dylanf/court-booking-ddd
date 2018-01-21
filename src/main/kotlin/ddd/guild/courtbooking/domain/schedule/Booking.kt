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
        val memberId: MemberId,
        val confirmed: Boolean = false)