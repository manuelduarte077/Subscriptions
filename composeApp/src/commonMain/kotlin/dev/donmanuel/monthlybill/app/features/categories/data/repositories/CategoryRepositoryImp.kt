package dev.donmanuel.monthlybill.app.features.categories.data.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category.SubscriptionCategory
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImp : CategoryRepository {
    override fun getSubscriptions(): Flow<List<Category>?> {
        return flow {
            emit(getSubscriptionList())
        }
    }

    fun getSubscriptionIcon(category: SubscriptionCategory): String {
        return when (category) {
            SubscriptionCategory.ENTERTAINMENT -> "🎬"
            SubscriptionCategory.UTILITIES -> "💡"
            SubscriptionCategory.FOOD -> "🍔"
            SubscriptionCategory.TRANSPORTATION -> "🚗"
            SubscriptionCategory.HEALTH -> "💊"
            SubscriptionCategory.OTHER -> "🛍️"
        }
    }

    fun getSubscriptionColor(category: SubscriptionCategory): String {
        return when (category) {
            SubscriptionCategory.ENTERTAINMENT -> "#FF5733"
            SubscriptionCategory.UTILITIES -> "#33FF57"
            SubscriptionCategory.FOOD -> "#3357FF"
            SubscriptionCategory.TRANSPORTATION -> "#FF33A1"
            SubscriptionCategory.HEALTH -> "#FF8C33"
            SubscriptionCategory.OTHER -> "#33FFF5"
        }
    }

    fun getSubscriptionList() = listOf(

        Category(
            id = 1,
            name = "Netflix",
            price = 15.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.ENTERTAINMENT,
            icon = getSubscriptionIcon(SubscriptionCategory.ENTERTAINMENT),
            color = getSubscriptionColor(SubscriptionCategory.ENTERTAINMENT),
            startDate = "2023-01-01",
            endDate = null,
            isActive = true,
            description = "Streaming service for movies and TV shows.",
            trialPeriodDays = 30,
            reminderDaysBefore = 5
        ),
        Category(
            id = 2,
            name = "Slack",
            price = 8.00,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.UTILITIES,
            icon = getSubscriptionIcon(SubscriptionCategory.UTILITIES),
            color = getSubscriptionColor(SubscriptionCategory.UTILITIES),
            startDate = "2023-02-01",
            endDate = null,
            isActive = true,
            description = "Team collaboration and messaging platform.",
            trialPeriodDays = 14,
            reminderDaysBefore = 3
        ),
        Category(
            id = 3,
            name = "Spotify",
            price = 9.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.YEARLY,
            category = SubscriptionCategory.ENTERTAINMENT,
            icon = getSubscriptionIcon(SubscriptionCategory.ENTERTAINMENT),
            color = getSubscriptionColor(SubscriptionCategory.ENTERTAINMENT),
            startDate = "2023-03-01",
            endDate = null,
            isActive = true,
            description = "Music streaming service.",
            trialPeriodDays = 30,
            reminderDaysBefore = 5,
        ),
        Category(
            id = 4,
            name = "Amazon Prime",
            price = 12.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.ENTERTAINMENT,
            icon = getSubscriptionIcon(SubscriptionCategory.ENTERTAINMENT),
            color = getSubscriptionColor(SubscriptionCategory.ENTERTAINMENT),
            startDate = "2023-04-01",
            endDate = null,
            isActive = true,
            description = "Membership for free shipping and streaming.",
            trialPeriodDays = 30,
            reminderDaysBefore = 5
        ),
        Category(
            id = 5,
            name = "Adobe Creative Cloud",
            price = 52.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.ENTERTAINMENT,
            icon = getSubscriptionIcon(SubscriptionCategory.ENTERTAINMENT),
            color = getSubscriptionColor(SubscriptionCategory.ENTERTAINMENT),
            startDate = "2023-05-01",
            endDate = null,
            isActive = true,
            description = "Creative software suite for design and editing.",
            trialPeriodDays = 7,
            reminderDaysBefore = 3
        ),
        Category(
            id = 6,
            name = "Hulu",
            price = 11.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.ENTERTAINMENT,
            icon = getSubscriptionIcon(SubscriptionCategory.ENTERTAINMENT),
            color = getSubscriptionColor(SubscriptionCategory.ENTERTAINMENT),
            startDate = "2023-06-01",
            endDate = null,
            isActive = true,
            description = "Streaming service for TV shows and movies.",
            trialPeriodDays = 30,
            reminderDaysBefore = 5
        ),
        Category(
            id = 7,
            name = "1Password",
            price = 2.99,
            currency = "USD",
            billingCycle = Category.BillingCycle.MONTHLY,
            category = SubscriptionCategory.UTILITIES,
            icon = getSubscriptionIcon(SubscriptionCategory.UTILITIES),
            color = getSubscriptionColor(SubscriptionCategory.UTILITIES),
            startDate = "2023-07-01",
            endDate = null,
            isActive = true,
            description = "Password manager and secure vault.",
            trialPeriodDays = 14,
            reminderDaysBefore = 3
        )
    )
}