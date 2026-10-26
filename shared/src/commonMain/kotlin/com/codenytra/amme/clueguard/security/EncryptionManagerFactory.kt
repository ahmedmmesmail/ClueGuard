package com.codenytra.amme.clueguard.security

/** Each platform decides which implementation to use. */
expect fun createEncryptionManager(): EncryptionManager
