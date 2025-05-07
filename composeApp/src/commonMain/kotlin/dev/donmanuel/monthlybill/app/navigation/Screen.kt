package dev.donmanuel.monthlybill.app.navigation

import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.calendar
import monthlybill.composeapp.generated.resources.category
import monthlybill.composeapp.generated.resources.home
import monthlybill.composeapp.generated.resources.home_selected
import monthlybill.composeapp.generated.resources.home_unselected
import monthlybill.composeapp.generated.resources.settings
import monthlybill.composeapp.generated.resources.tabs
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource


sealed class Screen(
    val route: String,
    val resourceId: StringResource,
    val selectedIcon: DrawableResource? = null,
    val unselectedIcon: DrawableResource? = null,
) {
    data object Tabs : Screen("tabs", Res.string.tabs)

    data object Home : Screen(
        "home",
        Res.string.home,
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected
    )

    data object Calendar : Screen(
        "calendar",
        Res.string.calendar,
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected
    )

    data object Category : Screen(
        "category",
        Res.string.category,
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected
    )

    data object Settings : Screen(
        "settings",
        Res.string.settings,
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected
    )

}