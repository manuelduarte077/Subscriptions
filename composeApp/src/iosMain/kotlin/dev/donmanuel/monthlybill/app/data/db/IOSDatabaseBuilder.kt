package dev.donmanuel.monthlybill.app.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun iosDatabaseBuilder(): RoomDatabase.Builder<RoomDB> {
    val dbFilePath = documentDirectory() + "/monthlybill.db"

    return Room.databaseBuilder(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(documentDirectory?.path)
}