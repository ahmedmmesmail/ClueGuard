package com.codenytra.amme.clueguard

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.codenytra.amme.clueguard.data.database.ClueGuardDatabase
import kotlinx.coroutines.Dispatchers

fun getDatabaseBuilder(
    context: Context
): RoomDatabase.Builder<ClueGuardDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("clueguard.db")

    return Room.databaseBuilder<ClueGuardDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getDatabase(context: Context): ClueGuardDatabase {
    return getDatabaseBuilder(context)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}