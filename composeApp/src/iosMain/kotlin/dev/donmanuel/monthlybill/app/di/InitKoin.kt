package dev.donmanuel.monthlybill.app.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(iosDatabaseModule, sharedModule)
    }
}