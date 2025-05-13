package dev.donmanuel.monthlybill.app.features.categories.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Show modal bottom sheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // Add skipPartiallyExpanded
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) } // State to hold the selected category

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        fontSize = 32.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    )
                },
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            itemsIndexed(items = categories.orEmpty()) { index, item ->
                CategoryItem(
                    category = item,
                    showBottomSheet = {
                        selectedCategory = item // Set the selected category when the item is clicked
                        showBottomSheet = true
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedCategory = null // Clear the selected category when dismissed
                },
                properties = ModalBottomSheetDefaults.properties,
                sheetState = sheetState,
            ) {
                // Pass the selected category to the bottom sheet content composable
                selectedCategory?.let { category ->
                    CategoryBottomSheetContent(category = category) {
                        showBottomSheet = false // Dismiss the bottom sheet from within the content
                        selectedCategory = null
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    showBottomSheet: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        onClick = {
            showBottomSheet()
        },
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
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun CategoryBottomSheetContent(
    category: Category,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Category Logo
        Image(
            painter = painterResource(category.icon),
            contentDescription = "${category.name} Icon",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp),
        )

        // Category Name
        Text(
            text = category.name,
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Category Description
        Text(
            text = category.description, // Use description from Category
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Plans Section
        if (category.plans.isNotEmpty()) {
            Text(
                text = "Available Plans:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )

            category.plans.forEach { plan ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = plan.name, style = MaterialTheme.typography.bodyMedium)
                    Text(text = plan.price, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}