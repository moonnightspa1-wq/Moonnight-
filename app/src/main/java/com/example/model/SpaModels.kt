package com.example.model

enum class SpaDuration(val minutes: Int, val label: String) {
    MIN_30(30, "30 Min"),
    MIN_60(60, "60 Min"),
    MIN_90(90, "90 Min")
}

data class SpaService(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val price30: Int,
    val price60: Int,
    val price90: Int,
    val benefits: List<String>,
    val badge: String? = null
) {
    fun getPrice(duration: SpaDuration): Int {
        return when (duration) {
            SpaDuration.MIN_30 -> price30
            SpaDuration.MIN_60 -> price60
            SpaDuration.MIN_90 -> price90
        }
    }
}

enum class BookingStatus(val label: String) {
    CONFIRMED("Confirmed"),
    IN_PROGRESS("Therapist En Route"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class Booking(
    val id: String,
    val serviceId: String,
    val serviceTitle: String,
    val durationMinutes: Int,
    val pricePaid: Int,
    val date: String,
    val timeSlot: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val specialNotes: String,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val freeFaceMaskIncluded: Boolean = true,
    val isVipBooking: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class FemalePartnerApplication(
    val id: String,
    val name: String,
    val phone: String,
    val age: Int,
    val bio: String,
    val experienceYears: Int,
    val specialty: String,
    val photoUri: String? = null,
    val feePaidAmount: Int = 500,
    val status: String = "Under Verification",
    val submittedAt: Long = System.currentTimeMillis()
)

data class CustomerReview(
    val id: String,
    val customerName: String,
    val maskedPhone: String,
    val rating: Int = 5,
    val serviceName: String,
    val comment: String,
    val date: String,
    val isVerified: Boolean = true
)

data class UserSession(
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val isVip: Boolean = false,
    val isLoggedIn: Boolean = false,
    val referralCode: String = "MOON399"
)

enum class AppNavTab(val title: String) {
    HOME("Home"),
    SERVICES("Services"),
    BOOKINGS("Bookings"),
    PARTNER("Partner Join"),
    PROFILE("Profile")
}
