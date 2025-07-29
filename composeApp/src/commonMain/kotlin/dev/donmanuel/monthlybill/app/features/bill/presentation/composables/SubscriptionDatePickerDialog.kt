package dev.donmanuel.monthlybill.app.features.bill.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import dev.donmanuel.monthlybill.app.utils.Constants

@Composable
fun SubscriptionDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var dateText by remember { mutableStateOf(initialDate.toString()) }
    var showError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona una fecha") },
        text = {
            TextField(
                value = dateText,
                onValueChange = { 
                    dateText = it
                    showError = false
                },
                label = { Text(Constants.DATE_FORMAT) },
                isError = showError,
                supportingText = if (showError) {
                    { Text(Constants.ErrorMessages.INVALID_DATE_FORMAT) }
                } else null
            )
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val parsedDate = LocalDate.parse(dateText)
                    onDateSelected(parsedDate)
                    onDismiss()
                } catch (e: Exception) {
                    showError = true
                    println("Error parsing date: $dateText, error: ${e.message}")
                }
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}