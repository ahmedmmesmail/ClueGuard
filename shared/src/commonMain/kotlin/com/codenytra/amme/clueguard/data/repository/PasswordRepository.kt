package com.codenytra.amme.clueguard.data.repository

import com.codenytra.amme.clueguard.data.database.PasswordDao
import com.codenytra.amme.clueguard.data.model.PasswordEntity
import com.codenytra.amme.clueguard.security.EncryptionManager
import com.codenytra.amme.clueguard.security.SecurityError
import com.codenytra.amme.clueguard.security.SecurityResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** What the list screen needs. Deliberately has NO password field. */
data class PasswordSummary(
    val id: Int,
    val platform: String,
    val username: String,
)

/**
 * The ONLY place where plaintext passwords meet the database:
 *   save   : plaintext -> encrypt -> DAO
 *   reveal : DAO -> decrypt -> plaintext (only when the user asks for it)
 */
class PasswordRepository(
    private val dao: PasswordDao,
    private val encryption: EncryptionManager,
) {

    private companion object {
        // Authenticated (not secret) label: ties the ciphertext to its purpose.
        val PASSWORD_CONTEXT = "clueguard.password.v1".encodeToByteArray()
    }

    suspend fun add(
        platform: String,
        username: String,
        password: String,
    ): SecurityResult<Unit> {
        val plainBytes = password.encodeToByteArray()
        return try {
            when (val result = encryption.encrypt(plainBytes, PASSWORD_CONTEXT)) {
                is SecurityResult.Success -> {
                    dao.insert(
                        PasswordEntity(
                            platform = platform,
                            username = username,
                            encryptedPassword = result.data,
                        )
                    )
                    SecurityResult.Success(Unit)
                }
                is SecurityResult.Error -> result
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            SecurityResult.Error(SecurityError.STORAGE_FAILED)
        } finally {
            plainBytes.fill(0) // best-effort wipe of the plaintext copy
        }
    }

    /** For the list screen: no decryption happens here. */
    fun observeAll(): Flow<List<PasswordSummary>> =
        dao.getAllAsFlow().map { list ->
            list.map { PasswordSummary(it.id, it.platform, it.username) }
        }

    /** Decrypts ONE password, only when the user taps "show" / "copy". */
    suspend fun reveal(id: Int): SecurityResult<String> {
        val entity = try {
            dao.findById(id)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            return SecurityResult.Error(SecurityError.STORAGE_FAILED)
        } ?: return SecurityResult.Error(SecurityError.STORAGE_FAILED)

        return when (val result = encryption.decrypt(entity.encryptedPassword, PASSWORD_CONTEXT)) {
            is SecurityResult.Success -> {
                val text = result.data.decodeToString()
                result.data.fill(0)
                SecurityResult.Success(text)
            }
            is SecurityResult.Error -> result
        }
    }

    suspend fun findById(id: Int): PasswordEntity? {
        return dao.findById(id)
    }

    suspend fun delete(id: Int): SecurityResult<Unit> = try {
        dao.deleteById(id)
        SecurityResult.Success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        SecurityResult.Error(SecurityError.STORAGE_FAILED)
    }
}
