package com.taskify.todomanager.ui.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import com.taskify.todomanager.R

object CategoryIcons {
    val iconList = listOf(
        Icons.Default.WorkspacePremium,   // Work 💼
        Icons.Default.Home,               // Personal 🏠
        Icons.Default.MenuBook,           // Study 📚
        Icons.Default.Favorite,           // Health ❤️
        Icons.Default.ShoppingCart,       // Shopping 🛒
        Icons.Default.Flight,             // Travel ✈️
        Icons.Default.AccountBalanceWallet, // Finance 💰
        Icons.Default.Lightbulb,          // Ideas 💡
        Icons.Default.Star,               // Important ⭐
        Icons.Default.PushPin             // Others 📌
    )
}