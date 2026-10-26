package com.codenytra.amme.clueguard.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.codenytra.amme.clueguard.data.model.PasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Insert
    suspend fun insert(password: PasswordEntity)

    @Query("SELECT count(*) FROM passwordEntity")
    suspend fun count(): Int

    @Query("SELECT * FROM passwordEntity")
    fun getAllAsFlow(): Flow<List<PasswordEntity>>

    @Query("SELECT * FROM passwordEntity WHERE id = :id")
    suspend fun findById(id: Int): PasswordEntity?

    @Query("DELETE FROM passwordEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
}
