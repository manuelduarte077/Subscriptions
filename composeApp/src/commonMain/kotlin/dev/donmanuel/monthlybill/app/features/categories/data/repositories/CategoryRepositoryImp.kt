package dev.donmanuel.monthlybill.app.features.categories.data.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.apple
import monthlybill.composeapp.generated.resources.disneyplus
import monthlybill.composeapp.generated.resources.hbo
import monthlybill.composeapp.generated.resources.netflix
import monthlybill.composeapp.generated.resources.primevideo
import monthlybill.composeapp.generated.resources.spotify

class CategoryRepositoryImp : CategoryRepository {
    override fun getSubscriptions(): Flow<List<Category>?> {
        return flow {
            emit(getSubscriptionList())
        }
    }

    fun getSubscriptionList() = listOf(

        Category(
            id = 1,
            name = "Netflix",
            icon = Res.drawable.netflix,
            color = ""
        ),
        Category(
            id = 2,
            name = "Spotify",
            icon = Res.drawable.spotify,
            color = ""
        ),
        Category(
            id = 3,
            name = "Disney+",
            icon = Res.drawable.disneyplus,
            color = ""
        ),
        Category(
            id = 4,
            name = "HBO",
            icon = Res.drawable.hbo,
            color = ""
        ),
        Category(
            id = 5,
            name = "Amazon Prime",
            icon = Res.drawable.primevideo,
            color = ""
        ),
        Category(
            id = 6,
            name = "Apple TV+",
            icon = Res.drawable.apple,
            color = ""
        ),
    )
}