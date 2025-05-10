package dev.donmanuel.monthlybill.app.features.bill.data.repository

import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(
    private val database: RoomDB
) {
    private val dispatcher = Dispatchers.IO

    suspend fun insertSubscription(subscription: Subscription) {
        with(dispatcher) {
            database.subscriptionDao().insertSubscription(subscription)
        }
    }

    suspend fun updateSubscription(subscription: Subscription) {
        with(dispatcher) {
            database.subscriptionDao().updateSubscription(subscription)
        }
    }

    suspend fun deleteSubscription(subscription: Subscription) {
        with(dispatcher) {
            database.subscriptionDao().deleteSubscription(subscription)
        }
    }

    fun getAllSubscriptions(): Flow<List<Subscription>> {
        return database.subscriptionDao().getAllSubscriptions()
    }

    suspend fun getSubscriptionById(id: Long): Subscription? {
        return with(dispatcher) {
            database.subscriptionDao().getSubscriptionById(id)
        }
    }

}