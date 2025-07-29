package dev.donmanuel.monthlybill.app.features.bill.data.repository

import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SubscriptionRepository(
    private val database: RoomDB
) {

    suspend fun insertSubscription(subscription: Subscription) {
        withContext(Dispatchers.IO) {
            database.subscriptionDao().insertSubscription(subscription)
        }
    }

    suspend fun updateSubscription(subscription: Subscription) {
        withContext(Dispatchers.IO) {
            database.subscriptionDao().updateSubscription(subscription)
        }
    }

    suspend fun deleteSubscription(subscription: Subscription) {
        withContext(Dispatchers.IO) {
            database.subscriptionDao().deleteSubscription(subscription)
        }
    }

    fun getAllSubscriptions(): Flow<List<Subscription>> {
        return database.subscriptionDao().getAllSubscriptions()
    }

    suspend fun getSubscriptionById(id: Long): Subscription? {
        return withContext(Dispatchers.IO) {
            database.subscriptionDao().getSubscriptionById(id)
        }
    }

}