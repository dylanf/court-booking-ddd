package ddd.guild.courtbooking.api

import ddd.guild.courtbooking.domain.schedule.Booking
import ddd.guild.courtbooking.domain.schedule.Schedule
import org.joda.time.DateTime
import org.joda.time.Interval
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class HomeController {

    val schedule = Schedule()

    @GetMapping("/")
    fun getHome(model: Model): String {
        model.addAttribute("bookings", schedule.bookings)
        model.addAttribute("bookingCmd", MakeBooking())
        return "home"
    }

    @PostMapping("/")
    fun postHome(@ModelAttribute("bookingCmd") bookingCmd: MakeBooking, errors: BindingResult, model: Model): String {
        val booking = Booking(
                Interval(bookingCmd.start, bookingCmd.end),
                bookingCmd.court,
                bookingCmd.member
        )
        schedule.createBooking(booking)
        model.addAttribute("bookings", schedule.bookings)
        model.addAttribute("bookingCmd", MakeBooking())
        return "home"
    }

    class MakeBooking(
            var start: DateTime = DateTime.now(),
            var end: DateTime = DateTime.now().plusMinutes(40),
            var court: Int = 1,
            var member: String = "me"
    )

}