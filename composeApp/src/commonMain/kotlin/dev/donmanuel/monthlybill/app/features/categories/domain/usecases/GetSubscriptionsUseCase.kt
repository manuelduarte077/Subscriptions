package dev.donmanuel.monthlybill.app.features.categories.domain.usecases

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Subscription
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.SubscriptionRepository

class GetSubscriptionsUseCase(private val subscriptionRepository: SubscriptionRepository) {
    suspend operator fun invoke(): List<Subscription> {
        return subscriptionRepository.getSubscriptions()
    }
}