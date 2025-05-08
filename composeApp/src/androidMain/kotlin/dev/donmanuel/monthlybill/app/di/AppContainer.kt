package dev.donmanuel.monthlybill.app.di

import dev.donmanuel.monthlybill.app.features.categories.data.repositories.SubscriptionRepositoryImpl
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.SubscriptionRepository
import dev.donmanuel.monthlybill.app.features.categories.domain.usecases.GetSubscriptionsUseCase
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel

object AppContainer {
    private val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepositoryImpl()
    }

    val getSubscriptionsUseCase: GetSubscriptionsUseCase by lazy {
        GetSubscriptionsUseCase(subscriptionRepository)
    }

    val categoriesViewModel: CategoriesViewModel by lazy {
        CategoriesViewModel(getSubscriptionsUseCase)
    }
}