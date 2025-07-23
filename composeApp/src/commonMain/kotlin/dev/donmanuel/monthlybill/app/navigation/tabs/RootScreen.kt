package dev.donmanuel.monthlybill.app.navigation.tabs

import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.calendar
import monthlybill.composeapp.generated.resources.home
import monthlybill.composeapp.generated.resources.ic_category
import org.jetbrains.compose.resources.DrawableResource


sealed class BottomNavScreen(
    var route: String,
    val icon: DrawableResource,
    var title: String
) {
    data object Home : BottomNavScreen("home", Res.drawable.home, "Home")
    data object Calendar : BottomNavScreen("calendar", Res.drawable.calendar, "Calendar")
    data object Category : BottomNavScreen("category", Res.drawable.ic_category, "Category")
}

sealed class AppScreen(var route: String, var title: String) {
    data object BillDetails : AppScreen("billDetails/{billId}", "Bill Details")
}