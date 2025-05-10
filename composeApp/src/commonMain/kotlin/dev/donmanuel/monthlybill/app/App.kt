package dev.donmanuel.monthlybill.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dev.donmanuel.monthlybill.app.navigation.RootNavigationGraph

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        RootNavigationGraph(navController = navController)
    }
}