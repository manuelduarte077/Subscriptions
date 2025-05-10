package dev.donmanuel.monthlybill.app.features.bill.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import dev.donmanuel.monthlybill.app.features.bill.data.local.dao.SubscriptionDao
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription

@Database(
    entities = [Subscription::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoomDB> {
    override fun initialize(): RoomDB
}