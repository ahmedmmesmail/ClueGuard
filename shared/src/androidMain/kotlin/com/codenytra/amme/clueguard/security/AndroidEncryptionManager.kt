package com.codenytra.amme.clueguard.security

import android.security.keystore.KeyPermanentlyInvalidatedException
import java.security.InvalidKeyException
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AES-256-GCM encryption backed by Android Keystore.
 *
 * Stored format (version 1):
 *
 *   [ version : 1 byte ][ IV : 12 bytes ][ ciphertext + GCM tag : N + 16 bytes ]
 *
 * The version byte lets us change the algorithm/format in the future and still
 * recognise (and migrate) data written by older app versions.
 */
class AndroidEncryptionManager(
    private val keyManager: AndroidKeyManager = AndroidKeyManager()
) : EncryptionManager {

    private companion object {
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val FORMAT_VERSION: Byte = 1
        const val IV_LENGTH = 12
        const val TAG_LENGTH_BYTES = 16
        const val TAG_LENGTH_BITS = TAG_LENGTH_BYTES * 8
        const val MIN_ENCRYPTED_SIZE = 1 + IV_LENGTH + TAG_LENGTH_BYTES
    }

    // Keystore operations are blocking (the first key creation can take a
    // noticeable time), so they must never run on the main thread.
    override suspend fun encrypt(
        plaintext: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> = withContext(Dispatchers.IO) {
        encryptBlocking(plaintext, associatedData)
    }

    override suspend fun decrypt(
        encryptedData: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> = withContext(Dispatchers.IO) {
        decryptBlocking(encryptedData, associatedData)
    }

    private fun encryptBlocking(
        plaintext: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)

            // No IV is passed: Keystore generates a fresh random one for us.
            cipher.init(Cipher.ENCRYPT_MODE, keyManager.getOrCreateKey())
            cipher.updateAAD(buildAad(associatedData))

            val iv = cipher.iv
            if (iv.size != IV_LENGTH) {
                return SecurityResult.Error(SecurityError.ENCRYPTION_FAILED)
            }

            val encrypted = cipher.doFinal(plaintext)

            SecurityResult.Success(byteArrayOf(FORMAT_VERSION) + iv + encrypted)
        } catch (_: KeyPermanentlyInvalidatedException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: InvalidKeyException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: UnrecoverableKeyException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: KeyStoreException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: Exception) {
            SecurityResult.Error(SecurityError.ENCRYPTION_FAILED)
        }
    }

    private fun decryptBlocking(
        encryptedData: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        // 1) Structural checks, before touching the key at all.
        if (encryptedData.size < MIN_ENCRYPTED_SIZE ||
            encryptedData[0] != FORMAT_VERSION
        ) {
            return SecurityResult.Error(SecurityError.INVALID_CIPHERTEXT)
        }

        return try {
            // 2) Never create a key while decrypting.
            val key = keyManager.getKey()
                ?: return SecurityResult.Error(SecurityError.KEY_NOT_FOUND)

            val iv = encryptedData.copyOfRange(1, 1 + IV_LENGTH)
            val ciphertext = encryptedData.copyOfRange(1 + IV_LENGTH, encryptedData.size)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH_BITS, iv))
            cipher.updateAAD(buildAad(associatedData))

            // 3) doFinal verifies the GCM tag. Any modification of the data,
            //    the IV, the version, the AAD, or a wrong key throws here.
            SecurityResult.Success(cipher.doFinal(ciphertext))
        } catch (_: AEADBadTagException) {
            SecurityResult.Error(SecurityError.DECRYPTION_FAILED)
        } catch (_: KeyPermanentlyInvalidatedException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: InvalidKeyException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: UnrecoverableKeyException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: KeyStoreException) {
            SecurityResult.Error(SecurityError.INVALID_KEY)
        } catch (_: Exception) {
            SecurityResult.Error(SecurityError.DECRYPTION_FAILED)
        }
    }

    /**
     * Additional Authenticated Data = version byte + caller context.
     * Including the version stops an attacker from relabelling the data as a
     * different format version without the tag check failing.
     */
    private fun buildAad(associatedData: ByteArray): ByteArray =
        byteArrayOf(FORMAT_VERSION) + associatedData
}
