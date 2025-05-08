package dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Subscription
import dev.donmanuel.monthlybill.app.features.categories.domain.usecases.GetSubscriptionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase
) : ViewModel() {
    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscription: StateFlow<List<Subscription>> = _subscriptions


    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            val subscriptions = getSubscriptionsUseCase()
            _subscriptions.value = subscriptions
        }
    }
}