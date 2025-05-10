package dev.donmanuel.monthlybill.app.di

import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.data.db.desktopDatabaseBuilder
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import org.koin.dsl.module

val desktopDatabaseModule = module {
    single<RoomDatabase.Builder<RoomDB>> {
        desktopDatabaseBuilder()
    }
}