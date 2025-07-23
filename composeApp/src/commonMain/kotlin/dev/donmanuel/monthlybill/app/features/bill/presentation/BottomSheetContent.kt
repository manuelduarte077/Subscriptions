package dev.donmanuel.monthlybill.app.features.bill.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.InsertSubscriptionUseCase
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun BottomSheetContent(
    onClose: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val insertSubscriptionUseCase = koinInject<InsertSubscriptionUseCase>()
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }

    // Currency
    val currencies = listOf("USD", "NIO", "EUR", "MXN", "COP", "ARS", "BRL")
    var selectedCurrency by remember { mutableStateOf(currencies.first()) }
    var currencyExpanded by remember { mutableStateOf(false) }

    // Billing Cycle
    val billingCycles = listOf("monthly", "yearly", "weekly", "daily")
    var selectedBillingCycle by remember { mutableStateOf(billingCycles.first()) }
    var billingCycleExpanded by remember { mutableStateOf(false) }

    // Category
    val categoriesViewModel = koinViewModel<CategoriesViewModel>()
    val categories by categoriesViewModel.getAllCategories().collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedCategory by remember { mutableStateOf(categories?.firstOrNull()?.name ?: "General") }
    var categoryExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(categories) {
        if (selectedCategory.isBlank() && categories?.isNotEmpty() == true) {
            selectedCategory = categories?.first()?.name ?: "General"
        }
    }

    // Dates
    val today = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    var startDate by remember { mutableStateOf(today) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    fun isValidDouble(value: String): Boolean = value.toDoubleOrNull() != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nueva Suscripción",
            style = MaterialTheme.typography.titleLarge
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre de la suscripción") },
            singleLine = true
        )

        TextField(
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            prefix = { Text("$ ") }
        )

        // Currency Dropdown
        ExposedDropdownMenuBox(
            expanded = currencyExpanded,
            onExpandedChange = { currencyExpanded = !currencyExpanded }
        ) {
            OutlinedTextField(
                value = selectedCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text("Moneda") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            DropdownMenu(
                expanded = currencyExpanded,
                onDismissRequest = { currencyExpanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            selectedCurrency = currency
                            currencyExpanded = false
                        }
                    )
                }
            }
        }

        // Billing Cycle Dropdown
        ExposedDropdownMenuBox(
            expanded = billingCycleExpanded,
            onExpandedChange = { billingCycleExpanded = !billingCycleExpanded }
        ) {
            OutlinedTextField(
                value = selectedBillingCycle,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ciclo de facturación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = billingCycleExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            DropdownMenu(
                expanded = billingCycleExpanded,
                onDismissRequest = { billingCycleExpanded = false }
            ) {
                billingCycles.forEach { cycle ->
                    DropdownMenuItem(
                        text = { Text(cycle) },
                        onClick = {
                            selectedBillingCycle = cycle
                            billingCycleExpanded = false
                        }
                    )
                }
            }
        }

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories?.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category.name
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        // Start Date Picker
        OutlinedButton(onClick = { showStartDatePicker = true }) {
            Text("Fecha de inicio: ${startDate.toString()}")
        }
        if (showStartDatePicker) {
            DatePickerDialog(
                initialDate = startDate,
                onDateSelected = {
                    startDate = it
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }

        // End Date Picker
        OutlinedButton(onClick = { showEndDatePicker = true }) {
            Text("Fecha de fin: ${endDate?.toString() ?: "No definida"}")
        }
        if (showEndDatePicker) {
            DatePickerDialog(
                initialDate = endDate ?: startDate,
                onDateSelected = {
                    endDate = it
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }

        if (showError) {
            Text(
                text = "Por favor ingresa un monto válido.",
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically
        ) {
            OutlinedButton(onClick = onClose, enabled = !isSaving) {
                Text(
                    "Cancelar", modifier = Modifier.padding(horizontal = 8.dp),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
            Button(
                onClick = {
                    if (!isValidDouble(amount)) {
                        showError = true
                        return@Button
                    }
                    showError = false
                    isSaving = true
                    coroutineScope.launch {
                        val subscription = Subscription(
                            name = name,
                            price = amount.toDouble(),
                            currency = selectedCurrency,
                            billingCycle = selectedBillingCycle,
                            category = selectedCategory,
                            startDate = startDate.toString(),
                            endDate = endDate?.toString()
                        )
                        insertSubscriptionUseCase(subscription)
                        isSaving = false
                        onClose()
                    }
                },
                enabled = name.isNotBlank() && amount.isNotBlank() && !isSaving
            ) {
                Text(
                    if (isSaving) "Guardando..." else "Registrar", modifier = Modifier.padding(horizontal = 8.dp),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(
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