package dev.donmanuel.monthlybill.app.features.bill.domain.usecase

import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository

class InsertSubscriptionUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: Subscription) {
        subscriptionRepository.insertSubscription(subscription = subscription)
    }
}