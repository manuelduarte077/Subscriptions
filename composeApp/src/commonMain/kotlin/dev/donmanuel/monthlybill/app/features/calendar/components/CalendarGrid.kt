package dev.donmanuel.monthlybill.app.features.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import kotlinx.datetime.LocalDate

@Composable
fun CalendarGrid(
    selectedDate: LocalDate,
    firstDayOfWeek: Int,
    daysInMonth: Int,
    today: LocalDate,
    subscriptionsByDate: Map<LocalDate?, Subscription>,
    modifier: Modifier = Modifier
) {
    val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val weeksInMonth = ((firstDayOfWeek + daysInMonth + 6) / 7)
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Week day headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar days
        repeat(weeksInMonth) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayIndex ->
                    val dateIndex = week * 7 + dayIndex - firstDayOfWeek
                    val isDayInMonth = dateIndex in 0..<daysInMonth
                    val day = if (isDayInMonth) dateIndex + 1 else null

                    val date = if (day != null) {
                        try {
                            LocalDate(selectedDate.year, selectedDate.month, day)
                        } catch (e: Exception) {
                            null
                        }
                    } else null
                    
                    val isToday = date == today && selectedDate.month == today.month && selectedDate.year == today.year
                    val hasSubscription = date != null && subscriptionsByDate.containsKey(date)
                    
                    CalendarDay(
                        day = day,
                        isToday = isToday,
                        hasSubscription = hasSubscription,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            if (week < weeksInMonth - 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

