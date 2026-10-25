package com.example.ui.components

import com.example.util.SpaUtils
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SpaDataDefaults
import com.example.model.Booking
import com.example.model.SpaDuration
import com.example.model.UserSession
import com.example.ui.theme.BlushPinkAccent
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
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
import com.example.ui.theme.WineDark
import com.example.viewmodel.BookingDraft

@Composable
fun BookingDialog(
    draft: BookingDraft,
    userSession: UserSession,
    onDismiss: () -> Unit,
    onUpdateDraft: (SpaDuration?, String?, String?, String?, String?) -> Unit,
    onConfirm: () -> Unit
) {
    val service = draft.service ?: return
    // Local text state must NOT be keyed on the draft values: the draft updates a frame later than
    // the keystroke, and re-keying would reset the field and can drop characters when typing fast.
    var addressInput by remember { mutableStateOf(draft.address) }
    var notesInput by remember { mutableStateOf(draft.specialNotes) }

    val basePrice = service.getPrice(draft.duration)
    val isVip = userSession.isVip
    val finalPrice = service.getPrice(draft.duration, isVip)

    val todayStr = SpaUtils.dateLabel(0)
    val todayHasSlots = SpaUtils.firstAvailableSlot(SpaDataDefaults.timeSlots, true) != null
    // Don't offer "Today" once every slot for today has already passed.
    val availableDates = (if (todayHasSlots) listOf(0, 1, 2) else listOf(1, 2)).map { SpaUtils.dateLabel(it) }
    val isTodaySelected = draft.date == todayStr

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .testTag("booking_modal_sheet"),
            color = PlumSurface,
            border = BorderStroke(1.2.dp, GoldAccent.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SCHEDULE IN-HOME SPA",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = service.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_booking_dialog_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LightGrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // FREE Face Mask Callout
                Surface(
                    color = SoftPinkContainer,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, RosePinkPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = PalePinkText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Complimentary Gift: FREE Collagen Face Mask Included 🎭",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PalePinkText
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Duration Selector
                Text(
                    text = "Select Duration",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = LightGrayText,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpaDuration.entries.forEach { d ->
                        val isSelected = d == draft.duration
                        val price = service.getPrice(d)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onUpdateDraft(d, null, null, null, null) }
                                .testTag("select_duration_${d.minutes}"),
                            color = if (isSelected) RosePinkPrimary else PlumCard,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) GoldAccent else PlumBorder
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = d.label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) SoftWhite else LightGrayText
                                    )
                                )
                                Text(
                                    text = "₹$price",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ChampagneGold else ChampagneGold.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Date Picker
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Preferred Date",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = LightGrayText,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availableDates) { dateStr ->
                        val isSel = draft.date == dateStr
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onUpdateDraft(null, dateStr, null, null, null) }
                                .testTag("date_pill_$dateStr"),
                            color = if (isSel) GoldAccent else PlumCard,
                            border = BorderStroke(1.dp, if (isSel) ChampagneGold else PlumBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) WineDark else SoftWhite
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Time Slots
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Preferred Time Slot",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = LightGrayText,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SpaDataDefaults.timeSlots.chunked(2).forEach { rowSlots ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowSlots.forEach { slot ->
                                val isSel = draft.timeSlot == slot
                                val slotEnabled = SpaUtils.isSlotAvailable(slot, isTodaySelected)
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .alpha(if (slotEnabled) 1f else 0.35f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable(enabled = slotEnabled) { onUpdateDraft(null, null, slot, null, null) }
                                        .testTag("time_slot_$slot"),
                                    color = if (isSel) RosePinkPrimary else PlumCard,
                                    border = BorderStroke(1.dp, if (isSel) BlushPinkAccent else PlumBorder),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = slot,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSel) SoftWhite else LightGrayText
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Home Address
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Home Address for Service",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = LightGrayText,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = addressInput,
                    onValueChange = {
                        addressInput = it
                        onUpdateDraft(null, null, null, it, null)
                    },
                    placeholder = { Text("Flat/Villa #, Building, Street, Landmark", color = MutedText, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_address_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Special Instructions
                OutlinedTextField(
                    value = notesInput,
                    onValueChange = {
                        notesInput = it
                        onUpdateDraft(null, null, null, null, it)
                    },
                    label = { Text("Special Requests (e.g., Focus on lower back / Quiet session)", color = MutedText, fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_notes_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Pricing Summary
                Surface(
                    color = PlumCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, PlumBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${service.title} (${draft.duration.label})",
                                style = MaterialTheme.typography.bodyMedium.copy(color = LightGrayText)
                            )
                            Text(
                                text = "₹$basePrice",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                        }

                        if (isVip) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "👑 VIP Elite 20% Privilege Discount",
                                    style = MaterialTheme.typography.labelSmall.copy(color = ChampagneGold)
                                )
                                Text(
                                    text = "-₹${basePrice - finalPrice}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ChampagneGold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Collagen Face Mask (Bonus Gift)",
                                style = MaterialTheme.typography.labelSmall.copy(color = BlushPinkAccent)
                            )
                            Text(
                                text = "FREE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlushPinkAccent
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(PlumBorder)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Amount Due",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                            Text(
                                text = "₹$finalPrice",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ChampagneGold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (draft.error != null) {
                    Text(
                        text = draft.error,
                        color = UrgentCoral,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .testTag("booking_error_text")
                    )
                }

                // Instant Confirm Button
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_booking_submit_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Instant Confirm • ₹$finalPrice",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🔒 Free cancellation anytime before therapist departs. Zero advance fee required.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedText,
                        fontSize = 10.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BookingConfirmationDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onViewBookings: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("booking_confirmation_modal"),
            color = PlumSurface,
            border = BorderStroke(1.2.dp, GoldAccent)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.5.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "BOOKING CONFIRMED!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold,
                        letterSpacing = 1.sp
                    )
                )

                Text(
                    text = "Booking Reference: #${booking.id}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedText,
                        letterSpacing = 0.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PlumCard),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, PlumBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = booking.serviceTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                        Text(
                            text = "${booking.durationMinutes} Minutes • ₹${booking.pricePaid}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "📅 ${booking.date} at ${booking.timeSlot}",
                            style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText)
                        )
                        Text(
                            text = "📍 ${booking.address}",
                            style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎭 Free Rosewater Face Mask Pack Included",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BlushPinkAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Our verified female therapist has accepted your booking and is preparing a sterile equipment kit. You will receive an SMS update prior to arrival.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LightGrayText,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 17.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onViewBookings()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("view_in_bookings_btn")
                ) {
                    Text(
                        text = "View in My Bookings",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
