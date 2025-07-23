package dev.donmanuel.monthlybill.app.navigation.tabs

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBarContent(
    navController: NavController,
    onItemClicked: (BottomNavScreen) -> Unit,
) {
    BottomNavigationBarContent { item ->
        navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
            onItemClicked(item)
        }
    }
}

@Composable
fun BottomNavigationBarContent(
    onItemClicked: (BottomNavScreen) -> Unit
) {
    BottomAppBar(modifier = Modifier) {
        val items = listOf(
            BottomNavScreen.Home,
            BottomNavScreen.Calendar,
            BottomNavScreen.Category,
        )
        var selectedItem by remember { mutableStateOf(0) }
        var currentRoute by remember { mutableStateOf(BottomNavScreen.Home.route) }

        items.forEachIndexed { index, navigationItem ->
            if (navigationItem.route == currentRoute) {
                selectedItem = index
            }
        }

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = "${item.title} icon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            item.title,
                            fontFamily = redHatBoldFont(),
                            fontSize = FontSize.REGULAR,
                        )
                    },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        currentRoute = item.route
                        onItemClicked(item)
                    }
                )
            }
        }
    }
}