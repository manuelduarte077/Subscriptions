package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.features.settings.presentation.SettingsScreen
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    /// Show Modal Bottom Sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = "Monthly Bill",
                        fontSize = FontSize.LARGE,
                        fontFamily = redHatBoldFont(),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(
                                onClick = { showBottomSheet = true },
                                indication = LocalIndication.current,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
    ) { innerPadding ->
        Text(
            text = "Home Screen",
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.wrapContentHeight(), // Adjust height as needed
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                SettingsScreen()
            }
        }
    }
}



