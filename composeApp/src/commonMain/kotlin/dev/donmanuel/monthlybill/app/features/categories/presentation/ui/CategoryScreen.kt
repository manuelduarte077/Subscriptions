package dev.donmanuel.monthlybill.app.features.categories.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<CategoriesViewModel>()
    val categories by viewModel.getAllCategories()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            itemsIndexed(items = categories.orEmpty()) { index, item ->
                CategoryItem(
                    category = item
                )
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                painter = painterResource(category.icon),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
