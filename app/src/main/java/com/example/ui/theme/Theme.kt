package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = RosePinkPrimary,
    onPrimary = SoftWhite,
    primaryContainer = SoftPinkContainer,
    onPrimaryContainer = PalePinkText,
    secondary = GoldAccent,
    onSecondary = WineDark,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = ChampagneGold,
    tertiary = BlushPinkAccent,
    onTertiary = WineDark,
    background = PlumBackground,
    onBackground = SoftWhite,
    surface = PlumSurface,
    onSurface = SoftWhite,
    surfaceVariant = PlumSurfaceVariant,
    onSurfaceVariant = LightGrayText,
    outline = PlumBorder,
    error = UrgentCoral,
    onError = SoftWhite
)

@Composable
fun MoonnightTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography = Typography,
        content = content
    )
}
