package dev.donmanuel.monthlybill.app.features.bill.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.donmanuel.monthlybill.app.utils.Constants
import dev.donmanuel.monthlybill.app.utils.ValidationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    currencies: List<String>,
    selectedBillingCycle: String,
    onBillingCycleChange: (String) -> Unit,
    billingCycles: List<String>,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    categories: List<String>?,
    showError: Boolean
) {
    var currencyExpanded by remember { mutableStateOf(false) }
    var billingCycleExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name Field
        TextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre de la suscripción") },
            singleLine = true,
            isError = name.isNotBlank() && !ValidationUtils.isValidName(name)
        )

        // Amount Field
        TextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            prefix = { Text("$ ") },
            isError = showError
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
                modifier = Modifier.menuAnchor(PrimaryNotEditable, true).fillMaxWidth()
            )
            DropdownMenu(
                expanded = currencyExpanded,
                onDismissRequest = { currencyExpanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            onCurrencyChange(currency)
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
                modifier = Modifier.menuAnchor(PrimaryNotEditable, true).fillMaxWidth()
            )
            DropdownMenu(
                expanded = billingCycleExpanded,
                onDismissRequest = { billingCycleExpanded = false }
            ) {
                billingCycles.forEach { cycle ->
                    DropdownMenuItem(
                        text = { Text(cycle) },
                        onClick = {
                            onBillingCycleChange(cycle)
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
                modifier = Modifier.menuAnchor(PrimaryNotEditable, true).fillMaxWidth()
            )
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories?.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            onCategoryChange(category)
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        if (showError) {
            Text(
                text = Constants.ErrorMessages.INVALID_AMOUNT,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
} 