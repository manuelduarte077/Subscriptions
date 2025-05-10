package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow

class GetAllSubscriptionsUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    operator fun invoke(): Flow<List<Subscription>?> {
        return subscriptionRepository.getAllSubscriptions()
    }
}