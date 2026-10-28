package com.codenytra.amme.clueguard.features.locale


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel من androidx.lifecycle (بيدعم KMP) — مفيش Application هنا
class LocaleViewModel : ViewModel() {

    // Compose state — القراءة في Composable بتعمل recomposition
    var currentLocale by mutableStateOf(LocaleHelper.getSavedLocale())
        private set

    // نسخة StateFlow لغير الـ Compose
    // (في الأصل كانت بتتقرأ مرة واحدة ومبتتحدّثش أبدًا — دلوقتي بتتحدّث مع updateLocale)
    private val _language = MutableStateFlow(currentLocale)
    val language: StateFlow<String> = _language.asStateFlow()

    init {
        LocaleHelper.applyLocale(currentLocale)
    }

    fun updateLocale(locale: String) {
        LocaleHelper.saveLocale(locale)
        LocaleHelper.applyLocale(locale)
        currentLocale = locale
        _language.value = locale
    }
}