package dev.donmanuel.monthlybill.app.features.bill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val currency: String,
    val billingCycle: String,
    val category: String,
    val icon: String? = null,
    val color: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val isActive: Boolean = true,
    val description: String? = null,
    val trialPeriodDays: Int? = null,
    val reminderDaysBefore: Int? = null
)