package dev.donmanuel.monthlybill.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.donmanuel.monthlybill.app.features.calendar.CalendarScreen
import dev.donmanuel.monthlybill.app.features.composables.ModalBottomSheetContent
import dev.donmanuel.monthlybill.app.features.home.HomeScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.BottomNavigationBarContent
import dev.donmanuel.monthlybill.app.navigation.tabs.RootScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigationGraph(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()


    // Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var fabVisible by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Monthly Bill")
                },
            )
        },
        bottomBar = {
            BottomNavigationBarContent(
                navController = navController,
            ) { item ->
                fabVisible = item == RootScreen.Home
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = expandHorizontally(),
                exit = shrinkHorizontally(),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Create")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RootScreen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            addHomeScreen()
            addCalendarScreen()
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,

            ) {
            ModalBottomSheetContent {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        }
    }
}


private fun NavGraphBuilder.addHomeScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.Home.route) {
        HomeScreen(modifier)
    }
}

private fun NavGraphBuilder.addCalendarScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.Calendar.route) {
        CalendarScreen(modifier)
    }
}
