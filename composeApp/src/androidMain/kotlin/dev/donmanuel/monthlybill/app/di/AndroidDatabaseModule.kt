package dev.donmanuel.monthlybill.app.di

import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.data.db.androidDatabaseBuilder
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDatabaseModule = module {
    single<RoomDatabase.Builder<RoomDB>> { androidDatabaseBuilder(androidContext()) }
}