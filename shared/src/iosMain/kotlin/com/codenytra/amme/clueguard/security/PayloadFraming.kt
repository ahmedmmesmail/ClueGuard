package com.codenytra.amme.clueguard.security

/**
 * Apple's ECIES (SecKey) API has no "associated data" parameter, unlike AES-GCM
 * on Android. We get the same protection by putting the context INSIDE the
 * encrypted payload and checking it after decryption:
 *
// *   payload = [context length: 2 bytes][context][plaintext]
 *
 * Because the whole payload is authenticated by the cipher, an attacker cannot
 * change the context (or move the blob to another field) without decryption
 * failing, and a wrong context given to decrypt() is rejected by unwrap().
 */
internal object PayloadFraming {

    const val MAX_CONTEXT_SIZE = 0xFFFF

    fun wrap(context: ByteArray, plaintext: ByteArray): ByteArray {
        require(context.size <= MAX_CONTEXT_SIZE) { "Context is too large" }

        val out = ByteArray(2 + context.size + plaintext.size)
        out[0] = (context.size ushr 8).toByte()
        out[1] = context.size.toByte()
        context.copyInto(out, destinationOffset = 2)
        plaintext.copyInto(out, destinationOffset = 2 + context.size)
        return out
    }

    /** Returns the plaintext, or null if the payload/context do not match. */
    fun unwrap(context: ByteArray, payload: ByteArray): ByteArray? {
        if (payload.size < 2) return null

        val length = ((payload[0].toInt() and 0xFF) shl 8) or (payload[1].toInt() and 0xFF)
        if (length != context.size || payload.size < 2 + length) return null

        // Compare without stopping at the first difference.
        var diff = 0
        for (i in 0 until length) {
            diff = diff or (payload[2 + i].toInt() xor context[i].toInt())
        }
        if (diff != 0) return null

        return payload.copyOfRange(2 + length, payload.size)
    }
}
