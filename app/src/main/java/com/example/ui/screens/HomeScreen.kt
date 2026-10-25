package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppNavTab
import com.example.model.CustomerReview
import com.example.model.SpaDuration
import com.example.model.SpaService
import com.example.model.UserSession
import com.example.ui.components.DurationSelectorRow
import com.example.ui.components.FreeMaskBanner
import com.example.ui.components.HygienePrivacySection
import com.example.ui.components.InHomeServiceCard
import com.example.ui.components.ReferAndEarnCard
import com.example.ui.components.UrgentSlotsWidget
import com.example.ui.components.VipEliteBanner
import com.example.ui.theme.BlushPinkAccent
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
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.WineDark

@Composable
fun HomeScreen(
    userSession: UserSession,
    urgentSlotsRemaining: Int,
    selectedDuration: SpaDuration,
    services: List<SpaService>,
    reviews: List<CustomerReview>,
    onDurationSelected: (SpaDuration) -> Unit,
    onQuickUrgentBook: () -> Unit,
    onBookService: (SpaService) -> Unit,
    onSeeAllServices: () -> Unit,
    onUpgradeVip: () -> Unit,
    onCopyReferral: () -> Unit,
    onWriteReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Urgent Slots Widget
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                UrgentSlotsWidget(
                    slotsRemaining = urgentSlotsRemaining,
                    onQuickBookClick = onQuickUrgentBook
                )
            }
        }

        // 2. Hero Banner with generated spa visual
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.2.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .testTag("hero_banner_card")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_spa_hero),
                    contentDescription = "Moonnight Luxury Spa Sanctuary",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    contentScale = ContentScale.Crop
                )

                // Dark Plum/Gold Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    WineDark.copy(alpha = 0.35f),
                                    WineDark.copy(alpha = 0.75f),
                                    WineDark.copy(alpha = 0.98f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Surface(
                        color = GoldContainer,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Text(
                            text = "ON-DEMAND 5-STAR IN-HOME THERAPY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Luxury & Relaxation at Your Doorstep",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Certified female therapists arrive equipped with full massage table, organic oils & sterile linen.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LightGrayText,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // 3. Free Face Mask Banner
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                FreeMaskBanner()
            }
        }

        // 4. "I Will Come" In-Home Spa Experience Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                InHomeServiceCard()
            }
        }

        // 5. Treatment Duration Selector Matrix
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LUXURY SPA MENU",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Featured Therapies",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                    }

                    Text(
                        text = "View All (9)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = RosePinkPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable { onSeeAllServices() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                DurationSelectorRow(
                    selectedDuration = selectedDuration,
                    onDurationSelected = onDurationSelected
                )
            }
        }

        // 6. Featured Services List
        items(services.take(4)) { service ->
            HomeServiceItem(
                service = service,
                selectedDuration = selectedDuration,
                isVip = userSession.isVip,
                onBookClick = { onBookService(service) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 7. Refer & Earn Program
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ReferAndEarnCard(
                    referralCode = userSession.referralCode,
                    onCopyCode = onCopyReferral
                )
            }
        }

        // 8. Hygiene & Privacy Assurance
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                HygienePrivacySection()
            }
        }

        // 9. Verified Customer Reviews Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TRUSTED BY 10,000+ CLIENTS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Verified Reviews",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = VerifiedGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = VerifiedGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "4.9 ★",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = VerifiedGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = onWriteReview,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, RosePinkPrimary.copy(alpha = 0.6f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("write_review_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            tint = RosePinkPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Write Review",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RosePinkPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                reviews.forEach { rev ->
                    ReviewCard(review = rev)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // 10. VIP Elite Banner
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                VipEliteBanner(
                    isVip = userSession.isVip,
                    onUpgradeClick = onUpgradeVip
                )
            }
        }
    }
}

@Composable
fun HomeServiceItem(
    service: SpaService,
    selectedDuration: SpaDuration,
    isVip: Boolean,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val basePrice = service.getPrice(selectedDuration)
    val displayPrice = if (isVip) (basePrice * 0.8).toInt() else basePrice

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("service_item_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = PlumCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PlumBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (service.badge != null) {
                        Surface(
                            color = SoftPinkContainer,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.8.dp, RosePinkPrimary.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = service.badge,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PalePinkText
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftWhite
                        )
                    )
                    Text(
                        text = service.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ChampagneGold
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹$displayPrice",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    )
                    Text(
                        text = selectedDuration.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LightGrayText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LightGrayText,
                    lineHeight = 17.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎭 Free Face Mask",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BlushPinkAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    if (isVip) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• VIP 20% Off",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("book_service_btn_${service.id}")
                ) {
                    Text(
                        text = "Book Now",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewCard(
    review: CustomerReview,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("review_card_${review.id}"),
        colors = CardDefaults.cardColors(containerColor = PlumSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, PlumBorder.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(SoftPinkContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.customerName.take(1),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PalePinkText
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = review.customerName,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Client",
                                tint = VerifiedGreen,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = "${review.maskedPhone} • ${review.date}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = MutedText
                            )
                        )
                    }
                }

                Row {
                    for (i in 1..review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldStar,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = review.serviceName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = ChampagneGold
                )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LightGrayText,
                    lineHeight = 17.sp
                )
            )
        }
    }
}
