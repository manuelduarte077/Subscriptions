package dev.donmanuel.monthlybill.app.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.donmanuel.monthlybill.app.features.bill.data.local.RoomDB

fun androidDatabaseBuilder(context: Context): RoomDatabase.Builder<RoomDB> {
    val dbFile = context.applicationContext.getDatabasePath("todo.db")

    return Room.databaseBuilder(
        context,
        dbFile.absolutePath
    )
}