package dev.donmanuel.monthlybill.app.features.categories.domain.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Subscription

interface SubscriptionRepository {
    suspend fun getSubscriptions(): List<Subscription>
}