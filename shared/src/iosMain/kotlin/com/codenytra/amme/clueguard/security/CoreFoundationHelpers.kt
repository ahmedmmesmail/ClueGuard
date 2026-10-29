@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, UnsafeNumber::class)

package com.codenytra.amme.clueguard.security

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.kCFAllocatorDefault
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

/*
 * Small helpers around Core Foundation, which is a C API: objects we create
 * ("Create"/"Copy" functions) must be released by us with CFRelease.
 * The dictionary-building pattern follows the one used by the open-source
 * multiplatform-settings library (Apache-2.0).
 */

/** Builds a CFDictionary. The caller owns it and must CFRelease it. */
internal fun MemScope.cfDictionaryOf(
    vararg items: Pair<CFStringRef?, CFTypeRef?>
): CFDictionaryRef? {
    val keys = allocArrayOf(*items.map { it.first }.toTypedArray())
    val values = allocArrayOf(*items.map { it.second }.toTypedArray())
    return CFDictionaryCreate(
        kCFAllocatorDefault,
        keys.reinterpret(),
        values.reinterpret(),
        items.size.convert(),
        null, // no key callbacks: we keep the keys/values alive ourselves
        null
    )
}

/** Creates a dictionary, runs [block], then always releases the dictionary. */
internal inline fun <T> withCfDictionary(
    vararg items: Pair<CFStringRef?, CFTypeRef?>,
    block: (CFDictionaryRef?) -> T
): T = memScoped {
    val dictionary = cfDictionaryOf(*items)
    try {
        block(dictionary)
    } finally {
        CFRelease(dictionary)
    }
}

/** Gives [block] a retained CF version of a Kotlin/Foundation object. */
internal inline fun <T> withCfRetained(value: Any?, block: (CFTypeRef?) -> T): T {
    val cfValue = CFBridgingRetain(value)
    try {
        return block(cfValue)
    } finally {
        CFBridgingRelease(cfValue)
    }
}

internal fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = size.convert())
}

internal fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val result = ByteArray(size)
    if (size > 0) {
        result.usePinned { memcpy(it.addressOf(0), bytes, length) }
    }
    return result
}
