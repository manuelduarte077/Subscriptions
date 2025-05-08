package dev.donmanuel.monthlybill.app.features.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Text(text = "Settings")
        },

        ) { innerPadding ->
        Text(
            text = "Settings Screen",
            modifier = modifier.padding(innerPadding)
        )
    }
}