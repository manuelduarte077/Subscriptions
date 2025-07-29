package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.GetAllSubscriptionsUseCase
import dev.donmanuel.monthlybill.app.features.home.components.SubscriptionItem
import dev.donmanuel.monthlybill.app.features.settings.presentation.SettingsScreen
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import kotlinx.datetime.toLocalDateTime
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.ic_settings
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    val getAllSubscriptionsUseCase = koinInject<GetAllSubscriptionsUseCase>()
    val subscriptions by getAllSubscriptionsUseCase().collectAsStateWithLifecycle(initialValue = emptyList())

    val today = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

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
                    IconButton(onClick = { openBottomSheet = true }) {
                        Icon(
                            painterResource(Res.drawable.ic_settings),
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subscriptions.orEmpty()) { subscription ->
                    SubscriptionItem(
                        subscription = subscription,
                        today = today
                    )
                }
            }

            if (openBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.wrapContentHeight(),
                    onDismissRequest = { openBottomSheet = false },
                    sheetState = bottomSheetState,
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}