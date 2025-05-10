package dev.donmanuel.monthlybill.app.di

import dev.donmanuel.monthlybill.app.features.bill.data.local.CreateDatabase
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import dev.donmanuel.monthlybill.app.features.bill.data.repository.SubscriptionRepository
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.DeleteSubscriptionUseCase
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.GetAllSubscriptionsUseCase
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.GetSubscriptionByIdUseCase
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.InsertSubscriptionUseCase
import dev.donmanuel.monthlybill.app.features.bill.domain.usecase.UpdateSubscriptionUseCase
import dev.donmanuel.monthlybill.app.features.categories.data.repositories.CategoryRepositoryImp
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.CategoryRepository
import dev.donmanuel.monthlybill.app.features.categories.domain.usecases.GetCategoriesUseCase
import dev.donmanuel.monthlybill.app.features.categories.presentation.viewmodel.CategoriesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    // Database
    single<RoomDB> { CreateDatabase(get()).getDatabase() }

    //  Categories
    viewModelOf(::CategoriesViewModel)
    singleOf(::GetCategoriesUseCase)
    single<CategoryRepository> { CategoryRepositoryImp() }

    // Use Cases Bill
    singleOf(::GetSubscriptionByIdUseCase)
    singleOf(::InsertSubscriptionUseCase)
    singleOf(::UpdateSubscriptionUseCase)
    singleOf(::GetAllSubscriptionsUseCase)
    singleOf(::DeleteSubscriptionUseCase)

    // Repositories
    singleOf(::SubscriptionRepository)
}