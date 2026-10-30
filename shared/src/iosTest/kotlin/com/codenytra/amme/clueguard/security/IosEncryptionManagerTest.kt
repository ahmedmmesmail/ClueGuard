package com.codenytra.amme.clueguard.security

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import platform.CoreFoundation.CFRelease
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** Runs on the iOS Simulator (see .github/workflows/ios-check.yml). */
@OptIn(ExperimentalForeignApi::class)
class IosEncryptionManagerTest {

    private val keyManager = IosKeyManager()
    private val manager = IosEncryptionManager(keyManager)

    private val context = "clueguard.password.v1".encodeToByteArray()
    private val secret = "P@ssw0rd-عربي".encodeToByteArray()

    @BeforeTest
    fun setUp() = keyManager.deleteKey()

    @AfterTest
    fun tearDown() = keyManager.deleteKey()

    private suspend fun encryptOk(): ByteArray {
        val result = manager.encrypt(secret, context)
        assertIs<SecurityResult.Success<ByteArray>>(result)
        return result.data
    }

    private fun assertError(expected: SecurityError, result: SecurityResult<ByteArray>) {
        assertIs<SecurityResult.Error>(result)
        assertEquals(expected, result.cause)
    }

    /**
     * TEMPORARY. Finds which native call crashes (segfault).
     * Run the CI with `--info`, then look at the LAST "STEP" line printed
     * before the crash. Delete this test once the crash is fixed.
     */
    @Test
    fun diagnostics() = runBlocking {
        println("STEP 1: deleteKey")
        keyManager.deleteKey()

        println("STEP 2: getKey (expected: no key)")
        val existing = keyManager.getKey()
        println("STEP 2 done: key == null -> ${existing == null}")
        if (existing != null) CFRelease(existing)

        println("STEP 3: getOrCreateKey (tries Secure Enclave first, then software key)")
        val created = keyManager.getOrCreateKey()
        println("STEP 3 done: key == null -> ${created == null}")
        if (created != null) CFRelease(created)

        println("STEP 4: encrypt")
        val encrypted = manager.encrypt(secret, context)
        println("STEP 4 done: $encrypted".take(80))

        if (encrypted is SecurityResult.Success) {
            println("STEP 5: decrypt")
            val decrypted = manager.decrypt(encrypted.data, context)
            val ok = decrypted is SecurityResult.Success && decrypted.data.contentEquals(secret)
            println("STEP 5 done: round trip ok -> $ok")
        } else if (encrypted is SecurityResult.Error) {
            println("STEP 4 error cause: ${encrypted.cause}")
        }
    }

    @Test
    fun roundTrip() = runBlocking {
        val encrypted = encryptOk()
        val decrypted = manager.decrypt(encrypted, context)
        assertIs<SecurityResult.Success<ByteArray>>(decrypted)
        assertContentEquals(secret, decrypted.data)
    }

    @Test
    fun sameInputGivesDifferentOutput() = runBlocking {
        assertTrue(!encryptOk().contentEquals(encryptOk()))
    }

    @Test
    fun ciphertextDoesNotContainPlaintext() = runBlocking {
        val encrypted = encryptOk().decodeToString()
        assertTrue(!encrypted.contains("P@ssw0rd"))
    }

    @Test
    fun tamperedDataIsRejected() = runBlocking {
        val encrypted = encryptOk()
        encrypted[encrypted.size - 3] = (encrypted[encrypted.size - 3].toInt() xor 1).toByte()
        assertError(SecurityError.DECRYPTION_FAILED, manager.decrypt(encrypted, context))
    }

    @Test
    fun wrongContextIsRejected() = runBlocking {
        val encrypted = encryptOk()
        assertError(
            SecurityError.DECRYPTION_FAILED,
            manager.decrypt(encrypted, "other".encodeToByteArray())
        )
    }

    @Test
    fun badVersionIsRejected() = runBlocking {
        val encrypted = encryptOk()
        encrypted[0] = 99
        assertError(SecurityError.INVALID_CIPHERTEXT, manager.decrypt(encrypted, context))
    }

    @Test
    fun tooShortIsRejected() = runBlocking {
        assertError(SecurityError.INVALID_CIPHERTEXT, manager.decrypt(ByteArray(10), context))
    }

    @Test
    fun missingKeyIsReportedAndNotRecreated() = runBlocking {
        val encrypted = encryptOk()
        keyManager.deleteKey()

        assertError(SecurityError.KEY_NOT_FOUND, manager.decrypt(encrypted, context))
        assertEquals(null, keyManager.getKey())
    }

    @Test
    fun keyIsReusedBetweenCalls() = runBlocking {
        val first = encryptOk()
        // A second call must reuse the same key, so the first blob still opens.
        encryptOk()
        val decrypted = manager.decrypt(first, context)
        assertIs<SecurityResult.Success<ByteArray>>(decrypted)
        assertNotEquals(0, decrypted.data.size)
    }
}