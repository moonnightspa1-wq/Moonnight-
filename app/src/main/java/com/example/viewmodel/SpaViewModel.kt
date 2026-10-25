package com.example.viewmodel

import android.app.Application
import android.content.Context
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
import com.example.util.SpaUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class BookingDraft(
    val service: SpaService? = null,
    val duration: SpaDuration = SpaDuration.MIN_60,
    val date: String = SpaUtils.dateLabel(0),
    val timeSlot: String = SpaDataDefaults.timeSlots.first(),
    val address: String = "",
    val specialNotes: String = "",
    val isUrgent: Boolean = false,
    /** Validation / save error shown inside the booking dialog (snackbars would be hidden behind it). */
    val error: String? = null
)

class SpaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SpaRepository
    private val prefs = application.getSharedPreferences("moonnight_prefs", Context.MODE_PRIVATE)

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

    // Urgency slot counter (only urgent bookings use one up)
    private val _urgentSlotsRemaining = MutableStateFlow(3)
    val urgentSlotsRemaining: StateFlow<Int> = _urgentSlotsRemaining.asStateFlow()

    // User session: loaded from disk so profile edits and VIP status survive app restarts.
    private val _userSession = MutableStateFlow(loadSession())
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

    // VIP Upgrade Modal
    private val _showVipModal = MutableStateFlow(false)
    val showVipModal: StateFlow<Boolean> = _showVipModal.asStateFlow()

    // Login / Profile Edit Modal
    private val _showLoginModal = MutableStateFlow(false)
    val showLoginModal: StateFlow<Boolean> = _showLoginModal.asStateFlow()

    // Write Review Modal
    private val _showReviewModal = MutableStateFlow(false)
    val showReviewModal: StateFlow<Boolean> = _showReviewModal.asStateFlow()

    private fun loadSession(): UserSession = UserSession(
        name = prefs.getString(KEY_NAME, "") ?: "",
        phone = prefs.getString(KEY_PHONE, "") ?: "",
        email = prefs.getString(KEY_EMAIL, "") ?: "",
        address = prefs.getString(KEY_ADDRESS, "") ?: "",
        isVip = prefs.getBoolean(KEY_VIP, false),
        isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false)
    )

    private fun saveSession(session: UserSession) {
        prefs.edit()
            .putString(KEY_NAME, session.name)
            .putString(KEY_PHONE, session.phone)
            .putString(KEY_EMAIL, session.email)
            .putString(KEY_ADDRESS, session.address)
            .putBoolean(KEY_VIP, session.isVip)
            .putBoolean(KEY_LOGGED_IN, session.isLoggedIn)
            .apply()
    }

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun setSelectedDuration(duration: SpaDuration) {
        _selectedDuration.value = duration
    }

    fun openBookingForService(service: SpaService, preselectedDuration: SpaDuration? = null) {
        val dur = preselectedDuration ?: _selectedDuration.value
        // If every slot for today has already passed, default to tomorrow instead of a past time.
        val todayHasSlot = SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, true) != null
        val dayOffset = if (todayHasSlot) 0 else 1
        val slot = SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, dayOffset == 0)
            ?: SpaDataDefaults.timeSlots.first()
        _bookingDraft.value = BookingDraft(
            service = service,
            duration = dur,
            date = SpaUtils.dateLabel(dayOffset),
            timeSlot = slot,
            address = _userSession.value.address
        )
    }

    fun openQuickUrgentBooking() {
        val firstService = services.firstOrNull() ?: return
        _bookingDraft.value = BookingDraft(
            service = firstService,
            duration = SpaDuration.MIN_60,
            date = SpaUtils.URGENT_DATE,
            timeSlot = SpaUtils.URGENT_SLOT,
            address = _userSession.value.address,
            isUrgent = true
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
        val todayLabel = SpaUtils.dateLabel(0)

        var newDate = date ?: current.date
        var newSlot = timeSlot ?: current.timeSlot
        var urgent = current.isUrgent

        // Picking a concrete date or slot turns an "urgent" draft into a normal scheduled one,
        // so we never end up with e.g. "Tomorrow" + "Next Available (Within 45 Mins)".
        if (urgent && (date != null || timeSlot != null)) {
            urgent = false
            if (date == null) newDate = todayLabel
            if (timeSlot == null) {
                newSlot = SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, newDate == todayLabel)
                    ?: SpaDataDefaults.timeSlots.first()
            }
        }

        // Never keep a slot that has already passed for today.
        val isToday = newDate == todayLabel
        if (!SpaUtils.isSlotAvailable(newSlot, isToday)) {
            newSlot = SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, isToday) ?: newSlot
        }

        _bookingDraft.value = current.copy(
            duration = duration ?: current.duration,
            date = newDate,
            timeSlot = newSlot,
            address = address ?: current.address,
            specialNotes = specialNotes ?: current.specialNotes,
            isUrgent = urgent,
            error = null
        )
    }

    fun confirmBooking() {
        val draft = _bookingDraft.value ?: return
        val service = draft.service ?: return
        val session = _userSession.value

        if (session.name.isBlank() || session.phone.isBlank()) {
            _bookingDraft.value = draft.copy(error = "Please add your name and mobile number so your therapist can reach you.")
            _showLoginModal.value = true
            return
        }
        val address = draft.address.ifBlank { session.address }
        if (address.isBlank()) {
            _bookingDraft.value = draft.copy(error = "Please enter the home address for your session.")
            return
        }
        if (!draft.isUrgent && !SpaUtils.isSlotAvailable(draft.timeSlot, draft.date == SpaUtils.dateLabel(0))) {
            _bookingDraft.value = draft.copy(error = "That time slot has already passed. Please pick another one.")
            return
        }

        val isVip = session.isVip
        val newBooking = Booking(
            id = "MN-" + UUID.randomUUID().toString().take(8).uppercase(),
            serviceId = service.id,
            serviceTitle = service.title,
            durationMinutes = draft.duration.minutes,
            pricePaid = service.getPrice(draft.duration, isVip),
            date = draft.date,
            timeSlot = draft.timeSlot,
            customerName = session.name,
            customerPhone = session.phone,
            address = address,
            specialNotes = draft.specialNotes,
            status = BookingStatus.CONFIRMED,
            freeFaceMaskIncluded = true,
            isVipBooking = isVip
        )

        // Close the draft immediately (before the suspending DB write) so a double tap on
        // "Instant Confirm" cannot create two bookings.
        _bookingDraft.value = null

        viewModelScope.launch {
            try {
                repository.createBooking(newBooking)
                _lastConfirmedBooking.value = newBooking
                if (draft.isUrgent && _urgentSlotsRemaining.value > 1) {
                    _urgentSlotsRemaining.value -= 1
                }
                showSnackbar("✨ Spa booked successfully! A verified female therapist is assigned.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Saving failed: give the draft back so the user does not lose what they entered.
                _bookingDraft.value = draft.copy(error = "Sorry, we couldn't save your booking. Please try again.")
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            val cancelled = repository.cancelBooking(bookingId)
            if (cancelled) {
                showSnackbar("Booking $bookingId cancelled.")
            } else {
                showSnackbar("This booking can no longer be cancelled.")
            }
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
            showSnackbar("🎉 Welcome $name! Your partner application has been submitted.")
        }
    }

    fun upgradeToVip() {
        val updated = _userSession.value.copy(isVip = true)
        _userSession.value = updated
        saveSession(updated)
        _showVipModal.value = false
        showSnackbar("👑 Congratulations! You are now a Moonnight V.I.P Elite Member!")
    }

    fun loginOrUpdateProfile(name: String, phone: String, email: String, address: String) {
        val updated = _userSession.value.copy(
            name = name,
            phone = phone,
            email = email,
            address = address,
            isLoggedIn = true
        )
        _userSession.value = updated
        saveSession(updated)
        _showLoginModal.value = false
        showSnackbar("Profile updated successfully!")
    }

    fun submitReview(rating: Int, serviceName: String, comment: String) {
        val session = _userSession.value
        val review = CustomerReview(
            id = "rev_" + System.currentTimeMillis(),
            customerName = session.name.ifBlank { "Verified Guest" },
            maskedPhone = SpaUtils.maskPhone(session.phone),
            rating = rating,
            serviceName = serviceName,
            comment = comment,
            date = "Just now",
            isVerified = true
        )
        viewModelScope.launch {
            repository.addReview(review)
            _showReviewModal.value = false
            showSnackbar("⭐ Thank you! Your review was posted.")
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
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

    private companion object {
        const val KEY_NAME = "name"
        const val KEY_PHONE = "phone"
        const val KEY_EMAIL = "email"
        const val KEY_ADDRESS = "address"
        const val KEY_VIP = "isVip"
        const val KEY_LOGGED_IN = "isLoggedIn"
    }
}
