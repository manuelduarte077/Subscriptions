package dev.donmanuel.monthlybill.app.features.categories.domain.usecases

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(private val subscriptionRepository: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>?> {
        return subscriptionRepository.getSubscriptions()
    }
}