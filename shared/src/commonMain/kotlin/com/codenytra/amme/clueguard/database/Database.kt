package com.codenytra.amme.clueguard

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.codenytra.amme.clueguard.database.PasswordDao

@Database(entities = [PasswordEntity::class], version = 1)
@ConstructedBy(ClueGuardDatabaseConstructor::class)
abstract class ClueGuardDatabase : RoomDatabase() {
    abstract val passwordDao: PasswordDao
}

@Suppress("KotlinNoActualForExpect")
expect object ClueGuardDatabaseConstructor : RoomDatabaseConstructor<ClueGuardDatabase> {
    override fun initialize(): ClueGuardDatabase
}