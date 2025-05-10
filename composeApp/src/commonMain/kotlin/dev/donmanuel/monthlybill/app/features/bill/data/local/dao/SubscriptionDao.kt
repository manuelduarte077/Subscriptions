package dev.donmanuel.monthlybill.app.features.bill.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscription")
    fun getAllSubscriptions(): Flow<List<Subscription>>

    @Query("SELECT * FROM subscription WHERE id = :id")
    suspend fun getSubscriptionById(id: Long): Subscription?

    @Insert
    suspend fun insertSubscription(subscription: Subscription)

    @Update
    suspend fun updateSubscription(subscription: Subscription)

    @Delete
    suspend fun deleteSubscription(subscription: Subscription)
}