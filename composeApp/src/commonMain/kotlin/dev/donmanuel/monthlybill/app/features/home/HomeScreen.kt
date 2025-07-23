package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.features.home.components.SimpleSearchBar
import dev.donmanuel.monthlybill.app.features.settings.presentation.SettingsScreen
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.ic_settings
import org.jetbrains.compose.resources.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.GetAllSubscriptionsUseCase
import org.koin.compose.koinInject
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    // Obtener las suscripciones
    val getAllSubscriptionsUseCase = koinInject<GetAllSubscriptionsUseCase>()
    val subscriptions by getAllSubscriptionsUseCase().collectAsStateWithLifecycle(initialValue = emptyList())

    // Obtener categorías para íconos
    val categoriesViewModel = koinViewModel<CategoriesViewModel>()
    val categories by categoriesViewModel.getAllCategories().collectAsStateWithLifecycle(initialValue = emptyList())

    val today = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
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
            SimpleSearchBar(
                modifier = Modifier.padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(subscriptions.orEmpty()) { subscription: Subscription ->
                    val category = categories?.firstOrNull { it.name == subscription.category }
                    val icon = category?.icon
                    val endDate = subscription.endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                    val daysToExpire = endDate?.let { today.daysUntil(it) } ?: Int.MAX_VALUE
                    val highlight = daysToExpire in 0..5

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = if (highlight) CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)) else CardDefaults.cardColors()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (icon != null) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = "Categoría",
                                    modifier = Modifier.size(45.dp).padding(end = 8.dp, start = 10.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(12.dp).weight(1f)) {
                                Text(subscription.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "${subscription.price} ${subscription.currency}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "${subscription.billingCycle} | ${subscription.category}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text("Inicio: ${subscription.startDate}", style = MaterialTheme.typography.bodySmall)
                                if (!subscription.endDate.isNullOrBlank()) {
                                    Text("Fin: ${subscription.endDate}", style = MaterialTheme.typography.bodySmall)
                                }
                                if (highlight && daysToExpire >= 0) {
                                    Text(
                                        "¡Próxima a vencer en $daysToExpire días!",
                                        color = Color(0xFF856404),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
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