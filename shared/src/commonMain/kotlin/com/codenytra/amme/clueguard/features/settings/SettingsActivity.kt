package com.codenytra.amme.clueguard.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.codenytra.amme.clueguard.BrandGradient

private data class SettingsOption(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
    var biometricLockEnabled by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // ---------- General ----------
            item { SettingsSectionTitle("General") }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Appearance",
                        description = "Customize the app theme",
                        icon = Icons.Rounded.Palette
                    ),
                    onClick = { /* TODO: Open appearance settings. */ }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Language",
                        description = "Choose your preferred language",
                        icon = Icons.Rounded.Language
                    ),
                    onClick = { /* TODO: Open language selection. */ }
                )
            }

            item {
                SettingsSwitchRow(
                    option = SettingsOption(
                        title = "Notifications",
                        description = "Manage security notifications",
                        icon = Icons.Rounded.Notifications
                    ),
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }

            // ---------- Security ----------
            item { SettingsSectionTitle("Security") }

            item {
                SettingsSwitchRow(
                    option = SettingsOption(
                        title = "Biometric lock",
                        description = "Require biometric authentication",
                        icon = Icons.Rounded.Lock
                    ),
                    checked = biometricLockEnabled,
                    onCheckedChange = { biometricLockEnabled = it }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Security",
                        description = "Review your vault security options",
                        icon = Icons.Rounded.Security
                    ),
                    onClick = { /* TODO: Open security settings. */ }
                )
            }

            // ---------- Data ----------
            item { SettingsSectionTitle("Data") }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Backup",
                        description = "Create a backup of your vault",
                        icon = Icons.Rounded.Backup
                    ),
                    onClick = { /* TODO: Implement secure vault backup. */ }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Restore",
                        description = "Restore a vault backup",
                        icon = Icons.Rounded.Restore
                    ),
                    onClick = { /* TODO: Implement backup restoration. */ }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Reset",
                        description = "Reset your application settings",
                        icon = Icons.Rounded.DeleteForever
                    ),
                    onClick = { /* TODO: Show a confirmation dialog. */ }
                )
            }

            // ---------- About ----------
            item { SettingsSectionTitle("About") }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Share ClueGuard",
                        description = "Tell others about ClueGuard",
                        icon = Icons.Rounded.Share
                    ),
                    onClick = { /* TODO: Implement platform-specific sharing. */ }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Privacy policy",
                        description = "Read the privacy policy",
                        icon = Icons.Rounded.Policy
                    ),
                    onClick = { /* TODO: Open the privacy policy. */ }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "About ClueGuard",
                        description = "App information and version",
                        icon = Icons.Rounded.Info
                    ),
                    onClick = { /* TODO: Open the about screen. */ }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
    )
}
@Composable
private fun SettingsIcon(icon: ImageVector) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(
                brush = BrandGradient,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@Composable
private fun SettingsRow(
    option: SettingsOption,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
    ListItem(
        headlineContent = {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                text = option.description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingContent = { SettingsIcon(option.icon) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    )
}
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SettingsSwitchRow(
    option: SettingsOption,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { Text(option.title, style = MaterialTheme.typography.bodyLarge) },
            supportingContent = { Text(option.description, style = MaterialTheme.typography.bodyMedium) },
            leadingContent = { SettingsIcon(option.icon) },
            trailingContent = { Switch(checked = checked, onCheckedChange = null) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}