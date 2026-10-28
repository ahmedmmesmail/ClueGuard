package com.codenytra.amme.clueguard.features.locale

import platform.Foundation.NSUserDefaults

actual fun applyPlatformLocale(code: String) {
    val defaults = NSUserDefaults.standardUserDefaults
    if (code == LocaleHelper.DEFAULT_LOCALE) {
        defaults.removeObjectForKey("AppleLanguages")
    } else {
        defaults.setObject(listOf(code), forKey = "AppleLanguages")
    }
    defaults.synchronize()
}