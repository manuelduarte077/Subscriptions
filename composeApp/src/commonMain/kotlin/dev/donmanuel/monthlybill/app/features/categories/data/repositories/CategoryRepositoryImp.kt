package dev.donmanuel.monthlybill.app.features.categories.data.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.adobe
import monthlybill.composeapp.generated.resources.applemusic
import monthlybill.composeapp.generated.resources.chycrunchyroll
import monthlybill.composeapp.generated.resources.disneyplus
import monthlybill.composeapp.generated.resources.duolingo
import monthlybill.composeapp.generated.resources.github
import monthlybill.composeapp.generated.resources.hbo
import monthlybill.composeapp.generated.resources.netflix
import monthlybill.composeapp.generated.resources.notion
import monthlybill.composeapp.generated.resources.onedrive
import monthlybill.composeapp.generated.resources.primevideo
import monthlybill.composeapp.generated.resources.spotify
import monthlybill.composeapp.generated.resources.twitch
import monthlybill.composeapp.generated.resources.x
import monthlybill.composeapp.generated.resources.youtube

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
            name = "Disney+",
            icon = Res.drawable.disneyplus,
            color = ""
        ),
        Category(
            id = 3,
            name = "HBO",
            icon = Res.drawable.hbo,
            color = ""
        ),
        Category(
            id = 4,
            name = "Amazon Prime",
            icon = Res.drawable.primevideo,
            color = ""
        ),
        Category(
            id = 5,
            name = "Duolingo",
            icon = Res.drawable.duolingo,
            color = ""
        ),
        Category(
            id = 6,
            name = "Youtube",
            icon = Res.drawable.youtube,
            color = ""
        ),
        Category(
            id = 7,
            name = "X",
            icon = Res.drawable.x,
            color = ""
        ),
        Category(
            id = 8,
            name = "Twitch",
            icon = Res.drawable.twitch,
            color = ""
        ),
        Category(
            id = 9,
            name = "Spotify",
            icon = Res.drawable.spotify,
            color = ""
        ),
        Category(
            id = 10,
            name = "One Drive",
            icon = Res.drawable.onedrive,
            color = ""
        ),
        Category(
            id = 11,
            name = "Adobe Creative Cloud",
            icon = Res.drawable.adobe,
            color = ""
        ),
        Category(
            id = 12,
            name = "Apple Music",
            icon = Res.drawable.applemusic,
            color = ""
        ),
        Category(
            id = 13,
            name = "Crunchyroll",
            icon = Res.drawable.chycrunchyroll,
            color = ""
        ),
        Category(
            id = 14,
            name = "GitHub",
            icon = Res.drawable.github,
            color = ""
        ),
        Category(
            id = 15,
            name = "Notion",
            icon = Res.drawable.notion,
            color = ""
        ),
    )
}