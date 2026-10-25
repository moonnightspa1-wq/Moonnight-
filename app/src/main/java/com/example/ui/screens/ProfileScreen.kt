package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserSession
import com.example.ui.components.HygienePrivacySection
import com.example.ui.components.ReferAndEarnCard
import com.example.ui.components.VipEliteBanner
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
fun ProfileScreen(
    userSession: UserSession,
    onEditProfile: () -> Unit,
    onUpgradeVip: () -> Unit,
    onCopyReferral: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Text(
                    text = "GUEST PROFILE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Account & Preferences",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // User Identity Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card"),
                colors = CardDefaults.cardColors(containerColor = PlumCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.2.dp, PlumBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SoftPinkContainer)
                                    .border(1.5.dp, RosePinkPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userSession.name.take(1).ifBlank { "M" },
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PalePinkText
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = userSession.name.ifBlank { "Luxury Guest" },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SoftWhite
                                    )
                                )
                                Text(
                                    text = userSession.phone.ifBlank { "+91 98201 44556" },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = LightGrayText
                                    )
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onEditProfile,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, RosePinkPrimary.copy(alpha = 0.6f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("edit_profile_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = RosePinkPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", color = RosePinkPrimary, style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (userSession.email.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MutedText, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = userSession.email, style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    if (userSession.address.isNotBlank()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = ChampagneGold, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = userSession.address, style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText), maxLines = 2)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // VIP Membership Banner
        item {
            VipEliteBanner(
                isVip = userSession.isVip,
                onUpgradeClick = onUpgradeVip
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Refer & Earn Card
        item {
            ReferAndEarnCard(
                referralCode = userSession.referralCode,
                onCopyCode = onCopyReferral
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Hygiene & Privacy Assurance
        item {
            HygienePrivacySection()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // VIP Concierge Help Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("concierge_card"),
                colors = CardDefaults.cardColors(containerColor = PlumSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, PlumBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = ChampagneGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "24/7 LUXURY SPA CONCIERGE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Need special accommodations, couple therapies, or custom event packages? Our spa managers are on standby.",
                        style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText, lineHeight = 17.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "📞 VIP Concierge Hotline: +91 800-MOON-SPA (Toll-Free)",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            OutlinedButton(
                onClick = onLogout,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = UrgentCoral),
                border = BorderStroke(1.dp, UrgentCoral.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_btn")
            ) {
                Text("Log Out / Change Account", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
