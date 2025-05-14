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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.donmanuel.monthlybill.app.features.bill.presentation.ModalBottomSheetContent
import dev.donmanuel.monthlybill.app.navigation.tabs.BottomNavScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.BottomNavigationBarContent
import dev.donmanuel.monthlybill.app.navigation.tabs.addBillDetailsScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.addCalendarScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.addCategoryScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.addHomeScreen
import dev.donmanuel.monthlybill.app.navigation.tabs.addSettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    // Bottom Sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    var fabVisible by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBarContent(
                navController = navController,
            ) { item ->
                fabVisible = item == BottomNavScreen.Home
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
                    Text(
                        "Create",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            addHomeScreen(
                navController = navController,
            )
            addCalendarScreen(
                navController = navController,
            )
            addCategoryScreen()
            addSettingsScreen()
            addBillDetailsScreen(
                navController = navController,
            )
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