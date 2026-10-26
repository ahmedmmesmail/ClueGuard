package com.codenytra.amme.clueguard.security

/**
 * Platform-independent contract for encrypting and decrypting sensitive bytes.
 *
 * - Android implements it with AES-256-GCM + Android Keystore.
 * - iOS will implement it later with the Apple Keychain.
 *
 * The rest of the app (repository, ViewModel, UI) depends on this interface
 * only, so it never knows which platform it is running on.
 *
 * The encryption key never appears in this API: it lives inside the platform's
 * secure hardware/OS storage and is never handed to our code.
 */
interface EncryptionManager {

    /**
     * @param plaintext        the secret to protect.
     * @param associatedData   public "context" label that is authenticated but
     *                         NOT encrypted. The exact same value must be passed
     *                         to [decrypt], otherwise decryption fails.
     */
    suspend fun encrypt(
        plaintext: ByteArray,
        associatedData: ByteArray = ByteArray(0)
    ): SecurityResult<ByteArray>

    suspend fun decrypt(
        encryptedData: ByteArray,
        associatedData: ByteArray = ByteArray(0)
    ): SecurityResult<ByteArray>
}
