package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.SpaService
import com.example.model.UserSession
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldStar
import com.example.ui.theme.LightGrayText
import com.example.ui.theme.MutedText
import com.example.ui.theme.PalePinkText
import com.example.ui.theme.PlumBorder
import com.example.ui.theme.PlumCard
import com.example.ui.theme.PlumSurface
import com.example.ui.theme.RosePinkPrimary
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.UrgentCoral
import com.example.ui.theme.WineDark

@Composable
fun LoginProfileDialog(
    userSession: UserSession,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, email: String, address: String) -> Unit
) {
    var name by remember { mutableStateOf(userSession.name) }
    var phone by remember { mutableStateOf(userSession.phone) }
    var email by remember { mutableStateOf(userSession.email) }
    var address by remember { mutableStateOf(userSession.address) }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .testTag("login_profile_modal"),
            color = PlumSurface,
            border = BorderStroke(1.dp, RosePinkPrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MOONNIGHT ACCOUNT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = if (userSession.isLoggedIn) "Edit Guest Profile" else "Sign In / Register",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = LightGrayText)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Full Name *",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g. Rhea Kapoor", color = MutedText, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("login_name_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mobile Number * (Used for home arrival confirmation)",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("+91 98201 44556", color = MutedText, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("login_phone_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Email Address (Optional)",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("e.g. guest@moonnight.in", color = MutedText, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Saved Home Address",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("Flat/Villa #, Society, Landmark", color = MutedText, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("login_address_input"),
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

                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error!!,
                        color = UrgentCoral,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank()) {
                            error = "Please enter both your name and phone number."
                        } else {
                            onSave(name.trim(), phone.trim(), email.trim(), address.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_profile_btn")
                ) {
                    Text(
                        text = if (userSession.isLoggedIn) "Save Profile Details" else "Continue as Guest",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun VipUpgradeDialog(
    isVip: Boolean,
    onDismiss: () -> Unit,
    onUpgrade: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("vip_upgrade_modal"),
            color = PlumSurface,
            border = BorderStroke(1.5.dp, GoldAccent)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.5.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "MOONNIGHT V.I.P ELITE",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold,
                        letterSpacing = 1.5.sp
                    )
                )

                Text(
                    text = "Exclusive Annual Luxury Pass • ₹8,000",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PalePinkText,
                        fontWeight = FontWeight.SemiBold
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
                        VipPerkItem("💎 20% Privilege Discount", "Automatically discounted on all 9 luxury home massage services.")
                        Spacer(modifier = Modifier.height(8.dp))
                        VipPerkItem("✨ Top-Tier Female Therapists", "Guaranteed priority allocation of senior certified specialists.")
                        Spacer(modifier = Modifier.height(8.dp))
                        VipPerkItem("🎁 Luxury Welcome Gift Kit", "Complimentary ultrasonic aroma diffuser & exotic essential oil set.")
                        Spacer(modifier = Modifier.height(8.dp))
                        VipPerkItem("⏰ Flexible Rescheduling", "Zero cancellation or rescheduling fees, even within 2 hours.")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isVip) {
                    Surface(
                        color = GoldAccent,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "YOU ARE ALREADY A V.I.P ELITE MEMBER",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WineDark
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onUpgrade,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            contentColor = WineDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_vip_upgrade_btn")
                    ) {
                        Text(
                            text = "Upgrade Instantly for ₹8,000",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                IconButton(onClick = onDismiss) {
                    Text(
                        text = "Close",
                        style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                    )
                }
            }
        }
    }
}

@Composable
private fun VipPerkItem(title: String, desc: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = ChampagneGold
            )
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall.copy(
                color = LightGrayText,
                fontSize = 11.5.sp
            )
        )
    }
}

@Composable
fun WriteReviewDialog(
    services: List<SpaService>,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, serviceName: String, comment: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var selectedService by remember { mutableStateOf(services.firstOrNull()?.title ?: "Swedish Massage") }
    var comment by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .testTag("write_review_modal"),
            color = PlumSurface,
            border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SHARE YOUR EXPERIENCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Write a Verified Review",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = LightGrayText)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Star Rating Picker
                Text(
                    text = "Rating (1 to 5 Stars)",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("star_rating_picker")
                ) {
                    for (i in 1..5) {
                        val isFilled = i <= rating
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "$i stars",
                            tint = if (isFilled) GoldStar else MutedText,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = i }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Massage Treatment",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = selectedService,
                    onValueChange = { selectedService = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Your Feedback & Experience",
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("How was the therapist, hygiene, massage technique, and complimentary face mask?", color = MutedText, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("review_comment_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RosePinkPrimary,
                        unfocusedBorderColor = PlumBorder,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedContainerColor = PlumCard,
                        unfocusedContainerColor = PlumCard
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Privacy Note: Your phone number is masked (e.g. +91 98****1234)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ChampagneGold,
                            fontSize = 10.5.sp
                        )
                    )
                }

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = error!!, color = UrgentCoral, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (comment.isBlank()) {
                            error = "Please share a few words about your experience."
                        } else {
                            onSubmit(rating, selectedService, comment.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_review_btn")
                ) {
                    Text(
                        text = "Submit Verified Review",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
