package dev.donmanuel.monthlybill.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.donmanuel.monthlybill.app.di.desktopDatabaseModule
import dev.donmanuel.monthlybill.app.di.sharedModule
import org.koin.core.context.startKoin

fun main() {

    startKoin {
        modules(
            sharedModule, desktopDatabaseModule
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication, title = "Monthly Bill"
        ) {
            App()
        }
    }
}