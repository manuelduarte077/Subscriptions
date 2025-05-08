package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.donmanuel.monthlybill.app.features.bill.BillDetailsScreen
import dev.donmanuel.monthlybill.app.features.calendar.CalendarScreen
import dev.donmanuel.monthlybill.app.features.categories.CategoryScreen
import dev.donmanuel.monthlybill.app.features.home.HomeScreen
import dev.donmanuel.monthlybill.app.features.settings.SettingsScreen

fun NavGraphBuilder.addHomeScreen(navController: NavController) {
    composable(RootScreen.Home.route) {
        HomeScreen(navController)
    }
}

fun NavGraphBuilder.addCalendarScreen(navController: NavController) {
    composable(RootScreen.Calendar.route) {
        CalendarScreen(navController)
    }
}

fun NavGraphBuilder.addCategoryScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.Category.route) {
        CategoryScreen(modifier)
    }
}

fun NavGraphBuilder.addSettingsScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.Settings.route) {
        SettingsScreen(modifier)
    }
}

fun NavGraphBuilder.addBillDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(RootScreen.BillDetails.route) {
        BillDetailsScreen(modifier, navController)
    }
}
