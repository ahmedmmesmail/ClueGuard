package com.codenytra.amme.clueguard.features.locale


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.russhwolf.settings.Settings


/*
 * LocaleHelper (KMP):
 *  - بيحفظ اللغة المختارة عن طريق multiplatform-settings (بدل SharedPreferences)
 *  - بيطبّقها عن طريق applyPlatformLocale (expect/actual لكل منصة)
 */

/** كل منصة بتطبّق اللغة بطريقتها (Android: Locale.setDefault، iOS: AppleLanguages). */
expect fun applyPlatformLocale(code: String)

object LocaleHelper {

    private const val KEY_LOCALE = "selected_locale"
    const val DEFAULT_LOCALE = "default"

    data class AppLanguage(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val flag: String,
        val isRTL: Boolean = false
    )

    val supportedLanguages = listOf(
        AppLanguage("default", "System Default", "حسب لغة الجهاز", "⚙️"),
        AppLanguage("ar", "Arabic", "العربية", "🇸🇦", isRTL = true),
        AppLanguage("en", "English", "English", "🇬🇧"),
        AppLanguage("es", "Spanish", "Español", "🇪🇸"),
        AppLanguage("fr", "French", "Français", "🇫🇷"),
        AppLanguage("de", "German", "Deutsch", "🇩🇪"),
        AppLanguage("it", "Italian", "Italiano", "🇮🇹"),
        AppLanguage("ru", "Russian", "Русский", "🇷🇺"),
        AppLanguage("pt", "Portuguese", "Português", "🇵🇹"),
        AppLanguage("tr", "Turkish", "Türkçe", "🇹🇷"),
        AppLanguage("zh", "Chinese", "中文 (简体)", "🇨🇳"),
        AppLanguage("ja", "Japanese", "日本語", "🇯🇵"),
        AppLanguage("hi", "Hindi", "हिन्दी", "🇮🇳"),
    )

    private val settings: Settings by lazy { Settings() }

    fun saveLocale(localeCode: String) {
        settings.putString(KEY_LOCALE, localeCode)
    }

    fun getSavedLocale(): String = settings.getString(KEY_LOCALE, DEFAULT_LOCALE)

    fun applyLocale(localeCode: String) = applyPlatformLocale(localeCode)

    fun applySavedLocale() = applyLocale(getSavedLocale())
}

/**
 * لُفّ بيها الـ App كله. لما اللغة تتغيّر بيعيد بناء الشجرة (key) عشان الـ resources تتحمّل
 * باللغة الجديدة، وبيظبط اتجاه الـ Layout (RTL/LTR).
 * ملحوظة: إعادة البناء بتصفّر أي state محلي تحت الـ key (زي الـ navigation) — احتفظ بيه في ViewModel.
 */
@Composable
fun LocalizedContent(localeCode: String, content: @Composable () -> Unit) {
    val lang = LocaleHelper.supportedLanguages.firstOrNull { it.code == localeCode }
    val direction = when {
        lang == null || localeCode == LocaleHelper.DEFAULT_LOCALE -> LocalLayoutDirection.current
        lang.isRTL -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }
    key(localeCode) {
        CompositionLocalProvider(LocalLayoutDirection provides direction) { content() }
    }
}