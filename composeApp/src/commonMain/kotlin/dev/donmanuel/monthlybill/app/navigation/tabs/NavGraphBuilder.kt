package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.donmanuel.monthlybill.app.features.bill.BillDetailsScreen
import dev.donmanuel.monthlybill.app.features.calendar.CalendarScreen
import dev.donmanuel.monthlybill.app.features.categories.presentation.ui.CategoryScreen
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

fun NavGraphBuilder.addCategoryScreen() {
    composable(RootScreen.Category.route) {
        CategoryScreen()
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
    composable(
        route = "${RootScreen.BillDetails.route}/{billId}",
        arguments = listOf(navArgument("billId") { type = NavType.IntType })
    ) { backStackEntry ->
        val billId = backStackEntry.arguments?.getInt("billId") ?: -1

        BillDetailsScreen(modifier, navController) // Pass the billId
    }
}