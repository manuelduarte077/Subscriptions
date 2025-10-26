package dev.donmanuel.monthlybill.app.features.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun MonthSelector(
    selectedDate: LocalDate,
    onMonthChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { 
            val newDate = if (selectedDate.month == Month.JANUARY) {
                LocalDate(selectedDate.year - 1, Month.DECEMBER, 1)
            } else {
                LocalDate(selectedDate.year, selectedDate.month.previous(), 1)
            }
            onMonthChange(newDate)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous month"
            )
        }
        
        Text(
            text = "${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(onClick = { 
            val newDate = if (selectedDate.month == Month.DECEMBER) {
                LocalDate(selectedDate.year + 1, Month.JANUARY, 1)
            } else {
                LocalDate(selectedDate.year, selectedDate.month.next(), 1)
            }
            onMonthChange(newDate)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next month"
            )
        }
    }
}

// Extension functions for Month navigation
private fun Month.previous(): Month = when (this) {
    Month.JANUARY -> Month.DECEMBER
    Month.FEBRUARY -> Month.JANUARY
    Month.MARCH -> Month.FEBRUARY
    Month.APRIL -> Month.MARCH
    Month.MAY -> Month.APRIL
    Month.JUNE -> Month.MAY
    Month.JULY -> Month.JUNE
    Month.AUGUST -> Month.JULY
    Month.SEPTEMBER -> Month.AUGUST
    Month.OCTOBER -> Month.SEPTEMBER
    Month.NOVEMBER -> Month.OCTOBER
    Month.DECEMBER -> Month.NOVEMBER
}

private fun Month.next(): Month = when (this) {
    Month.JANUARY -> Month.FEBRUARY
    Month.FEBRUARY -> Month.MARCH
    Month.MARCH -> Month.APRIL
    Month.APRIL -> Month.MAY
    Month.MAY -> Month.JUNE
    Month.JUNE -> Month.JULY
    Month.JULY -> Month.AUGUST
    Month.AUGUST -> Month.SEPTEMBER
    Month.SEPTEMBER -> Month.OCTOBER
    Month.OCTOBER -> Month.NOVEMBER
    Month.NOVEMBER -> Month.DECEMBER
    Month.DECEMBER -> Month.JANUARY
}

