package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RootScreen(var route: String, val icon: ImageVector?, var title: String) {
    data object Home : RootScreen("home", Icons.Rounded.Home, "Home")
    data object Calendar : RootScreen("calendar", Icons.Rounded.CheckCircle, "Calendar")
    data object Category : RootScreen("category", Icons.AutoMirrored.Rounded.List, "Category")
    data object Settings : RootScreen("settings", Icons.Rounded.Settings, "Settings")
    data object BillDetails : RootScreen("billDetails", Icons.Rounded.PlayArrow, "BillDetails")
}