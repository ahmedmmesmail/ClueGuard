@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, UnsafeNumber::class)

package com.codenytra.amme.clueguard.security

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFRelease
import platform.Foundation.CFBridgingRelease
import platform.Foundation.NSData
import platform.Security.SecKeyCopyPublicKey
import platform.Security.SecKeyRef
import platform.Security.SecKeyCreateDecryptedData
import platform.Security.SecKeyCreateEncryptedData
import platform.Security.kSecKeyAlgorithmECIESEncryptionCofactorX963SHA256AESGCM

/**
 * iOS implementation of [EncryptionManager] using Apple's Security framework.
 *
 * Algorithm: ECIES (P-256, cofactor X9.63, SHA-256) + AES-GCM, done by Apple.
 *   encrypt -> uses the PUBLIC key  (anyone could do this step)
 *   decrypt -> uses the PRIVATE key (never leaves the Secure Enclave/Keychain)
 *
 * Stored format (version 2):  [ version : 1 byte ][ Apple ECIES output ]
 * (Version 1 is the Android AES-GCM format; the two platforms never share data.)
 */
internal class IosEncryptionManager(
    private val keyManager: IosKeyManager = IosKeyManager()
) : EncryptionManager {

    private companion object {
        const val FORMAT_VERSION: Byte = 2
        // version byte + ephemeral public key (65) + GCM tag (16) at minimum.
        const val MIN_ENCRYPTED_SIZE = 1 + 65 + 16
        val ALGORITHM = kSecKeyAlgorithmECIESEncryptionCofactorX963SHA256AESGCM
    }

    // Two coroutines must not create two keys at the same time.
    private val keyMutex = Mutex()

    override suspend fun encrypt(
        plaintext: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        // Keychain / Secure Enclave calls are blocking: keep them off the main thread.
        return withContext(Dispatchers.Default) {
            val key = keyMutex.withLock { keyManager.getOrCreateKey() }
                ?: return@withContext SecurityResult.Error(SecurityError.INVALID_KEY)

            try {
                encryptBlocking(key, plaintext, associatedData)
            } finally {
                CFRelease(key)
            }
        }
    }

    override suspend fun decrypt(
        encryptedData: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        // 1) Structural checks, before touching the key at all.
        if (encryptedData.size < MIN_ENCRYPTED_SIZE ||
            encryptedData[0] != FORMAT_VERSION
        ) {
            return SecurityResult.Error(SecurityError.INVALID_CIPHERTEXT)
        }

        return withContext(Dispatchers.Default) {
            // 2) Never create a key while decrypting.
            val key = keyMutex.withLock { keyManager.getKey() }
                ?: return@withContext SecurityResult.Error(SecurityError.KEY_NOT_FOUND)

            try {
                decryptBlocking(key, encryptedData, associatedData)
            } finally {
                CFRelease(key)
            }
        }
    }

    private fun encryptBlocking(
        privateKey: SecKeyRef,
        plaintext: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        val publicKey = SecKeyCopyPublicKey(privateKey)
            ?: return SecurityResult.Error(SecurityError.INVALID_KEY)

        // Context = version + caller context (same idea as the Android AAD).
        val payload = PayloadFraming.wrap(buildContext(associatedData), plaintext)

        try {
            val encrypted = transform(payload) { input, error ->
                SecKeyCreateEncryptedData(publicKey, ALGORITHM, input, error)
            } ?: return SecurityResult.Error(SecurityError.ENCRYPTION_FAILED)

            return SecurityResult.Success(byteArrayOf(FORMAT_VERSION) + encrypted)
        } finally {
            payload.fill(0)
            CFRelease(publicKey)
        }
    }

    private fun decryptBlocking(
        privateKey: SecKeyRef,
        encryptedData: ByteArray,
        associatedData: ByteArray
    ): SecurityResult<ByteArray> {
        val ciphertext = encryptedData.copyOfRange(1, encryptedData.size)

        // 3) Apple verifies the GCM tag here: any modification of the data
        //    or a wrong key makes this return null.
        val payload = transform(ciphertext) { input, error ->
            SecKeyCreateDecryptedData(privateKey, ALGORITHM, input, error)
        } ?: return SecurityResult.Error(SecurityError.DECRYPTION_FAILED)

        try {
            // 4) Check the context that was encrypted together with the data.
            val plaintext = PayloadFraming.unwrap(buildContext(associatedData), payload)
                ?: return SecurityResult.Error(SecurityError.DECRYPTION_FAILED)

            return SecurityResult.Success(plaintext)
        } finally {
            payload.fill(0)
        }
    }

    /**
     * Runs one SecKey operation on [bytes]. Returns the resulting bytes, or
     * null if the operation failed. Handles the Core Foundation memory rules.
     */
    private fun transform(
        bytes: ByteArray,
        operation: (CFDataRef?, CPointer<CFErrorRefVar>) -> CFDataRef?
    ): ByteArray? = memScoped {
        val error = alloc<CFErrorRefVar>()
        try {
            withCfRetained(bytes.toNSData()) { cfInput ->
                val input: CFDataRef? = cfInput?.reinterpret()
                val output = operation(input, error.ptr) ?: return@withCfRetained null
                // Ownership of "output" moves to Foundation, which frees it.
                (CFBridgingRelease(output) as? NSData)?.toByteArray()
            }
        } finally {
            error.value?.let { CFRelease(it) }
        }
    }

    private fun buildContext(associatedData: ByteArray): ByteArray =
        byteArrayOf(FORMAT_VERSION) + associatedData
}
