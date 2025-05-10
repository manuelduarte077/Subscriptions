package dev.donmanuel.monthlybill.app.features.categories.domain.entities

data class Category(
    val id: Long = 0,
    val name: String,
    val price: Double,
    val currency: String,
    val billingCycle: BillingCycle,
    val category: SubscriptionCategory,
    val icon: String? = null,
    val color: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val isActive: Boolean = true,
    val description: String? = null,
    val trialPeriodDays: Int? = null,
    val reminderDaysBefore: Int? = null
) {

    enum class BillingCycle {
        MONTHLY, YEARLY, QUARTERLY, WEEKLY,
    }

    enum class SubscriptionCategory {
        ENTERTAINMENT, UTILITIES, FOOD, TRANSPORTATION, HEALTH, OTHER
    }

    companion object {
        fun create(
            id: Long = 0,
            name: String,
            price: Double,
            currency: String,
            billingCycle: BillingCycle,
            category: SubscriptionCategory,
            icon: String? = null,
            color: String? = null,
            startDate: String,
            endDate: String? = null,
            isActive: Boolean = true,
            description: String? = null,
            trialPeriodDays: Int? = null,
            reminderDaysBefore: Int? = null
        ) = Category(
            id,
            name,
            price,
            currency,
            billingCycle,
            category,
            icon,
            color,
            startDate,
            endDate,
            isActive,
            description,
            trialPeriodDays,
            reminderDaysBefore
        )
    }

}