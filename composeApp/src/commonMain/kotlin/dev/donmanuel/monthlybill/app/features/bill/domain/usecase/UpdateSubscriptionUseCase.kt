package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.SubscriptionEntity
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository

class UpdateSubscriptionUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: SubscriptionEntity) {
        subscriptionRepository.updateSubscription(subscription = subscription)
    }
}