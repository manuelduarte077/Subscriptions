package dev.donmanuel.monthlybill.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dev.donmanuel.monthlybill.app.navigation.RootNavigationGraph
import dev.donmanuel.monthlybill.app.theme.MonthlyBillTheme

@Composable
fun App() {

    MonthlyBillTheme {
        val navController = rememberNavController()
        RootNavigationGraph(navController = navController)
    }
}