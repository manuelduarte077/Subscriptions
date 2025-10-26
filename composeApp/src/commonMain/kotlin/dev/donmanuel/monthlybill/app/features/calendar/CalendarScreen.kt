package dev.donmanuel.monthlybill.app.features.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.GetAllSubscriptionsUseCase
import dev.donmanuel.monthlybill.app.features.calendar.components.*
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import kotlinx.datetime.*
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun CalendarScreen(
    navController: NavController
) {

    val getAllSubscriptionsUseCase = koinInject<GetAllSubscriptionsUseCase>()
    val subscriptions by getAllSubscriptionsUseCase().collectAsStateWithLifecycle(initialValue = emptyList())

    val today = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(today) }

    val startOfMonth = LocalDate(selectedDate.year, selectedDate.month, 1)
    val daysInMonth = when (selectedDate.month) {
        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        Month.FEBRUARY -> if (selectedDate.year % 4 == 0 && (selectedDate.year % 100 != 0 || selectedDate.year % 400 == 0)) 29 else 28
    }
    val firstDayOfWeek = (startOfMonth.dayOfWeek.ordinal + 1) % 7

    val subscriptionsByDate = remember(subscriptions, selectedDate) {
        subscriptions.orEmpty()
            .filter { sub ->
                val startDate = kotlin.runCatching {
                    LocalDate.parse(sub.startDate)
                }.getOrNull() ?: return@filter false
                val endDate = sub.endDate?.let {
                    kotlin.runCatching { LocalDate.parse(it) }.getOrNull()
                }

                val subMonth = YearMonth(startDate.year, startDate.month)
                val currentMonth = YearMonth(selectedDate.year, selectedDate.month)
                val withinMonth = subMonth == currentMonth ||
                        (endDate != null && YearMonth(endDate.year, endDate.month) == currentMonth)
                withinMonth
            }
            .associateBy {
                runCatching { LocalDate.parse(it.startDate) }.getOrNull()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calendar",
                        fontSize = FontSize.LARGE,
                        fontFamily = redHatBoldFont(),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Month selector
            MonthSelector(
                selectedDate = selectedDate,
                onMonthChange = { selectedDate = it },
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider()

            // Calendar grid
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    CalendarGrid(
                        selectedDate = selectedDate,
                        firstDayOfWeek = firstDayOfWeek,
                        daysInMonth = daysInMonth,
                        today = today,
                        subscriptionsByDate = subscriptionsByDate,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Subscriptions list for selected month
                item {
                    Text(
                        text = "Subscriptions this month",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(subscriptionsByDate.entries.toList()) { (date, subscription) ->
                    SubscriptionCalendarItem(
                        subscription = subscription,
                        date = date ?: today,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}