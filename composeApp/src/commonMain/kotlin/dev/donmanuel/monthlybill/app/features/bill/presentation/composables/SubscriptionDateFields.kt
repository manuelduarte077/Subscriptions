package dev.donmanuel.monthlybill.app.features.bill.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDateFields(
    startDate: LocalDate,
    onStartDateChange: (LocalDate) -> Unit,
    endDate: LocalDate?,
    onEndDateChange: (LocalDate?) -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = { showStartDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fecha de inicio: $startDate")
        }
        
        if (showStartDatePicker) {
            SubscriptionDatePickerDialog(
                initialDate = startDate,
                onDateSelected = { selectedDate ->
                    onStartDateChange(selectedDate)
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }

        // End Date Picker
        OutlinedButton(
            onClick = { showEndDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fecha de fin: ${endDate?.toString() ?: "No definida"}")
        }
        
        if (showEndDatePicker) {
            SubscriptionDatePickerDialog(
                initialDate = endDate ?: startDate,
                onDateSelected = { selectedDate ->
                    onEndDateChange(selectedDate)
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }
    }
} 