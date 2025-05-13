package dev.donmanuel.monthlybill.app.features.categories.domain.entities

import org.jetbrains.compose.resources.DrawableResource

data class Category(
    val id: Long = 0,
    val name: String,
    val color: String,
    val icon: DrawableResource,
)