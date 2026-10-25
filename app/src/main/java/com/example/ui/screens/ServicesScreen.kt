package com.example.ui.screens

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.model.SpaDuration
import com.example.model.SpaService
import com.example.model.UserSession
import com.example.ui.components.DurationSelectorRow
import com.example.ui.components.FreeMaskBanner
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

@Composable
fun ServicesScreen(
    services: List<SpaService>,
    selectedDuration: SpaDuration,
    userSession: UserSession,
    onDurationSelected: (SpaDuration) -> Unit,
    onBookService: (SpaService, SpaDuration) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("services_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Text(
                    text = "CURATED WELLNESS MENU",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ChampagneGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Luxury Spa & Pricing Matrix",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = SoftWhite
                    )
                )
                Text(
                    text = "Choose your preferred duration to view real-time transparent rates. Therapists bring all equipment directly to your residence.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LightGrayText,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))
                DurationSelectorRow(
                    selectedDuration = selectedDuration,
                    onDurationSelected = onDurationSelected
                )

                Spacer(modifier = Modifier.height(12.dp))
                FreeMaskBanner()
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        items(services, key = { it.id }) { service ->
            ServiceDetailCard(
                service = service,
                selectedDuration = selectedDuration,
                isVip = userSession.isVip,
                onBookDuration = { dur -> onBookService(service, dur) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServiceDetailCard(
    service: SpaService,
    selectedDuration: SpaDuration,
    isVip: Boolean,
    onBookDuration: (SpaDuration) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("detailed_service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = PlumCard),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, PlumBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                                    fontSize = 9.5.sp,
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
                            color = SoftWhite,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = service.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ChampagneGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PlumSurface)
                        .border(1.dp, PlumBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = RosePinkPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LightGrayText,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key Benefits Tags
            // FlowRow: three long benefit chips in a plain Row get squashed/clipped on narrow screens.
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                service.benefits.forEach { b ->
                    Surface(
                        color = PlumSurface,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.7.dp, PlumBorder.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "✓ $b",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                color = LightGrayText
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Exact Pricing Matrix Row (30 Min, 60 Min, 90 Min)
            Text(
                text = "DURATION & PRICING MATRIX",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = MutedText,
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpaDuration.entries.forEach { dur ->
                    val isCurrentSelected = dur == selectedDuration
                    val price = service.getPrice(dur, isVip)

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onBookDuration(dur) }
                            .testTag("pricing_matrix_${service.id}_${dur.minutes}"),
                        color = if (isCurrentSelected) SoftPinkContainer else PlumSurface,
                        border = BorderStroke(
                            1.dp,
                            if (isCurrentSelected) RosePinkPrimary else PlumBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dur.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrentSelected) PalePinkText else LightGrayText
                                )
                            )
                            Text(
                                text = "₹$price",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ChampagneGold
                                )
                            )
                            if (isVip) {
                                Text(
                                    text = "VIP Rate",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.sp,
                                        color = ChampagneGold.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = BlushPinkAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Includes FREE Face Mask 🎭",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BlushPinkAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Button(
                    onClick = { onBookDuration(selectedDuration) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosePinkPrimary,
                        contentColor = SoftWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("book_button_${service.id}")
                ) {
                    Text(
                        text = "Book Session",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
