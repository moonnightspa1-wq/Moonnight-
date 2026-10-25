package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.SpaRepository
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication
import com.example.model.UserSession
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class SpaRepositoryRuleTest : FirestoreEmulatorTestBase() {

    private lateinit var repository: SpaRepository

    @Before
    fun setUp() {
        repository = SpaRepository(firestore)
    }

    @Test
    fun unauthenticatedWrite_failsWithSecurityCheck() = runBlocking {
        auth.signOut()
        val booking = Booking(
            id = "test-booking-unauth",
            serviceId = "swedish",
            serviceTitle = "Swedish Massage",
            durationMinutes = 60,
            pricePaid = 2800,
            date = "Today",
            timeSlot = "02:00 PM",
            customerName = "Guest",
            customerPhone = "+91 98000 00000",
            address = "Mumbai",
            specialNotes = ""
        )

        try {
            repository.createBooking(booking)
            fail("Should fail when unauthenticated")
        } catch (e: IllegalStateException) {
            assertTrue(e.message?.contains("User must be authenticated") == true)
        }
    }

    @Test
    fun authenticatedUser_canCreateAndObserveBooking() = runBlocking {
        val uid = signInTestUser("guest_user@example.com")

        val booking = Booking(
            id = "test-booking-001",
            serviceId = "swedish",
            serviceTitle = "Swedish Massage",
            durationMinutes = 60,
            pricePaid = 2800,
            date = "Tomorrow",
            timeSlot = "03:00 PM - 04:00 PM",
            customerName = "Priya Sharma",
            customerPhone = "+91 98765 43210",
            address = "Bandra West, Mumbai",
            specialNotes = "Prefer lavender oil",
            status = BookingStatus.CONFIRMED,
            freeFaceMaskIncluded = true,
            isVipBooking = true
        )

        repository.createBooking(booking)

        val bookings = repository.observeBookings(uid).first()
        assertTrue(bookings.isNotEmpty())
        val found = bookings.firstOrNull { it.id == "test-booking-001" }
        assertNotNull(found)
        assertEquals("Swedish Massage", found?.serviceTitle)
        assertEquals(2800, found?.pricePaid)
    }

    @Test
    fun authenticatedUser_canRegisterPartnerApplication() = runBlocking {
        val uid = signInTestUser("therapist@example.com")

        val partner = FemalePartnerApplication(
            id = "PARTNER-TEST-1",
            name = "Kavita Rao",
            phone = "+91 98765 12345",
            age = 28,
            bio = "Certified therapist with 5 years experience in Swedish & Thai therapies.",
            experienceYears = 5,
            specialty = "Swedish & Balinese",
            feePaidAmount = 500,
            status = "Under Verification"
        )

        repository.registerPartner(partner)

        val partners = repository.observePartners().first()
        assertTrue(partners.isNotEmpty())
        val found = partners.firstOrNull { it.id == "PARTNER-TEST-1" }
        assertNotNull(found)
        assertEquals("Kavita Rao", found?.name)
        assertEquals(500, found?.feePaidAmount)
    }

    @Test
    fun authenticatedUser_canAddReviewAndReadBack() = runBlocking {
        val uid = signInTestUser("reviewer@example.com")

        val review = CustomerReview(
            id = "rev_test_001",
            customerName = "Meera S.",
            maskedPhone = "+91 98XXX XXXXX",
            rating = 5,
            serviceName = "Aromatherapy",
            comment = "Outstanding therapist! Felt like a 5-star spa in my own living room.",
            date = "Oct 25",
            isVerified = true
        )

        repository.addReview(review)

        val reviews = repository.observeReviews().first()
        assertTrue(reviews.isNotEmpty())
        val found = reviews.firstOrNull { it.id == "rev_test_001" }
        assertNotNull(found)
        assertEquals(5, found?.rating)
    }

    @Test
    fun userProfile_saveAndRetrieve() = runBlocking {
        val uid = signInTestUser("profile_user@example.com")

        val profile = UserSession(
            userId = uid,
            name = "Aisha Khan",
            phone = "+91 98765 99999",
            email = "aisha@example.com",
            address = "Juhu, Mumbai",
            isVip = true,
            isLoggedIn = true,
            referralCode = "MOON399"
        )

        repository.saveUserProfile(profile)

        val retrieved = repository.getUserProfile(uid)
        assertNotNull(retrieved)
        assertEquals("Aisha Khan", retrieved?.name)
        assertTrue(retrieved?.isVip == true)
    }
}
