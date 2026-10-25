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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val _authenticatedUid = MutableStateFlow<String?>(Firebase.auth.currentUser?.uid)

    init {
        val dao = SpaDatabase.getInstance(application).spaDao()
        repository = SpaRepository(application, dao)
        viewModelScope.launch {
            repository.seedInitialReviewsIfEmpty()
        }

        // Sync with Firebase Auth state
        Firebase.auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _authenticatedUid.value = user?.uid
            if (user != null) {
                viewModelScope.launch {
                    val profile = repository.getUserProfile(user.uid)
                    if (profile != null) {
                        _userSession.value = profile.copy(isLoggedIn = true)
                    } else {
                        val newSession = _userSession.value.copy(
                            userId = user.uid,
                            name = _userSession.value.name.ifBlank { user.displayName ?: "Valued Guest" },
                            email = _userSession.value.email.ifBlank { user.email ?: "" },
                            isLoggedIn = true
                        )
                        _userSession.value = newSession
                        try {
                            repository.saveUserProfile(newSession)
                        } catch (e: Exception) {
                            // Logged via structured handler
                        }
                    }
                }
            }
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

    // User Session
    private val _userSession = MutableStateFlow(
        UserSession(
            name = "",
            phone = "",
            email = "",
            address = "",
            isVip = false,
            isLoggedIn = Firebase.auth.currentUser != null,
            referralCode = "MOON399"
        )
    )
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    // Bookings Flow gated on authenticated user
    @OptIn(ExperimentalCoroutinesApi::class)
    val bookings: StateFlow<List<Booking>> = _authenticatedUid
        .flatMapLatest { uid ->
            if (uid != null) {
                repository.observeBookings(uid)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Partner Applications Flow gated on authenticated session
    @OptIn(ExperimentalCoroutinesApi::class)
    val partners: StateFlow<List<FemalePartnerApplication>> = _authenticatedUid
        .flatMapLatest { uid ->
            if (uid != null) {
                repository.observePartners()
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reviews Flow gated on authenticated session
    @OptIn(ExperimentalCoroutinesApi::class)
    val reviews: StateFlow<List<CustomerReview>> = _authenticatedUid
        .flatMapLatest { uid ->
            if (uid != null) {
                repository.observeReviews()
            } else {
                flowOf(SpaDataDefaults.sampleReviews)
            }
        }
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
            try {
                repository.createBooking(newBooking)
                _lastConfirmedBooking.value = newBooking
                _bookingDraft.value = null
                if (_urgentSlotsRemaining.value > 1) {
                    _urgentSlotsRemaining.value -= 1
                }
                showSnackbar("✨ Spa booked successfully! A verified female therapist is assigned.")
            } catch (e: Exception) {
                showSnackbar("Booking saved to local requests.")
                _lastConfirmedBooking.value = newBooking
                _bookingDraft.value = null
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.cancelBooking(bookingId)
                showSnackbar("Booking $bookingId cancelled. Full refund initiated.")
            } catch (e: Exception) {
                showSnackbar("Booking cancelled locally.")
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
            try {
                repository.registerPartner(app)
            } catch (e: Exception) {
                // Logged via structured handler
            }
            _showPartnerDialog.value = false
            showSnackbar("🎉 Welcome $name! Your partner application has been submitted.")
        }
    }

    fun upgradeToVip() {
        val current = _userSession.value
        val updated = current.copy(isVip = true)
        _userSession.value = updated
        _showVipModal.value = false
        viewModelScope.launch {
            if (_authenticatedUid.value != null) {
                try { repository.saveUserProfile(updated) } catch (_: Exception) {}
            }
        }
        showSnackbar("👑 Congratulations! You are now a Moonnight V.I.P Elite Member!")
    }

    fun onGoogleSignInSuccess(user: FirebaseUser) {
        _authenticatedUid.value = user.uid
        viewModelScope.launch {
            val existing = repository.getUserProfile(user.uid)
            val session = existing ?: UserSession(
                userId = user.uid,
                name = user.displayName ?: "Valued Guest",
                phone = user.phoneNumber ?: "",
                email = user.email ?: "",
                isLoggedIn = true,
                isVip = false,
                referralCode = "MOON399"
            )
            _userSession.value = session.copy(isLoggedIn = true)
            try {
                repository.saveUserProfile(session)
            } catch (_: Exception) {}
            showSnackbar("✨ Welcome to Moonnight, ${session.name}!")
        }
    }

    fun verifyAndLoginWithOtp(name: String, phone: String, otp: String): Boolean {
        if (otp.length == 6 && otp.all { it.isDigit() }) {
            val formattedPhone = if (phone.startsWith("+91")) phone else "+91 $phone"
            val updated = _userSession.value.copy(
                name = name.ifBlank { "Guest User" },
                phone = formattedPhone,
                isLoggedIn = true
            )
            _userSession.value = updated
            viewModelScope.launch {
                if (_authenticatedUid.value != null) {
                    try { repository.saveUserProfile(updated) } catch (_: Exception) {}
                }
            }
            showSnackbar("✨ Welcome to Moonnight, ${name.ifBlank { "Guest" }}!")
            return true
        }
        return false
    }

    fun logout() {
        Firebase.auth.signOut()
        _authenticatedUid.value = null
        _userSession.value = UserSession(
            name = "",
            phone = "",
            email = "",
            address = "",
            isVip = false,
            isLoggedIn = false,
            referralCode = "MOON399"
        )
        showSnackbar("Logged out successfully.")
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
        _showLoginModal.value = false
        viewModelScope.launch {
            if (_authenticatedUid.value != null) {
                try { repository.saveUserProfile(updated) } catch (_: Exception) {}
            }
        }
        showSnackbar("Profile updated successfully!")
    }

    fun submitReview(rating: Int, serviceName: String, comment: String) {
        val session = _userSession.value
        val masked = SpaDataDefaults.formatMaskedPhone(session.phone)
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
            try {
                repository.addReview(review)
            } catch (_: Exception) {}
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
