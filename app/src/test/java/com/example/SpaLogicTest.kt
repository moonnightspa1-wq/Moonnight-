package com.example

import com.example.data.SpaDataDefaults
import com.example.model.SpaDuration
import com.example.util.SpaUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class SpaLogicTest {

    private fun at(hour: Int, minute: Int): Calendar =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

    // ---- VIP pricing ----

    @Test
    fun vipPriceIsExactlyTwentyPercentOffForEveryServiceAndDuration() {
        SpaDataDefaults.services.forEach { service ->
            SpaDuration.entries.forEach { duration ->
                val base = service.getPrice(duration)
                assertEquals(base, service.getPrice(duration, isVip = false))
                assertEquals(base * 80 / 100, service.getPrice(duration, isVip = true))
                assertEquals(0, base * 80 % 100) // prices are whole rupees, no rounding loss
            }
        }
    }

    // ---- phone masking ----

    @Test
    fun maskPhoneNeverRevealsTheWholeNumber() {
        assertEquals("+91 98****4556", SpaUtils.maskPhone("+91 98201 44556"))
        // Old implementation returned the full number for a bare 10-digit input.
        assertEquals("+91 98****4556", SpaUtils.maskPhone("9820144556"))
        assertEquals("Number hidden", SpaUtils.maskPhone("12345"))
        assertEquals("Number hidden", SpaUtils.maskPhone(""))
    }

    // ---- time slots ----

    @Test
    fun parsesSlotStartTimes() {
        assertEquals(10 * 60, SpaUtils.slotStartMinutes("10:00 AM - 11:00 AM"))
        assertEquals(14 * 60 + 30, SpaUtils.slotStartMinutes("02:30 PM - 03:30 PM"))
        assertEquals(20 * 60 + 30, SpaUtils.slotStartMinutes("08:30 PM - 09:30 PM"))
        assertNull(SpaUtils.slotStartMinutes(SpaUtils.URGENT_SLOT))
    }

    @Test
    fun pastSlotsAreNotAvailableToday() {
        val now = at(15, 0)
        assertFalse(SpaUtils.isSlotAvailable("10:00 AM - 11:00 AM", isToday = true, now = now))
        assertFalse(SpaUtils.isSlotAvailable("02:30 PM - 03:30 PM", isToday = true, now = now))
        // needs a 60 minute lead time: 15:30 start is too soon, 16:00 is fine
        assertFalse(SpaUtils.isSlotAvailable("01:00 PM - 02:00 PM", isToday = true, now = now))
        assertTrue(SpaUtils.isSlotAvailable("04:00 PM - 05:00 PM", isToday = true, now = now))
    }

    @Test
    fun futureDaysAlwaysHaveEverySlot() {
        val lateNow = at(23, 0)
        SpaDataDefaults.timeSlots.forEach {
            assertTrue(SpaUtils.isSlotAvailable(it, isToday = false, now = lateNow))
        }
        assertNull(SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, isToday = true, now = lateNow))
        assertNotNull(SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, isToday = false, now = lateNow))
    }

    // ---- validation ----

    @Test
    fun validatesPhoneAndEmail() {
        assertTrue(SpaUtils.isValidPhone("+91 98201 44556"))
        assertTrue(SpaUtils.isValidPhone("9820144556"))
        assertFalse(SpaUtils.isValidPhone("98201"))
        assertFalse(SpaUtils.isValidPhone("abc"))
        assertTrue(SpaUtils.isValidEmail("rhea@example.com"))
        assertFalse(SpaUtils.isValidEmail("rhea@"))
        assertFalse(SpaUtils.isValidEmail("not an email"))
    }

    // ---- review dates ----

    @Test
    fun relativeDateLabels() {
        val now = System.currentTimeMillis()
        assertEquals("Just now", SpaUtils.relativeDate(now - 30_000L, now))
        assertEquals("Yesterday", SpaUtils.relativeDate(now - 24L * 60 * 60 * 1000, now))
    }
}
