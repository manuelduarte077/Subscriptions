package dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.usecases.GetCategoriesUseCase
import kotlinx.coroutines.flow.Flow

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    fun getAllCategories(): Flow<List<Category>?> {
        return getCategoriesUseCase()
    }
}