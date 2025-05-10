package dev.donmanuel.monthlybill.app.features.categories.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<CategoriesViewModel>()
    val categories by viewModel.getAllCategories()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold { innerPadding ->
        Box(contentAlignment = Alignment.Center) {
            LazyColumn(modifier = modifier.padding(innerPadding)) {
                itemsIndexed(items = categories.orEmpty()) { index, item ->
                    CategoryItem(subscription = item)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(subscription: Category) {
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