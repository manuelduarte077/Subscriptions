package dev.donmanuel.monthlybill.app.features.categories.domain.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getSubscriptions(): Flow<List<Category>?>
}