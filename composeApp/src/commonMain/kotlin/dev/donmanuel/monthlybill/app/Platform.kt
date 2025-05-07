package dev.donmanuel.monthlybill.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform