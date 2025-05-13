package dev.donmanuel.monthlybill.app.features.categories.data.repositories

import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Plan
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
            plans = listOf(
                Plan("Basic", "8.99"),
                Plan("Standard", "15.49"),
                Plan("Premium", "19.99")
            ),
            description = "Netflix is a streaming service that offers a wide variety of award-winning TV shows, movies, anime, documentaries, and more on thousands of internet-connected devices."
        ),
        Category(
            id = 2,
            name = "Disney+",
            icon = Res.drawable.disneyplus,
            plans = listOf(
                Plan("Basic", "7.99"),
                Plan("Standard", "10.99"),
                Plan("Premium", "13.99")
            ),
            description = "Disney+ is the streaming home of Disney, Pixar, Marvel, Star Wars, and National Geographic. Stream the best movies, shows, and documentaries from Disney and more."
        ),
        Category(
            id = 3,
            name = "HBO",
            icon = Res.drawable.hbo,
            plans = listOf(
                Plan("Basic", "14.99"),
                Plan("Standard", "19.99"),
                Plan("Premium", "24.99")
            ),
            description = "HBO Max is a streaming service that offers a wide variety of movies, shows, and documentaries from HBO and other WarnerMedia brands."
        ),
        Category(
            id = 4,
            name = "Amazon Prime",
            icon = Res.drawable.primevideo,
            plans = listOf(
                Plan("Basic", "8.99"),
                Plan("Standard", "12.99"),
                Plan("Premium", "15.99")
            ),
            description = "Amazon Prime Video is a streaming service that offers a wide variety of movies, shows, and documentaries from Amazon and other studios."
        ),
        Category(
            id = 5,
            name = "Duolingo",
            icon = Res.drawable.duolingo,
            plans = listOf(
                Plan("Basic", "6.99"),
                Plan("Standard", "12.99"),
                Plan("Premium", "19.99")
            ),
            description = "Duolingo is a language-learning platform that offers courses in various languages through interactive lessons and exercises."
        ),
        Category(
            id = 6,
            name = "Youtube",
            icon = Res.drawable.youtube,
            plans = listOf(
                Plan("Basic", "11.99"),
                Plan("Standard", "15.99"),
                Plan("Premium", "19.99")
            ),
            description = "YouTube is a video-sharing platform where users can upload, view, and share videos. It offers a wide variety of content, including music videos, vlogs, tutorials, and more."
        ),
        Category(
            id = 7,
            name = "X",
            icon = Res.drawable.x,
            plans = listOf(
                Plan("Basic", "4.99"),
                Plan("Standard", "8.99"),
                Plan("Premium", "12.99")
            ),
            description = "X is a social media platform that allows users to share and discover short-form content, including text, images, and videos. It is known for its real-time updates and trending topics."
        ),
        Category(
            id = 8,
            name = "Twitch",
            icon = Res.drawable.twitch,
            plans = listOf(
                Plan("Basic", "4.99"),
                Plan("Standard", "9.99"),
                Plan("Premium", "14.99")
            ),
            description = "Twitch is a live streaming platform primarily focused on video game streaming, but it also includes streams related to music, art, and other creative content."
        ),
        Category(
            id = 9,
            name = "Spotify",
            icon = Res.drawable.spotify,
            plans = listOf(
                Plan("Basic", "9.99"),
                Plan("Standard", "14.99"),
                Plan("Premium", "19.99")
            ),
            description = "Spotify is a music streaming service that offers a vast library of songs, albums, and playlists. Users can create and share playlists, discover new music, and listen to podcasts."
        ),
        Category(
            id = 10,
            name = "One Drive",
            icon = Res.drawable.onedrive,
            plans = listOf(
                Plan("Basic", "1.99"),
                Plan("Standard", "3.99"),
                Plan("Premium", "5.99")
            ),
            description = "OneDrive is a cloud storage service from Microsoft that allows users to store files and data online securely. It offers seamless integration with Microsoft Office applications."
        ),
        Category(
            id = 11,
            name = "Adobe Creative Cloud",
            icon = Res.drawable.adobe,
            plans = listOf(
                Plan("Basic", "9.99"),
                Plan("Standard", "19.99"),
                Plan("Premium", "29.99")
            ),
            description = "Adobe Creative Cloud is a suite of software applications and services for graphic design, video editing, web development, photography, and more."
        ),
        Category(
            id = 12,
            name = "Apple Music",
            icon = Res.drawable.applemusic,
            plans = listOf(
                Plan("Basic", "9.99"),
                Plan("Standard", "14.99"),
                Plan("Premium", "19.99")
            ),
            description = "Apple Music is a music streaming service that offers access to millions of songs, curated playlists, and radio stations. It allows users to stream music on-demand and download songs for offline listening."
        ),
        Category(
            id = 13,
            name = "Crunchyroll",
            icon = Res.drawable.chycrunchyroll,
            plans = listOf(
                Plan("Basic", "7.99"),
                Plan("Standard", "9.99"),
                Plan("Premium", "14.99")
            ),
            description = "Crunchyroll is a streaming service that specializes in anime, manga, and Asian dramas. It offers a wide variety of content for fans of Japanese pop culture."
        ),
        Category(
            id = 14,
            name = "GitHub",
            icon = Res.drawable.github,
            plans = listOf(
                Plan("Basic", "4.99"),
                Plan("Standard", "8.99"),
                Plan("Premium", "12.99")
            ),
            description = "GitHub is a platform for version control and collaboration, allowing developers to work on projects together from anywhere."
        ),
        Category(
            id = 15,
            name = "Notion",
            icon = Res.drawable.notion,
            plans = listOf(
                Plan("Basic", "4.99"),
                Plan("Standard", "8.99"),
                Plan("Premium", "12.99")
            ),
            description = "Notion is a productivity and collaboration tool that combines note-taking, task management, and project planning into one platform."
        ),
    )
}