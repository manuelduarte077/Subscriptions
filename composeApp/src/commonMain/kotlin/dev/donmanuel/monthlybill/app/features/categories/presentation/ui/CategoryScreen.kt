package dev.donmanuel.monthlybill.app.features.categories.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Subscription
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel

@Composable
fun CategoryScreen(
    viewModel: CategoriesViewModel = viewModel()
) {

    val subscriptions by viewModel.subscription.collectAsState()

    Scaffold { innerPadding ->
        LazyColumn {
            items(subscriptions) { subscription ->
                SubscriptionItem(subscription)
            }
        }
    }
}

@Composable
fun SubscriptionItem(subscription: Subscription) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (subscription.icon != null && false) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(text = subscription.name)
                Text(text = "${subscription.price} ${subscription.currency}")
            }
        }
    }
}