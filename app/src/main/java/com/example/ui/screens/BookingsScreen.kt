package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.ui.theme.BlushPinkAccent
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGrayText
import com.example.ui.theme.MutedText
import com.example.ui.theme.PalePinkText
import com.example.ui.theme.PlumBorder
import com.example.ui.theme.PlumCard
import com.example.ui.theme.PlumSurface
import com.example.ui.theme.RosePinkPrimary
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.UrgentCoral
import com.example.ui.theme.VerifiedGreen

@Composable
fun BookingsScreen(
    bookings: List<Booking>,
    onCancelBooking: (String) -> Unit,
    onExploreServices: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bookingToCancel by remember { mutableStateOf<Booking?>(null) }

    if (bookingToCancel != null) {
        AlertDialog(
            onDismissRequest = { bookingToCancel = null },
            title = {
                Text(
                    text = "Cancel Booking?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to cancel your appointment for ${bookingToCancel!!.serviceTitle} on ${bookingToCancel!!.date}? No advance payment was taken, so you won't be charged.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = LightGrayText)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelBooking(bookingToCancel!!.id)
                        bookingToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UrgentCoral,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_cancel_booking_btn")
                ) {
                    Text("Yes, Cancel Booking")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { bookingToCancel = null },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, PlumBorder)
                ) {
                    Text("Keep Booking", color = LightGrayText)
                }
            },
            containerColor = PlumSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("bookings_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Text(
                    text = "APPOINTMENT HISTORY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "My Spa Bookings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        if (bookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .testTag("empty_bookings_card"),
                    colors = CardDefaults.cardColors(containerColor = PlumCard),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, PlumBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SoftPinkContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = RosePinkPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "No Active Spa Bookings",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                        Text(
                            text = "Schedule a tranquil home massage session. Our verified female therapists arrive on time with a complete luxury spa setup.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightGrayText,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onExploreServices,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RosePinkPrimary,
                                contentColor = SoftWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("explore_services_empty_btn")
                        ) {
                            Text("Explore Luxury Menu", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(bookings, key = { it.id }) { booking ->
                BookingCard(
                    booking = booking,
                    onCancelClick = { bookingToCancel = booking }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: Booking,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCancelled = booking.status == BookingStatus.CANCELLED

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("booking_item_${booking.id}"),
        colors = CardDefaults.cardColors(containerColor = PlumCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (isCancelled) PlumBorder else GoldAccent.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: ID & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ref #${booking.id}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold
                    )
                )

                Surface(
                    color = when (booking.status) {
                        BookingStatus.CONFIRMED -> VerifiedGreen.copy(alpha = 0.2f)
                        BookingStatus.IN_PROGRESS -> GoldAccent.copy(alpha = 0.2f)
                        BookingStatus.COMPLETED -> PlumSurface
                        BookingStatus.CANCELLED -> UrgentCoral.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        0.8.dp,
                        when (booking.status) {
                            BookingStatus.CONFIRMED -> VerifiedGreen
                            BookingStatus.IN_PROGRESS -> GoldAccent
                            BookingStatus.COMPLETED -> LightGrayText
                            BookingStatus.CANCELLED -> UrgentCoral
                        }
                    )
                ) {
                    Text(
                        text = booking.status.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (booking.status) {
                                BookingStatus.CONFIRMED -> VerifiedGreen
                                BookingStatus.IN_PROGRESS -> ChampagneGold
                                BookingStatus.COMPLETED -> LightGrayText
                                BookingStatus.CANCELLED -> UrgentCoral
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Service Title & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.serviceTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )

                Text(
                    text = "₹${booking.pricePaid}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold
                    )
                )
            }

            Text(
                text = "${booking.durationMinutes} Minutes Session",
                style = MaterialTheme.typography.labelSmall.copy(color = LightGrayText)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Schedule info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = RosePinkPrimary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = booking.date,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SoftWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = RosePinkPrimary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = booking.timeSlot,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SoftWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Address info
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = ChampagneGold,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = booking.address,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LightGrayText,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Free Face Mask pill
            Surface(
                color = SoftPinkContainer,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.7.dp, BlushPinkAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = PalePinkText,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "FREE Rose Face Mask Included 🎭",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PalePinkText
                        )
                    )
                }
            }

            // Only a still-confirmed booking can be cancelled ("free cancellation before the
            // therapist departs"); it used to be offered for every non-cancelled status.
            if (booking.status == BookingStatus.CONFIRMED) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = UrgentCoral
                        ),
                        border = BorderStroke(1.dp, UrgentCoral.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("cancel_booking_action_${booking.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Cancel Booking",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
