package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SafetyCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FemalePartnerApplication
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

@Composable
fun PartnerScreen(
    applications: List<FemalePartnerApplication>,
    onSubmitApplication: (name: String, phone: String, age: Int, bio: String, exp: Int, specialty: String, photoUri: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var expYearsStr by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("Swedish & Deep Tissue Massage") }
    var photoSelected by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("partner_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp)
    ) {
        // Hero / Pitch
        item {
            Column {
                Text(
                    text = "FEMALE THERAPIST NETWORK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Become a Partner at ₹500",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
                Text(
                    text = "Join India's premier luxury in-home spa brand. Work with complete dignity, verified clients, and earn high income on your own schedule.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LightGrayText,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Perks Cards
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("partner_perks_card"),
                colors = CardDefaults.cardColors(containerColor = GoldContainer),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GoldAccent)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PARTNER BENEFITS & SAFETY",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Earn ₹40,000 to ₹75,000/month with direct bank deposits\n• 100% Female Therapist Safety Guarantee & client identity verification\n• Choose your preferred work zones & hours\n• Welcome kit provided: Foldable lightweight bed & sterile linen",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LightGrayText,
                            lineHeight = 19.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Registration Form Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("partner_registration_form"),
                colors = CardDefaults.cardColors(containerColor = PlumCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.2.dp, RosePinkPrimary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "ONBOARDING REGISTRATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ChampagneGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Therapist Application",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                        }

                        Surface(
                            color = SoftPinkContainer,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, RosePinkPrimary)
                        ) {
                            Text(
                                text = "Fee: ₹500",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PalePinkText
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Photo Upload UI Placeholder
                    Text(
                        text = "Profile Photo Upload *",
                        style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { photoSelected = !photoSelected }
                            .testTag("partner_photo_upload_placeholder"),
                        color = PlumSurface,
                        border = BorderStroke(
                            1.dp,
                            if (photoSelected) VerifiedGreen else PlumBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (photoSelected) VerifiedGreen.copy(alpha = 0.2f) else SoftPinkContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (photoSelected) Icons.Default.CheckCircle else Icons.Default.AddAPhoto,
                                    contentDescription = "Upload Photo",
                                    tint = if (photoSelected) VerifiedGreen else RosePinkPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (photoSelected) "Photo Attached: therapist_portrait.jpg" else "Tap to Select Profile Photo",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (photoSelected) VerifiedGreen else SoftWhite
                                    )
                                )
                                Text(
                                    text = "Professional portrait with clean attire. Displayed to confirmed clients.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = LightGrayText
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name
                    Text(text = "Full Legal Name *", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("e.g. Priya Sharma", color = MutedText, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("partner_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RosePinkPrimary,
                            unfocusedBorderColor = PlumBorder,
                            focusedTextColor = SoftWhite,
                            unfocusedTextColor = SoftWhite,
                            focusedContainerColor = PlumSurface,
                            unfocusedContainerColor = PlumSurface
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone & Age Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1.3f)) {
                            Text(text = "Phone Number *", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                placeholder = { Text("+91 98765 43210", color = MutedText, fontSize = 13.sp) },
                                modifier = Modifier.fillMaxWidth().testTag("partner_phone_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RosePinkPrimary,
                                    unfocusedBorderColor = PlumBorder,
                                    focusedTextColor = SoftWhite,
                                    unfocusedTextColor = SoftWhite,
                                    focusedContainerColor = PlumSurface,
                                    unfocusedContainerColor = PlumSurface
                                ),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(0.7f)) {
                            Text(text = "Age *", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = ageStr,
                                onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) ageStr = it },
                                placeholder = { Text("26", color = MutedText, fontSize = 13.sp) },
                                modifier = Modifier.fillMaxWidth().testTag("partner_age_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RosePinkPrimary,
                                    unfocusedBorderColor = PlumBorder,
                                    focusedTextColor = SoftWhite,
                                    unfocusedTextColor = SoftWhite,
                                    focusedContainerColor = PlumSurface,
                                    unfocusedContainerColor = PlumSurface
                                ),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Working Experience
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(0.8f)) {
                            Text(text = "Experience *", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = expYearsStr,
                                onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) expYearsStr = it },
                                placeholder = { Text("Years (e.g. 4)", color = MutedText, fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth().testTag("partner_exp_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RosePinkPrimary,
                                    unfocusedBorderColor = PlumBorder,
                                    focusedTextColor = SoftWhite,
                                    unfocusedTextColor = SoftWhite,
                                    focusedContainerColor = PlumSurface,
                                    unfocusedContainerColor = PlumSurface
                                ),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(text = "Primary Specialty *", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = specialty,
                                onValueChange = { specialty = it },
                                placeholder = { Text("e.g. Ayurvedic / Deep Tissue", color = MutedText, fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth().testTag("partner_specialty_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RosePinkPrimary,
                                    unfocusedBorderColor = PlumBorder,
                                    focusedTextColor = SoftWhite,
                                    unfocusedTextColor = SoftWhite,
                                    focusedContainerColor = PlumSurface,
                                    unfocusedContainerColor = PlumSurface
                                ),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio / About
                    Text(text = "Bio & Professional Background", style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        placeholder = { Text("Tell us about your spa certifications, past salon/hotel experience, and massage technique...", color = MutedText, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("partner_bio_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RosePinkPrimary,
                            unfocusedBorderColor = PlumBorder,
                            focusedTextColor = SoftWhite,
                            unfocusedTextColor = SoftWhite,
                            focusedContainerColor = PlumSurface,
                            unfocusedContainerColor = PlumSurface
                        ),
                        maxLines = 3
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage!!, color = UrgentCoral, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button with ₹500 fee
                    Button(
                        onClick = {
                            val age = ageStr.toIntOrNull() ?: 0
                            val exp = expYearsStr.toIntOrNull() ?: 0
                            if (name.isBlank() || phone.isBlank() || age < 18) {
                                errorMessage = "Please enter valid legal name, phone number, and age (18+)."
                            } else {
                                errorMessage = null
                                onSubmitApplication(
                                    name.trim(),
                                    phone.trim(),
                                    age,
                                    bio.ifBlank { "Certified female therapist with $exp years spa experience." },
                                    exp,
                                    specialty.trim(),
                                    if (photoSelected) "content://photos/therapist_avatar.jpg" else null
                                )
                                name = ""
                                phone = ""
                                ageStr = ""
                                bio = ""
                                expYearsStr = ""
                                photoSelected = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RosePinkPrimary,
                            contentColor = SoftWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_partner_application_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Register as Partner • Pay ₹500 Fee",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🔒 Payment is 100% refundable if verification is not approved. Encrypted with 256-bit SSL.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedText,
                            fontSize = 10.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Existing Applications List
        if (applications.isNotEmpty()) {
            item {
                Text(
                    text = "YOUR SUBMITTED APPLICATIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(applications) { app ->
                PartnerApplicationCard(app = app)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PartnerApplicationCard(app: FemalePartnerApplication) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("partner_app_card_${app.id}"),
        colors = CardDefaults.cardColors(containerColor = PlumSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, PlumBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )

                Surface(
                    color = VerifiedGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, VerifiedGreen)
                ) {
                    Text(
                        text = app.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VerifiedGreen,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${app.specialty} • ${app.experienceYears} Years Experience • Age ${app.age}",
                style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText)
            )
            Text(
                text = "Contact: ${app.phone} • Onboarding Fee: ₹${app.feePaidAmount} (Received)",
                style = MaterialTheme.typography.labelSmall.copy(color = ChampagneGold)
            )
        }
    }
}
