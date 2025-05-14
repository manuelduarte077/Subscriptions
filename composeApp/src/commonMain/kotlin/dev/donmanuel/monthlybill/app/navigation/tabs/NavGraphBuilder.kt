package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.donmanuel.monthlybill.app.features.bill.presentation.BillDetailsScreen
import dev.donmanuel.monthlybill.app.features.calendar.CalendarScreen
import dev.donmanuel.monthlybill.app.features.categories.presentation.ui.CategoryScreen
import dev.donmanuel.monthlybill.app.features.home.HomeScreen
import dev.donmanuel.monthlybill.app.features.settings.presentation.SettingsScreen

fun NavGraphBuilder.addHomeScreen(navController: NavController) {
    composable(BottomNavScreen.Home.route) {
        HomeScreen(navController)
    }
}

fun NavGraphBuilder.addCalendarScreen(navController: NavController) {
    composable(BottomNavScreen.Calendar.route) {
        CalendarScreen(navController)
    }
}

fun NavGraphBuilder.addCategoryScreen() {
    composable(BottomNavScreen.Category.route) {
        CategoryScreen()
    }
}

fun NavGraphBuilder.addSettingsScreen(modifier: Modifier = Modifier) {
    composable(BottomNavScreen.Settings.route) {
        SettingsScreen(modifier)
    }
}

fun NavGraphBuilder.addBillDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(
        route = "${AppScreen.BillDetails.route}/{billId}",
        arguments = listOf(navArgument("billId") { type = NavType.IntType })
    ) { backStackEntry ->
        val billId = backStackEntry.arguments?.getInt("billId") ?: -1

        BillDetailsScreen(modifier, navController)
    }
}