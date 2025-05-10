package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository

class GetSubscriptionByIdUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(id: Long): Subscription? {
        return subscriptionRepository.getSubscriptionById(id = id)
    }
}