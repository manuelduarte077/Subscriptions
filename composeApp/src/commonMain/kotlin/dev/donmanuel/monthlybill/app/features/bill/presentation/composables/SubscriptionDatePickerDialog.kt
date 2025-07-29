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

@Composable
fun SubscriptionDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var dateText by remember { mutableStateOf(initialDate.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona una fecha") },
        text = {
            TextField(
                value = dateText,
                onValueChange = { dateText = it },
                label = { Text("YYYY-MM-DD") }
            )
        },
        confirmButton = {
            Button(onClick = {
                runCatching { LocalDate.parse(dateText) }.onSuccess {
                    onDateSelected(it)
                }
                onDismiss()
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