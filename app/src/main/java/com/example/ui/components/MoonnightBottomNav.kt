package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppNavTab
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.LightGrayText
import com.example.ui.theme.MutedText
import com.example.ui.theme.PlumBorder
import com.example.ui.theme.PlumSurface
import com.example.ui.theme.RosePinkPrimary
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.SoftWhite

@Composable
fun MoonnightBottomNav(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.testTag("bottom_navigation_bar"),
        color = PlumSurface,
        border = BorderStroke(1.dp, PlumBorder.copy(alpha = 0.5f))
    ) {
        NavigationBar(
            containerColor = PlumSurface,
            contentColor = SoftWhite,
            windowInsets = WindowInsets.navigationBars
        ) {
            AppNavTab.values().forEach { tab ->
                val isSelected = tab == currentTab
                val icon = when (tab) {
                    AppNavTab.HOME -> Icons.Default.Home
                    AppNavTab.SERVICES -> Icons.Default.Spa
                    AppNavTab.BOOKINGS -> Icons.Default.CalendarMonth
                    AppNavTab.PARTNER -> Icons.Default.Work
                    AppNavTab.PROFILE -> Icons.Default.Person
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.title,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RosePinkPrimary,
                        selectedTextColor = ChampagneGold,
                        unselectedIconColor = MutedText,
                        unselectedTextColor = LightGrayText,
                        indicatorColor = SoftPinkContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                )
            }
        }
    }
}
