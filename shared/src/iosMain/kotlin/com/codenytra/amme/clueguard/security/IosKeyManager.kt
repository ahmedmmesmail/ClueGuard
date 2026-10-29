@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, UnsafeNumber::class)

package com.codenytra.amme.clueguard.security

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.numberWithInt
import platform.Security.SecAccessControlCreateWithFlags
import platform.Security.SecAccessControlRef
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecKeyCreateRandomKey
import platform.Security.SecKeyRef
import platform.Security.errSecSuccess
import platform.Security.kSecAccessControlPrivateKeyUsage
import platform.Security.kSecAttrAccessControl
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleWhenUnlockedThisDeviceOnly
import platform.Security.kSecAttrApplicationTag
import platform.Security.kSecAttrIsPermanent
import platform.Security.kSecAttrKeySizeInBits
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeECSECPrimeRandom
import platform.Security.kSecAttrTokenID
import platform.Security.kSecAttrTokenIDSecureEnclave
import platform.Security.kSecClass
import platform.Security.kSecClassKey
import platform.Security.kSecPrivateKeyAttrs
import platform.Security.kSecReturnRef

/**
 * Owns the single EC (P-256) key pair used by the app, stored in the Keychain.
 *
 * - On a real device the private key is generated INSIDE the Secure Enclave:
 *   it can never be exported, exactly like the Android Keystore key.
 * - The Simulator has no Secure Enclave, so we fall back to a normal Keychain
 *   key there (it is still stored in the Keychain, just not hardware-bound).
 * - Accessible only while the device is unlocked, and never migrated to
 *   another device or included in backups (ThisDeviceOnly).
 *
 * IMPORTANT: every SecKeyRef returned from here is owned by the caller and
 * must be released with CFRelease.
 */
internal class IosKeyManager {

    private companion object {
        const val KEY_TAG = "com.codenytra.amme.clueguard.encryption-key"
        const val KEY_SIZE_BITS = 256
    }

    private val keyTag: NSData = KEY_TAG.encodeToByteArray().toNSData()

    /** Returns the private key, or null if it doesn't exist. */
    fun getKey(): SecKeyRef? = memScoped {
        withCfRetained(keyTag) { cfTag ->
            withCfDictionary(
                kSecClass to kSecClassKey,
                kSecAttrApplicationTag to cfTag,
                kSecAttrKeyType to kSecAttrKeyTypeECSECPrimeRandom,
                kSecReturnRef to kCFBooleanTrue,
            ) { query ->
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)
                val key: SecKeyRef? =
                    if (status == errSecSuccess) result.value?.reinterpret() else null
                key
            }
        }
    }

    /** Existing key, or a new one: Secure Enclave first, software key as fallback. */
    fun getOrCreateKey(): SecKeyRef? =
        getKey() ?: createKey(inSecureEnclave = true) ?: createKey(inSecureEnclave = false)

    private fun createKey(inSecureEnclave: Boolean): SecKeyRef? = memScoped {
        val error = alloc<CFErrorRefVar>()
        var accessControl: SecAccessControlRef? = null

        try {
            val privateAttributes = mutableListOf<Pair<CFStringRef?, CFTypeRef?>>(
                kSecAttrIsPermanent to kCFBooleanTrue
            )

            if (inSecureEnclave) {
                accessControl = SecAccessControlCreateWithFlags(
                    kCFAllocatorDefault,
                    kSecAttrAccessibleWhenUnlockedThisDeviceOnly,
                    kSecAccessControlPrivateKeyUsage,
                    error.ptr
                ) ?: return@memScoped null
                privateAttributes += kSecAttrAccessControl to accessControl
            } else {
                privateAttributes += kSecAttrAccessible to kSecAttrAccessibleWhenUnlockedThisDeviceOnly
            }

            withCfRetained(keyTag) { cfTag ->
                privateAttributes += kSecAttrApplicationTag to cfTag

                withCfRetained(NSNumber.numberWithInt(KEY_SIZE_BITS)) { cfSize ->
                    withCfDictionary(*privateAttributes.toTypedArray()) { cfPrivateAttributes ->
                        val parameters = mutableListOf<Pair<CFStringRef?, CFTypeRef?>>(
                            kSecAttrKeyType to kSecAttrKeyTypeECSECPrimeRandom,
                            kSecAttrKeySizeInBits to cfSize,
                            kSecPrivateKeyAttrs to cfPrivateAttributes,
                        )
                        if (inSecureEnclave) {
                            parameters += kSecAttrTokenID to kSecAttrTokenIDSecureEnclave
                        }

                        withCfDictionary(*parameters.toTypedArray()) { cfParameters ->
                            SecKeyCreateRandomKey(cfParameters, error.ptr)
                        }
                    }
                }
            }
        } finally {
            error.value?.let { CFRelease(it) }
            accessControl?.let { CFRelease(it) }
        }
    }

    /** Deleting the key makes ALL data encrypted with it unrecoverable. */
    fun deleteKey() {
        memScoped {
            withCfRetained(keyTag) { cfTag ->
                withCfDictionary(
                    kSecClass to kSecClassKey,
                    kSecAttrApplicationTag to cfTag,
                    kSecAttrKeyType to kSecAttrKeyTypeECSECPrimeRandom,
                ) { query ->
                    SecItemDelete(query)
                }
            }
        }
    }
}
