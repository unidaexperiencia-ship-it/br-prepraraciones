package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbPrimaryContainer
import com.example.ui.theme.GbSecondaryText

sealed class RbTab(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
) {
    object Promociones : RbTab(
        title = "Promos",
        selectedIcon = Icons.Filled.LocalOffer,
        unselectedIcon = Icons.Outlined.LocalOffer,
        testTag = "tab_promotions"
    )

    object Recomendaciones : RbTab(
        title = "Recomendar",
        selectedIcon = Icons.Filled.CardGiftcard,
        unselectedIcon = Icons.Outlined.CardGiftcard,
        testTag = "tab_recommendations"
    )

    object Oportunidad : RbTab(
        title = "Oportunidad",
        selectedIcon = Icons.Filled.TrendingUp,
        unselectedIcon = Icons.Outlined.TrendingUp,
        testTag = "tab_opportunity"
    )

    object Negocios : RbTab(
        title = "Negocios",
        selectedIcon = Icons.Filled.Business,
        unselectedIcon = Icons.Outlined.Business,
        testTag = "tab_business"
    )
}

val rbTabs = listOf(
    RbTab.Promociones,
    RbTab.Recomendaciones,
    RbTab.Oportunidad,
    RbTab.Negocios
)

@Composable
fun RbBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        rbTabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GbDarkText,
                    selectedTextColor = GbDarkText,
                    indicatorColor = GbPrimaryContainer,
                    unselectedIconColor = GbSecondaryText,
                    unselectedTextColor = GbSecondaryText
                ),
                modifier = Modifier.testTag(tab.testTag)
            )
        }
    }
}

