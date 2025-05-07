package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.donmanuel.monthlybill.app.navigation.Screen

fun NavController.navigateToTabs(navOptions: NavOptions? = null) {
    navigate(Screen.Tabs.route)
}

fun NavGraphBuilder.tabsNavGraph(
    tabNavController: NavHostController,

    ) {
    composable(Screen.Tabs.route) {
        TabsRoute(
            tabNavController = tabNavController,
        )
    }
}