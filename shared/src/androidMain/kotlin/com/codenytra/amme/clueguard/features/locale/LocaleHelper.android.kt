package com.codenytra.amme.clueguard.features.locale


import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

actual fun applyPlatformLocale(code: String) {
    val locale = if (code == LocaleHelper.DEFAULT_LOCALE) {
        Resources.getSystem().configuration.locales[0]   // لغة الجهاز
    } else {
        Locale.forLanguageTag(code.replace('_', '-'))
    }
    Locale.setDefault(locale) // Compose Resources بتقرا Locale.getDefault()
}

/**
 * للـ Android بس: استخدمها في MainActivity.attachBaseContext
 * عشان الـ Views/النظام يتبعوا نفس اللغة (زي الـ LocaleHelper.wrap القديم).
 *
 *   override fun attachBaseContext(newBase: Context) =
 *       super.attachBaseContext(newBase.withSavedLocale())
 */
fun Context.withSavedLocale(): Context {
    val code = LocaleHelper.getSavedLocale()
    if (code == LocaleHelper.DEFAULT_LOCALE) return this
    val locale = Locale.forLanguageTag(code.replace('_', '-'))
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration).apply {
        setLocale(locale)
        setLayoutDirection(locale)
    }
    return createConfigurationContext(config)
}