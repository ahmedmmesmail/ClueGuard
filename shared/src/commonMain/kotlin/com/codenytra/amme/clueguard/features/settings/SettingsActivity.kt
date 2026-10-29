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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Restore
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import clueguard.shared.generated.resources.*
import com.codenytra.amme.clueguard.features.locale.LocaleViewModel
import com.codenytra.amme.clueguard.getPlatform
import com.codenytra.amme.clueguard.rememberShareHandler
import com.codenytra.amme.clueguard.ui.components.FeedbackSheet
import com.codenytra.amme.clueguard.ui.components.LanguagePickerSheet
import org.jetbrains.compose.resources.stringResource

private data class SettingsOption(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onAboutClick: () -> Unit,
    onOurAppsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var biometricLockEnabled by rememberSaveable { mutableStateOf(false) }
    var showFeedbackSheet by rememberSaveable { mutableStateOf(false) }
    var showLanguageSheet by rememberSaveable { mutableStateOf(false) }
    val localeVm: LocaleViewModel = viewModel()

    val share = rememberShareHandler()
    val shareMessage = stringResource(Res.string.share_app_message)
    val shareTitle = stringResource(Res.string.share_app_title)

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

        if (showLanguageSheet) {
            LanguagePickerSheet(
                currentCode = localeVm.currentLocale,
                onSelect = { lang ->
                    localeVm.updateLocale(lang.code)
                    showLanguageSheet = false
                },
                onDismiss = { showLanguageSheet = false }
            )
        }
        if (showFeedbackSheet) {
            FeedbackSheet(onDismiss = { showFeedbackSheet = false })
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // ---------- General ----------
            item { SettingsSectionTitle("General") }

            if (getPlatform().name.lowercase().contains("android")) {

            }
            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Appearance",
                        description = "Customize the app theme",
                        icon = Icons.Rounded.Palette
                    ),
                    isFirst = true,
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
                    onClick = { showLanguageSheet = true }
                )
            }

            item {
                SettingsSwitchRow(
                    option = SettingsOption(
                        title = "Biometric lock",
                        description = "Require biometric authentication",
                        icon = Icons.Rounded.Lock
                    ),
                    checked = biometricLockEnabled,
                    onCheckedChange = { biometricLockEnabled = it },
                    isLast = true
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
                    isFirst = true,
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
                    isLast = true,
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
                    isFirst = true,
                    onClick = {
                        share.shareText(
                            text = "$shareMessage https://play.google.com/store/apps/details?id=com.codenytra.amme.clueguard",
                            title = shareTitle
                        )
                    }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "Feedback",
                        description = "help us improve ClueGuard",
                        icon = Icons.Rounded.Feedback
                    ),
                    onClick = { showFeedbackSheet = true }
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
                        title = "Our Apps",
                        description = "Discover other apps from CodeNytra",
                        icon = Icons.Rounded.Apps
                    ),
                    onClick = { onOurAppsClick() }
                )
            }

            item {
                SettingsRow(
                    option = SettingsOption(
                        title = "About ClueGuard",
                        description = "App information and version",
                        icon = Icons.Rounded.Info
                    ),
                    isLast = true,
                    onClick = { onAboutClick() }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        style = TextStyle(
            fontSize = 11.sp, fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp, color = MaterialTheme.colorScheme.primary
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SettingsRow(
    option: SettingsOption,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onClick: () -> Unit
) {

    val shape: Shape = when {
        isFirst && isLast -> MaterialTheme.shapes.extraLarge
        isFirst -> MaterialTheme.shapes.extraLarge.copy(
            bottomStart = CornerSize(
                4.dp
            ), bottomEnd = CornerSize(4.dp)
        )

        isLast -> MaterialTheme.shapes.extraLarge.copy(
            topStart = CornerSize(4.dp),
            topEnd = CornerSize(4.dp)
        )

        else -> MaterialTheme.shapes.small
    }

    Surface(
        onClick = onClick,
        shape = shape,
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
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {

    val shape: Shape = when {
        isFirst && isLast -> MaterialTheme.shapes.extraLarge
        isFirst -> MaterialTheme.shapes.extraLarge.copy(
            bottomStart = CornerSize(
                4.dp
            ), bottomEnd = CornerSize(4.dp)
        )

        isLast -> MaterialTheme.shapes.extraLarge.copy(
            topStart = CornerSize(4.dp),
            topEnd = CornerSize(4.dp)
        )

        else -> MaterialTheme.shapes.small
    }

    Surface(
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { Text(option.title, style = MaterialTheme.typography.bodyLarge) },
            supportingContent = {
                Text(
                    option.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingContent = { SettingsIcon(option.icon) },
            trailingContent = { Switch(checked = checked, onCheckedChange = null) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}