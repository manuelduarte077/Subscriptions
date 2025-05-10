package dev.donmanuel.monthlybill.app.features.bill.data.repository

import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import dev.donmanuel.monthlybill.app.features.bill.data.model.SubscriptionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(
    private val database: RoomDB
) {
    private val dispatcher = Dispatchers.IO

    suspend fun insertSubscription(subscription: SubscriptionEntity) {
        with(dispatcher) {
            database.subscriptionDao().insertSubscription(subscription)
        }
    }

    suspend fun updateSubscription(subscription: SubscriptionEntity) {
        with(dispatcher) {
            database.subscriptionDao().updateSubscription(subscription)
        }
    }

    suspend fun deleteSubscription(subscription: SubscriptionEntity) {
        with(dispatcher) {
            database.subscriptionDao().deleteSubscription(subscription)
        }
    }

    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>> {
        return database.subscriptionDao().getAllSubscriptions()
    }

    suspend fun getSubscriptionById(id: Long): SubscriptionEntity? {
        return with(dispatcher) {
            database.subscriptionDao().getSubscriptionById(id)
        }
    }

}