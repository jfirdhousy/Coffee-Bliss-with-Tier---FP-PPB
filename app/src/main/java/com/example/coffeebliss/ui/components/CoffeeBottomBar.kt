package com.example.coffeebliss.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/** The four main sections shown in the bottom navigation bar. */
enum class BottomTab { HOME, TRANSACTIONS, REWARDS, PROFILE }

/**
 * The bottom navigation bar shared by the main screens.
 * [current] is highlighted; tapping any item calls [onSelect].
 */
@Composable
fun CoffeeBottomBar(
    current: BottomTab,
    onSelect: (BottomTab) -> Unit
) {
    NavigationBar {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = current == tab,
                onClick = { onSelect(tab) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}

private val BottomTab.icon: ImageVector
    get() = when (this) {
        BottomTab.HOME -> Icons.Default.Home
        BottomTab.TRANSACTIONS -> Icons.Default.ShoppingCart
        BottomTab.REWARDS -> Icons.Default.Star
        BottomTab.PROFILE -> Icons.Default.Person
    }

private val BottomTab.label: String
    get() = when (this) {
        BottomTab.HOME -> "Home"
        BottomTab.TRANSACTIONS -> "Transaksi"
        BottomTab.REWARDS -> "Reward"
        BottomTab.PROFILE -> "Profil"
    }
