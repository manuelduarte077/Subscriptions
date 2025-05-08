package dev.donmanuel.monthlybill.app.navigation.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(modifier: Modifier = Modifier, onOptionSelected: () -> Unit) {
    ModalDrawerSheet {
        Column(modifier = modifier.fillMaxHeight().padding(16.dp)) {

            Column {
                Text(
                    text = "Monthly Bill",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                )
                Image(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            NavigationDrawerItem(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Category") },
                selected = false,
                onClick = onOptionSelected,
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Category") }
            )
            NavigationDrawerItem(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Settings") },
                selected = false,
                onClick = onOptionSelected,
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
            )
        }
    }
}