package dev.donmanuel.monthlybill.app.navigation.tabs

import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.home
import org.jetbrains.compose.resources.DrawableResource

sealed class BottomNavScreen(
    var route: String,
    val icon: DrawableResource,
    var title: String
) {
    data object Home : BottomNavScreen("home", Res.drawable.home, "Home")
    data object Calendar : BottomNavScreen("calendar", Res.drawable.home, "Calendar")
    data object Category : BottomNavScreen("category", Res.drawable.home, "Category")
    data object Settings : BottomNavScreen("settings", Res.drawable.home, "Settings")
}

sealed class AppScreen(var route: String, var title: String) {
    data object BillDetails : AppScreen("billDetails/{billId}", "Bill Details")
}