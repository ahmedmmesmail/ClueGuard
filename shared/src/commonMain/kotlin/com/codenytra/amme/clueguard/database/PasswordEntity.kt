package com.codenytra.amme.clueguard

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val password: String,
    val platform: String,
)
