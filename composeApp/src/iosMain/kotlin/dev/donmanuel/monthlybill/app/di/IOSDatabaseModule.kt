package dev.donmanuel.monthlybill.app.di

import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.data.db.iosDatabaseBuilder
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import org.koin.dsl.module

val iosDatabaseModule = module {
    single<RoomDatabase.Builder<RoomDB>> { iosDatabaseBuilder() }
}