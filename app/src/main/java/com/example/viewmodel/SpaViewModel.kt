package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SpaDataDefaults
import com.example.data.SpaRepository
import com.example.data.local.SpaDatabase
import com.example.model.AppNavTab
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication
import com.example.model.SpaDuration
import com.example.model.SpaService
import com.example.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class BookingDraft(
    val service: SpaService? = null,
    val duration: SpaDuration = SpaDuration.MIN_60,
    val date: String = "Today, " + SimpleDateFormat("MMM d", Locale.getDefault()).format(Date()),
    val timeSlot: String = "02:30 PM - 03:30 PM",
    val address: String = "",
    val specialNotes: String = ""
)

class SpaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SpaRepository

    init {
        val dao = SpaDatabase.getInstance(application).spaDao()
        repository = SpaRepository(dao)
        viewModelScope.launch {
            repository.seedInitialReviewsIfEmpty()
        }
    }

    val services: List<SpaService> = repository.services

    // Navigation & Global UI States
    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _selectedDuration = MutableStateFlow(SpaDuration.MIN_60)
    val selectedDuration: StateFlow<SpaDuration> = _selectedDuration.asStateFlow()

    // Real-time Urgency Slot Counter
    private val _urgentSlotsRemaining = MutableStateFlow(3)
    val urgentSlotsRemaining: StateFlow<Int> = _urgentSlotsRemaining.asStateFlow()

    // User Session (Defaults to pre-filled luxury guest, can log in / edit profile)
    private val _userSession = MutableStateFlow(
        UserSession(
            name = "Rhea Kapoor",
            phone = "+91 98201 44556",
            email = "rhea.kapoor@luxuryspa.in",
            address = "Penthouse 14B, Palm Grove Residency, Marine Drive",
            isVip = false,
            isLoggedIn = true,
            referralCode = "MOON399"
        )
    )
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    // Bookings Flow
    val bookings: StateFlow<List<Booking>> = repository.getBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Partner Applications Flow
    val partners: StateFlow<List<FemalePartnerApplication>> = repository.getPartners()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reviews Flow
    val reviews: StateFlow<List<CustomerReview>> = repository.getReviews()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SpaDataDefaults.sampleReviews)

    // Active Booking Draft / Modal
    private val _bookingDraft = MutableStateFlow<BookingDraft?>(null)
    val bookingDraft: StateFlow<BookingDraft?> = _bookingDraft.asStateFlow()

    private val _lastConfirmedBooking = MutableStateFlow<Booking?>(null)
    val lastConfirmedBooking: StateFlow<Booking?> = _lastConfirmedBooking.asStateFlow()

    // Feedback Toast / SnackBar
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Partner Registration Modal
    private val _showPartnerDialog = MutableStateFlow(false)
    val showPartnerDialog: StateFlow<Boolean> = _showPartnerDialog.asStateFlow()

    // VIP Upgrade Modal
    private val _showVipModal = MutableStateFlow(false)
    val showVipModal: StateFlow<Boolean> = _showVipModal.asStateFlow()

    // Login / Profile Edit Modal
    private val _showLoginModal = MutableStateFlow(false)
    val showLoginModal: StateFlow<Boolean> = _showLoginModal.asStateFlow()

    // Write Review Modal
    private val _showReviewModal = MutableStateFlow(false)
    val showReviewModal: StateFlow<Boolean> = _showReviewModal.asStateFlow()

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun setSelectedDuration(duration: SpaDuration) {
        _selectedDuration.value = duration
    }

    fun openBookingForService(service: SpaService, preselectedDuration: SpaDuration? = null) {
        val dur = preselectedDuration ?: _selectedDuration.value
        _bookingDraft.value = BookingDraft(
            service = service,
            duration = dur,
            address = _userSession.value.address
        )
    }

    fun openQuickUrgentBooking() {
        val firstService = services.firstOrNull() ?: return
        _bookingDraft.value = BookingDraft(
            service = firstService,
            duration = SpaDuration.MIN_60,
            date = "Today (Urgent Priority)",
            timeSlot = "Next Available (Within 45 Mins)",
            address = _userSession.value.address
        )
    }

    fun dismissBookingDraft() {
        _bookingDraft.value = null
    }

    fun dismissLastConfirmedBooking() {
        _lastConfirmedBooking.value = null
    }

    fun updateDraft(
        duration: SpaDuration? = null,
        date: String? = null,
        timeSlot: String? = null,
        address: String? = null,
        specialNotes: String? = null
    ) {
        val current = _bookingDraft.value ?: return
        _bookingDraft.value = current.copy(
            duration = duration ?: current.duration,
            date = date ?: current.date,
            timeSlot = timeSlot ?: current.timeSlot,
            address = address ?: current.address,
            specialNotes = specialNotes ?: current.specialNotes
        )
    }

    fun confirmBooking() {
        val draft = _bookingDraft.value ?: return
        val service = draft.service ?: return
        val basePrice = service.getPrice(draft.duration)
        val isVip = _userSession.value.isVip
        // VIPs get 20% privilege discount
        val finalPrice = if (isVip) (basePrice * 0.8).toInt() else basePrice

        val newBooking = Booking(
            id = "MN-" + (1000..9999).random(),
            serviceId = service.id,
            serviceTitle = service.title,
            durationMinutes = draft.duration.minutes,
            pricePaid = finalPrice,
            date = draft.date,
            timeSlot = draft.timeSlot,
            customerName = _userSession.value.name.ifBlank { "Valued Guest" },
            customerPhone = _userSession.value.phone.ifBlank { "+91 98000 00000" },
            address = draft.address.ifBlank { _userSession.value.address.ifBlank { "Home Address provided on call" } },
            specialNotes = draft.specialNotes,
            status = BookingStatus.CONFIRMED,
            freeFaceMaskIncluded = true,
            isVipBooking = isVip
        )

        viewModelScope.launch {
            repository.createBooking(newBooking)
            _lastConfirmedBooking.value = newBooking
            _bookingDraft.value = null
            if (_urgentSlotsRemaining.value > 1) {
                _urgentSlotsRemaining.value -= 1
            }
            showSnackbar("✨ Spa booked successfully! A verified female therapist is assigned.")
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
            showSnackbar("Booking $bookingId cancelled. Full refund initiated.")
        }
    }

    fun submitPartnerApplication(
        name: String,
        phone: String,
        age: Int,
        bio: String,
        experienceYears: Int,
        specialty: String,
        photoUri: String? = null
    ) {
        viewModelScope.launch {
            val app = FemalePartnerApplication(
                id = "PARTNER-" + UUID.randomUUID().toString().take(6).uppercase(),
                name = name,
                phone = phone,
                age = age,
                bio = bio,
                experienceYears = experienceYears,
                specialty = specialty,
                photoUri = photoUri,
                feePaidAmount = 500,
                status = "Application Received (₹500 Fee Paid)"
            )
            repository.registerPartner(app)
            _showPartnerDialog.value = false
            showSnackbar("🎉 Welcome $name! Your partner application has been submitted.")
        }
    }

    fun upgradeToVip() {
        val current = _userSession.value
        _userSession.value = current.copy(isVip = true)
        _showVipModal.value = false
        showSnackbar("👑 Congratulations! You are now a Moonnight V.I.P Elite Member!")
    }

    fun loginOrUpdateProfile(name: String, phone: String, email: String, address: String) {
        _userSession.value = _userSession.value.copy(
            name = name,
            phone = phone,
            email = email,
            address = address,
            isLoggedIn = true
        )
        _showLoginModal.value = false
        showSnackbar("Profile updated successfully!")
    }

    fun submitReview(rating: Int, serviceName: String, comment: String) {
        val session = _userSession.value
        val masked = if (session.phone.length >= 10) {
            val start = session.phone.take(6)
            val end = session.phone.takeLast(4)
            "$start****$end"
        } else {
            "+91 98****1234"
        }
        val review = CustomerReview(
            id = "rev_" + System.currentTimeMillis(),
            customerName = session.name.ifBlank { "Verified Guest" },
            maskedPhone = masked,
            rating = rating,
            serviceName = serviceName,
            comment = comment,
            date = "Just now",
            isVerified = true
        )
        viewModelScope.launch {
            repository.addReview(review)
            _showReviewModal.value = false
            showSnackbar("⭐ Thank you! Your review was verified and posted.")
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun setShowPartnerDialog(show: Boolean) {
        _showPartnerDialog.value = show
    }

    fun setShowVipModal(show: Boolean) {
        _showVipModal.value = show
    }

    fun setShowLoginModal(show: Boolean) {
        _showLoginModal.value = show
    }

    fun setShowReviewModal(show: Boolean) {
        _showReviewModal.value = show
    }
}
