package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Pure helpers (no Android dependencies) so they can be unit tested on the JVM.
 */
object SpaUtils {

    const val URGENT_DATE = "Today (Urgent Priority)"
    const val URGENT_SLOT = "Next Available (Within 45 Mins)"

    /** A therapist needs travel/prep time, so a slot starting sooner than this is not bookable. */
    private const val LEAD_TIME_MINUTES = 60

    /** "Today, Oct 25" / "Tomorrow, Oct 26" / "Tue, Oct 27" */
    fun dateLabel(offsetDays: Int, now: Calendar = Calendar.getInstance()): String {
        val cal = now.clone() as Calendar
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        val shortDay = SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.time)
        return when (offsetDays) {
            0 -> "Today, $shortDay"
            1 -> "Tomorrow, $shortDay"
            else -> SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(cal.time)
        }
    }

    /** Start of a slot like "02:30 PM - 03:30 PM" in minutes since midnight, or null if not a clock slot. */
    fun slotStartMinutes(slot: String): Int? {
        val start = slot.substringBefore(" - ").trim()
        val parsed: Date? = try {
            SimpleDateFormat("hh:mm a", Locale.US).parse(start)
        } catch (e: java.text.ParseException) {
            null
        }
        if (parsed == null) return null
        val cal = Calendar.getInstance()
        cal.time = parsed
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    }

    /** Slots on a future day are always available; slots today must start after now + lead time. */
    fun isSlotAvailable(slot: String, isToday: Boolean, now: Calendar = Calendar.getInstance()): Boolean {
        if (!isToday) return true
        val start = slotStartMinutes(slot) ?: return true
        val nowMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        return start >= nowMinutes + LEAD_TIME_MINUTES
    }

    fun firstAvailableSlot(
        slots: List<String>,
        isToday: Boolean,
        now: Calendar = Calendar.getInstance()
    ): String? = slots.firstOrNull { isSlotAvailable(it, isToday, now) }

    /**
     * Masks the middle of an Indian mobile number, e.g. "+91 98201 44556" -> "+91 98****4556".
     * Never reveals more than 6 of the 10 digits.
     */
    fun maskPhone(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        if (digits.length < 10) return "Number hidden"
        val national = digits.takeLast(10)
        return "+91 ${national.take(2)}****${national.takeLast(4)}"
    }

    /** Friendly label for a review timestamp. */
    fun relativeDate(timestamp: Long, now: Long = System.currentTimeMillis()): String {
        val diffMinutes = (now - timestamp) / 60_000L
        if (diffMinutes < 2) return "Just now"
        val then = Calendar.getInstance().apply { timeInMillis = timestamp }
        val today = Calendar.getInstance().apply { timeInMillis = now }
        val dayDiff = dayNumber(today) - dayNumber(then)
        return when {
            dayDiff <= 0 -> "Today"
            dayDiff == 1 -> "Yesterday"
            dayDiff < 7 -> "$dayDiff days ago"
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }

    private fun dayNumber(cal: Calendar): Int = cal.get(Calendar.YEAR) * 366 + cal.get(Calendar.DAY_OF_YEAR)

    fun isValidPhone(phone: String): Boolean = phone.count { it.isDigit() } in 10..13

    fun isValidEmail(email: String): Boolean =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
}
