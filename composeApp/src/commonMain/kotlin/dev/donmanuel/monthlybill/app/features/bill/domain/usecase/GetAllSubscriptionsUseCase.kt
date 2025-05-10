package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.SubscriptionEntity
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow

class GetAllSubscriptionsUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    operator fun invoke(): Flow<List<SubscriptionEntity>?> {
        return subscriptionRepository.getAllSubscriptions()
    }
}