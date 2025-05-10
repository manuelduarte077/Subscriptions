package dev.donmanuel.monthlybill.app

import android.app.Application
import dev.donmanuel.monthlybill.app.di.androidDatabaseModule
import dev.donmanuel.monthlybill.app.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MonthlyBillApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MonthlyBillApplication)
            modules(androidDatabaseModule, sharedModule)
        }
    }
}