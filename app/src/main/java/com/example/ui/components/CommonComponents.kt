package com.example.ui.components

import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Sanitizer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.SpaDuration
import com.example.model.UserSession
import com.example.ui.theme.BlushPinkAccent
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.DeepGold
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
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.UrgentCoral
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.WineDark

@Composable
fun MoonnightTopBar(
    userSession: UserSession,
    onVipClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header_bar"),
        color = PlumSurface,
        tonalElevation = 6.dp,
        border = BorderStroke(1.dp, PlumBorder.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Scaffold does not add status-bar insets for a custom top bar; without this the
                // logo and VIP badge are drawn underneath the system clock/notch.
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                // Brand Moon & Lotus Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PlumCard, WineDark)
                            )
                        )
                        .border(1.5.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.moonnight_icon),
                        contentDescription = "Moonnight Logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MOONNIGHT",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = ChampagneGold
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = RosePinkPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "LUXURY HOME SPA & WELLNESS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.5.sp,
                            letterSpacing = 1.2.sp,
                            color = MutedText
                        )
                    )
                }
            }

            // VIP Membership Badge Indicator
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onVipClick() }
                    .testTag("vip_badge_button"),
                color = if (userSession.isVip) GoldContainer else SoftPinkContainer,
                border = BorderStroke(
                    1.dp,
                    if (userSession.isVip) GoldAccent else RosePinkPrimary.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (userSession.isVip) Icons.Default.WorkspacePremium else Icons.Default.Star,
                        contentDescription = null,
                        tint = if (userSession.isVip) ChampagneGold else BlushPinkAccent,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (userSession.isVip) "👑 VIP ELITE" else "UPGRADE VIP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (userSession.isVip) ChampagneGold else PalePinkText
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun UrgentSlotsWidget(
    slotsRemaining: Int,
    onQuickBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("urgent_slots_widget"),
        colors = CardDefaults.cardColors(containerColor = WineDark),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(listOf(UrgentCoral, GoldAccent))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(UrgentCoral.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Urgent Slots",
                        tint = UrgentCoral,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚡ LAST-MINUTE AVAILABILITY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = UrgentCoral,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }
                    Text(
                        text = "Hurry! Only $slotsRemaining ${if (slotsRemaining == 1) "slot" else "slots"} left for today! Book now.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SoftWhite
                        )
                    )
                }
            }

            Button(
                onClick = onQuickBookClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RosePinkPrimary,
                    contentColor = SoftWhite
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("urgent_book_now_btn")
            ) {
                Text(
                    text = "Book Now",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun FreeMaskBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("free_mask_banner"),
        colors = CardDefaults.cardColors(containerColor = SoftPinkContainer),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BlushPinkAccent.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(RosePinkPrimary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = PalePinkText,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SPECIAL COMPLIMENTARY GIFT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Every massage includes a FREE Face Mask! 🎭",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
                Text(
                    text = "Infused with organic rosewater & marine collagen for instant glow.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LightGrayText,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InHomeServiceCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("in_home_service_card"),
        colors = CardDefaults.cardColors(containerColor = PlumCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PlumBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "\"I WILL COME\" SPA SERVICES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ChampagneGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Luxury Spa Brought Directly To Your Home",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No traffic, no stress. Our verified female therapists bring everything needed for an authentic 5-star spa sanctuary into your living room or suite.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LightGrayText,
                    lineHeight = 19.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // FlowRow so the three pills wrap on narrow phones instead of being squashed/clipped.
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InHomePill("🛋️ Foldable Spa Bed")
                InHomePill("🧴 Organic Warm Oils")
                InHomePill("🎵 Zen Soundscapes")
            }
        }
    }
}

@Composable
private fun InHomePill(text: String) {
    Surface(
        color = PlumSurface,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, PlumBorder.copy(alpha = 0.6f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = SoftWhite
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun DurationSelectorRow(
    selectedDuration: SpaDuration,
    onDurationSelected: (SpaDuration) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("duration_selector_row"),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SpaDuration.entries.forEach { duration ->
            val isSelected = duration == selectedDuration
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDurationSelected(duration) }
                    .testTag("duration_tab_${duration.minutes}"),
                color = if (isSelected) RosePinkPrimary else PlumCard,
                border = BorderStroke(
                    1.dp,
                    if (isSelected) BlushPinkAccent else PlumBorder
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = duration.label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) SoftWhite else LightGrayText
                        )
                    )
                    Text(
                        text = when (duration) {
                            SpaDuration.MIN_30 -> "Express Reset"
                            SpaDuration.MIN_60 -> "Signature Flow"
                            SpaDuration.MIN_90 -> "Deep Transcend"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = if (isSelected) PalePinkText else MutedText
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun HygienePrivacySection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hygiene_privacy_section"),
        colors = CardDefaults.cardColors(containerColor = PlumSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PlumBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = VerifiedGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "HYGIENE & PRIVACY ASSURANCE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HygieneBullet(
                icon = Icons.Default.Sanitizer,
                title = "Fresh Sanitized Towels & Bed Linen",
                desc = "Single-use sterilized kit unsealed right in front of you."
            )
            Spacer(modifier = Modifier.height(8.dp))
            HygieneBullet(
                icon = Icons.Default.CheckCircle,
                title = "100% Background-Checked Female Therapists",
                desc = "Government ID verified with certified ayurvedic/spa credentials."
            )
            Spacer(modifier = Modifier.height(8.dp))
            HygieneBullet(
                icon = Icons.Default.Lock,
                title = "100% Customer Data Privacy Guarantee",
                desc = "Phone numbers are masked (+91 98****1234) for private communication."
            )
        }
    }
}

@Composable
private fun HygieneBullet(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(VerifiedGreen.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VerifiedGreen,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = SoftWhite
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
}

@Composable
fun ReferAndEarnCard(
    referralCode: String,
    onCopyCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("refer_and_earn_card"),
        colors = CardDefaults.cardColors(containerColor = PlumCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "REFER & EARN PROGRAM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ChampagneGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Refer a Friend & Earn ₹399!",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Share the luxury of Moonnight with friends. Earn ₹399 spa credits straight to your wallet on their very first home booking!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LightGrayText,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PlumSurface)
                    .border(1.dp, PlumBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "YOUR REFERRAL LINK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = MutedText
                        )
                    )
                    Text(
                        text = "https://moonnight.spa/invite?code=$referralCode",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = ChampagneGold
                        )
                    )
                }

                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Moonnight Referral", "https://moonnight.spa/invite?code=$referralCode")
                        clipboard.setPrimaryClip(clip)
                        onCopyCode()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = WineDark
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("copy_referral_link_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Copy",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun VipEliteBanner(
    isVip: Boolean,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("vip_elite_banner"),
        colors = CardDefaults.cardColors(containerColor = GoldContainer),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.2.dp, GoldAccent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "👑 V.I.P ELITE MEMBERSHIP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold
                            )
                        )
                        Text(
                            text = "Annual Luxury Privilege at ₹8,000",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightGrayText
                            )
                        )
                    }
                }

                if (isVip) {
                    Surface(
                        color = GoldAccent,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "ACTIVE MEMBER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WineDark
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "• 20% Privilege Discount on all 9 spa therapies\n• Priority therapist scheduling & preferred female specialists\n• Complimentary aroma diffusers & artisanal herbal tea set\n• Zero cancellation charges anytime",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LightGrayText,
                    lineHeight = 20.sp
                )
            )

            if (!isVip) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onUpgradeClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = WineDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upgrade_vip_btn")
                ) {
                    Text(
                        text = "Upgrade to VIP Elite for ₹8,000",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
