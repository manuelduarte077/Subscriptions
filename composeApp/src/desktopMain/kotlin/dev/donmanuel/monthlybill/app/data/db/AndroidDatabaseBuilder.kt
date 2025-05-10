package dev.donmanuel.monthlybill.app.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB
import java.io.File

fun desktopDatabaseBuilder(): RoomDatabase.Builder<RoomDB> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "monthlybill.db")

    return Room.databaseBuilder(
        dbFile.absolutePath
    )
}