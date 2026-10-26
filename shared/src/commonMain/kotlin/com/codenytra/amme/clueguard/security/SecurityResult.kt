package com.codenytra.amme.clueguard.security

sealed interface SecurityResult<out T> {
    data class Success<T>(val data: T) : SecurityResult<T>
    data class Error(val cause: SecurityError) : SecurityResult<Nothing>

}

enum class SecurityError {
    INVALID_KEY,
    ENCRYPTION_FAILED,
    DECRYPTION_FAILED,
    INVALID_CIPHERTEXT,
    KEY_NOT_FOUND,
    STORAGE_FAILED
}