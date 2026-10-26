package com.codenytra.amme.clueguard.security

actual fun createEncryptionManager(): EncryptionManager = AndroidEncryptionManager()
