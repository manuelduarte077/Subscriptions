package dev.donmanuel.monthlybill.app.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.donmanuel.monthlybill.app.features.settings.presentation.composables.SettingsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToThemeSettings: () -> Unit = {},
    onCurrencySettingClicked: () -> Unit = {},
    onAboutClicked: () -> Unit = {},
    onRateAppClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Column {

            SettingsItem(
                title = "Theme",
                description = "Change app appearance (Light, Dark, System)",
                onClick = onNavigateToThemeSettings
            )
            SettingsItem(
                title = "Currency",
                description = "Select your preferred currency",
                value = "USD",
                onClick = onCurrencySettingClicked
            )
            SettingsItem(
                title = "About",
                description = "Learn more about the app",
                onClick = onAboutClicked
            )
            SettingsItem(
                title = "Rate the App",
                description = "Support us by rating the app",
                onClick = onRateAppClicked
            )
        }
    }
}


