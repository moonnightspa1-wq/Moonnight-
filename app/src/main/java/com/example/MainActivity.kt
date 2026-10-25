package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import com.example.ui.components.signOut
import com.example.model.AppNavTab
import com.example.ui.components.BookingConfirmationDialog
import com.example.ui.components.BookingDialog
import com.example.ui.components.LoginProfileDialog
import com.example.ui.components.MoonnightBottomNav
import com.example.ui.components.MoonnightTopBar
import com.example.ui.components.VipUpgradeDialog
import com.example.ui.components.WriteReviewDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookingsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PartnerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.MoonnightTheme
import com.example.ui.theme.PlumBackground
import com.example.viewmodel.SpaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SpaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoonnightTheme {
                MoonnightApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MoonnightApp(viewModel: SpaViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val selectedDuration by viewModel.selectedDuration.collectAsStateWithLifecycle()
    val urgentSlotsRemaining by viewModel.urgentSlotsRemaining.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val partners by viewModel.partners.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val bookingDraft by viewModel.bookingDraft.collectAsStateWithLifecycle()
    val lastConfirmedBooking by viewModel.lastConfirmedBooking.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val showVipModal by viewModel.showVipModal.collectAsStateWithLifecycle()
    val showLoginModal by viewModel.showLoginModal.collectAsStateWithLifecycle()
    val showReviewModal by viewModel.showReviewModal.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Authentication Gate: Require authentication on fresh app startup
    if (!userSession.isLoggedIn) {
        AuthScreen(
            onGoogleAuthenticated = { user ->
                viewModel.onGoogleSignInSuccess(user)
            },
            onAuthenticated = { name, phone, otp ->
                viewModel.verifyAndLoginWithOtp(name, phone, otp)
            }
        )
        return
    }

    // BackHandler per guidelines: pop back to Home if in another tab
    if (currentTab != AppNavTab.HOME) {
        BackHandler {
            viewModel.setTab(AppNavTab.HOME)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlumBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    MoonnightTopBar(
                        userSession = userSession,
                        onVipClick = { viewModel.setShowVipModal(true) },
                        onProfileClick = { viewModel.setShowLoginModal(true) }
                    )
                },
                bottomBar = {
                    MoonnightBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.setTab(it) }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = PlumBackground
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        AppNavTab.HOME -> {
                            HomeScreen(
                                userSession = userSession,
                                urgentSlotsRemaining = urgentSlotsRemaining,
                                selectedDuration = selectedDuration,
                                services = viewModel.services,
                                reviews = reviews,
                                onDurationSelected = { viewModel.setSelectedDuration(it) },
                                onQuickUrgentBook = { viewModel.openQuickUrgentBooking() },
                                onBookService = { service -> viewModel.openBookingForService(service) },
                                onSeeAllServices = { viewModel.setTab(AppNavTab.SERVICES) },
                                onUpgradeVip = { viewModel.setShowVipModal(true) },
                                onCopyReferral = {
                                    viewModel.showSnackbar("Copied referral link: https://moonnight.spa/invite?code=${userSession.referralCode}")
                                },
                                onWriteReview = { viewModel.setShowReviewModal(true) }
                            )
                        }

                        AppNavTab.SERVICES -> {
                            ServicesScreen(
                                services = viewModel.services,
                                selectedDuration = selectedDuration,
                                userSession = userSession,
                                onDurationSelected = { viewModel.setSelectedDuration(it) },
                                onBookService = { service, dur -> viewModel.openBookingForService(service, dur) }
                            )
                        }

                        AppNavTab.BOOKINGS -> {
                            BookingsScreen(
                                bookings = bookings,
                                onCancelBooking = { viewModel.cancelBooking(it) },
                                onExploreServices = { viewModel.setTab(AppNavTab.SERVICES) }
                            )
                        }

                        AppNavTab.PARTNER -> {
                            PartnerScreen(
                                applications = partners,
                                onSubmitApplication = { name, phone, age, bio, exp, spec, photo ->
                                    viewModel.submitPartnerApplication(name, phone, age, bio, exp, spec, photo)
                                }
                            )
                        }

                        AppNavTab.PROFILE -> {
                            ProfileScreen(
                                userSession = userSession,
                                onEditProfile = { viewModel.setShowLoginModal(true) },
                                onUpgradeVip = { viewModel.setShowVipModal(true) },
                                onCopyReferral = {
                                    viewModel.showSnackbar("Copied referral link: https://moonnight.spa/invite?code=${userSession.referralCode}")
                                },
                                onLogout = {
                                    signOut(
                                        context = context,
                                        credentialManager = credentialManager,
                                        onSignOutComplete = { viewModel.logout() },
                                        scope = coroutineScope
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Booking Dialog Modal
    bookingDraft?.let { draft ->
        BookingDialog(
            draft = draft,
            userSession = userSession,
            onDismiss = { viewModel.dismissBookingDraft() },
            onUpdateDraft = { dur, date, slot, addr, notes ->
                viewModel.updateDraft(dur, date, slot, addr, notes)
            },
            onConfirm = { viewModel.confirmBooking() }
        )
    }

    // Confirmation Receipt Modal
    lastConfirmedBooking?.let { booking ->
        BookingConfirmationDialog(
            booking = booking,
            onDismiss = { viewModel.dismissLastConfirmedBooking() },
            onViewBookings = { viewModel.setTab(AppNavTab.BOOKINGS) }
        )
    }

    // VIP Upgrade Modal
    if (showVipModal) {
        VipUpgradeDialog(
            isVip = userSession.isVip,
            onDismiss = { viewModel.setShowVipModal(false) },
            onUpgrade = { viewModel.upgradeToVip() }
        )
    }

    // Login & Profile Edit Modal
    if (showLoginModal) {
        LoginProfileDialog(
            userSession = userSession,
            onDismiss = { viewModel.setShowLoginModal(false) },
            onSave = { name, phone, email, address ->
                viewModel.loginOrUpdateProfile(name, phone, email, address)
            }
        )
    }

    // Write Review Modal
    if (showReviewModal) {
        WriteReviewDialog(
            services = viewModel.services,
            onDismiss = { viewModel.setShowReviewModal(false) },
            onSubmit = { rating, sName, comment ->
                viewModel.submitReview(rating, sName, comment)
            }
        )
    }
}
