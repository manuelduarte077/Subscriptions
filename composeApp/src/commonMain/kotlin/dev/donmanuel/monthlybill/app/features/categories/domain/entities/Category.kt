package dev.donmanuel.monthlybill.app.features.categories.domain.entities

import org.jetbrains.compose.resources.DrawableResource

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: DrawableResource,
    val plans: List<Plan>,
    val description: String,
)

data class Plan(
    val name: String,
    val price: String,
)