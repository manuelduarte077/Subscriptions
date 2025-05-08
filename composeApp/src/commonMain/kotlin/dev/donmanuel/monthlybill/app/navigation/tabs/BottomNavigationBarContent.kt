package dev.donmanuel.monthlybill.app.navigation.tabs

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
import androidx.navigation.NavController

@Composable
fun BottomNavigationBarContent(
    navController: NavController,
    onItemClicked: (RootScreen) -> Unit,
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
    onItemClicked: (RootScreen) -> Unit
) {
    BottomAppBar(modifier = Modifier) {
        val items = listOf(
            RootScreen.Home,
            RootScreen.Calendar,
        )
        var selectedItem by remember { mutableStateOf(0) }
        var currentRoute by remember { mutableStateOf(RootScreen.Home.route) }

        items.forEachIndexed { index, navigationItem ->
            if (navigationItem.route == currentRoute) {
                selectedItem = index
            }
        }

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = { Icon(item.icon!!, contentDescription = item.title) },
                    label = { Text(item.title) },
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