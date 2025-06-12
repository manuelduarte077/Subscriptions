package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.features.settings.presentation.SettingsScreen
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.compose_multiplatform
import monthlybill.composeapp.generated.resources.home
import org.jetbrains.compose.resources.painterResource

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
                        painterResource(Res.drawable.compose_multiplatform),
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