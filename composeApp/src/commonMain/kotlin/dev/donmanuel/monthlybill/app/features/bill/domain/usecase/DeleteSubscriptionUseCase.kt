package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.SubscriptionEntity
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository

class DeleteSubscriptionUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: SubscriptionEntity) {
        subscriptionRepository.deleteSubscription(subscription = subscription)
    }
}