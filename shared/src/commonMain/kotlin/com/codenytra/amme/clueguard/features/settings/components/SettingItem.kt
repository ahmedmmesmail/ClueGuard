package com.codenytra.amme.el_8animah.features.settings.components

import androidx.compose.ui.graphics.vector.ImageVector

// Supported trailing indicator types for a settings item row
enum class SettingsTrailing {
    CHEVRON,
    SWITCH,
    VALUE
}

// Data model representing a single configurable setting item in SettingsScreen
data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val trailing: SettingsTrailing = SettingsTrailing.CHEVRON,
    val value: String = "",
    val checked: Boolean = false,
    val onToggle: (Boolean) -> Unit = {},
    val onClick: () -> Unit = {}
)