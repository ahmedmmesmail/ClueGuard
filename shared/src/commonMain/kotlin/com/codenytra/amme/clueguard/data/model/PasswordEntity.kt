package com.codenytra.amme.clueguard.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * One saved credential.
 *
 * [encryptedPassword] holds the output of EncryptionManager.encrypt(), NOT the
 * password. The plaintext password is never written to the database.
 */
@Entity
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val platform: String,
    val username: String,
    val encryptedPassword: ByteArray,
) {
    // Kotlin's generated equals/hashCode compare arrays by reference, which is
    // wrong for ByteArray. Compare the content instead.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PasswordEntity) return false
        return id == other.id &&
            platform == other.platform &&
            username == other.username &&
            encryptedPassword.contentEquals(other.encryptedPassword)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + platform.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + encryptedPassword.contentHashCode()
        return result
    }
}
