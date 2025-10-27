package dev.donmanuel.monthlybill.app.features.categories.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.presentation.ui.composables.CategoryBottomSheetContent
import dev.donmanuel.monthlybill.app.features.categories.presentation.ui.composables.CategoryItem
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val viewModel = koinViewModel<CategoriesViewModel>()
    val categories by viewModel.getAllCategories().collectAsStateWithLifecycle(initialValue = emptyList())

    // Show modal bottom sheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = redHatBoldFont(),
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items = categories.orEmpty()) { _, item ->
                CategoryItem(
                    category = item,
                    showBottomSheet = {
                        selectedCategory = item
                        showBottomSheet = true
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedCategory = null
                },
                properties = ModalBottomSheetDefaults.properties,
                sheetState = sheetState,
            ) {
                selectedCategory?.let { category ->
                    CategoryBottomSheetContent(category = category) {
                        showBottomSheet = false
                        selectedCategory = null
                    }
                }
            }
        }
    }
}


