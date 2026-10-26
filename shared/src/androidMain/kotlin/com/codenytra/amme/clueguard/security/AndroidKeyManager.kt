package com.codenytra.amme.clueguard.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Owns the single AES-256 key used by the app.
 *
 * The key is generated INSIDE Android Keystore and is non-exportable: our code
 * only receives a handle ([SecretKey]) that can be used for encrypt/decrypt
 * operations, but the raw key bytes can never be read, logged or backed up.
 */
class AndroidKeyManager {

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "ClueGuardKey"
        const val KEY_SIZE_BITS = 256
    }

    // Loaded once instead of on every access.
    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }

    /**
     * Returns the existing key, or null if there is none.
     * Used by decryption: if the key is missing we must NOT silently create a
     * new one, because a new key can never decrypt old data.
     */
    fun getKey(): SecretKey? = keyStore.getKey(KEY_ALIAS, null) as? SecretKey

    /**
     * Returns the existing key, or creates it the first time it is needed.
     * Used by encryption. Synchronized so two coroutines can't create two keys.
     */
    @Synchronized
    fun getOrCreateKey(): SecretKey = getKey() ?: createKey()

    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(KEY_SIZE_BITS)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            // Keystore must generate a fresh random IV for every encryption.
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    /** Deleting the key makes ALL data encrypted with it unrecoverable. */
    @Synchronized
    fun deleteKey() {
        if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.deleteEntry(KEY_ALIAS)
        }
    }
}
