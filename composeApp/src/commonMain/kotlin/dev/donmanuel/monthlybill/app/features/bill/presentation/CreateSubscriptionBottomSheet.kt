package dev.donmanuel.monthlybill.app.features.bill.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.InsertSubscriptionUseCase
import dev.donmanuel.monthlybill.app.features.bill.presentation.composables.SubscriptionDateFields
import dev.donmanuel.monthlybill.app.features.bill.presentation.composables.SubscriptionFormFields
import dev.donmanuel.monthlybill.app.features.categories.domain.usecases.GetCategoriesUseCase
import dev.donmanuel.monthlybill.app.utils.Constants
import dev.donmanuel.monthlybill.app.utils.ValidationUtils
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun CreateSubscriptionBottomSheet(
    onClose: () -> Unit,
    insertSubscriptionUseCase: InsertSubscriptionUseCase,
    getCategoriesUseCase: GetCategoriesUseCase
) {
    val coroutineScope = rememberCoroutineScope()
    
    // Form state
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    
    // Dropdown states
    var selectedCurrency by remember { mutableStateOf(Constants.SUPPORTED_CURRENCIES.first()) }
    var selectedBillingCycle by remember { mutableStateOf(Constants.BILLING_CYCLES.first()) }
    var selectedCategory by remember { mutableStateOf(Constants.DEFAULT_CATEGORY) }
    
    // Date states
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var startDate by remember { mutableStateOf(today) }
    var endDate by remember { mutableStateOf<kotlinx.datetime.LocalDate?>(null) }
    
    // Categories
    val categories by getCategoriesUseCase().collectAsStateWithLifecycle(initialValue = emptyList())
    
    LaunchedEffect(categories) {
        if (categories?.isNotEmpty() == true) {
            selectedCategory = categories?.first()?.name ?: Constants.DEFAULT_CATEGORY
        }
    }

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

        // Form Fields
        SubscriptionFormFields(
            name = name,
            onNameChange = { name = it },
            amount = amount,
            onAmountChange = { amount = it },
            selectedCurrency = selectedCurrency,
            onCurrencyChange = { selectedCurrency = it },
            currencies = Constants.SUPPORTED_CURRENCIES,
            selectedBillingCycle = selectedBillingCycle,
            onBillingCycleChange = { selectedBillingCycle = it },
            billingCycles = Constants.BILLING_CYCLES,
            selectedCategory = selectedCategory,
            onCategoryChange = { selectedCategory = it },
            categories = categories?.map { it.name },
            showError = showError
        )

        // Date Fields
        SubscriptionDateFields(
            startDate = startDate,
            onStartDateChange = { startDate = it },
            endDate = endDate,
            onEndDateChange = { endDate = it }
        )

        Spacer(Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onClose, enabled = !isSaving) {
                Text(
                    "Cancelar", 
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = TextStyle(fontSize = 18.sp)
                )
            }

            Button(
                onClick = {
                    val priceValue = ValidationUtils.parsePrice(amount)
                    if (priceValue == null || !ValidationUtils.isValidPrice(amount)) {
                        showError = true
                        return@Button
                    }
                    showError = false
                    isSaving = true
                    coroutineScope.launch {
                        val subscription = Subscription(
                            name = name,
                            price = priceValue,
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
                enabled = ValidationUtils.isValidName(name) && amount.isNotBlank() && !isSaving
            ) {
                Text(
                    if (isSaving) "Guardando..." else "Registrar", 
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}