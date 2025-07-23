package dev.donmanuel.monthlybill.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.donmanuel.monthlybill.app.features.bill.presentation.ModalBottomSheetContent
import dev.donmanuel.monthlybill.app.navigation.tabs.*
import kotlinx.coroutines.launch
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.add_circle
import monthlybill.composeapp.generated.resources.ic_add
import org.jetbrains.compose.resources.painterResource

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
                    Icon(
                        painterResource(Res.drawable.ic_add),
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
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