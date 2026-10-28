package com.codenytra.amme.clueguard.features

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clueguard.shared.generated.resources.Res
import clueguard.shared.generated.resources.*
import com.codenytra.amme.clueguard.ui.components.AnimatedAppIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private data class ContactInfo(
    val label: StringResource,
    val value: String,
    val icon: ImageVector,
    val uri: String,
)

private val contacts = listOf(
    ContactInfo(
        Res.string.about_contact_email,
        "ahmedmme.26@gmail.com",
        Icons.Rounded.Email,
        "mailto:ahmedmme.26@gmail.com"
    ),
    ContactInfo(
        Res.string.about_contact_portfolio,
        "ahmedmmesmail.me",
        Icons.Rounded.Language,
        "https://ahmedmmesmail.me"
    ),
    ContactInfo(
        Res.string.about_contact_github,
        "github.com/ahmedmmesmail",
        Icons.Rounded.Code,
        "https://github.com/ahmedmmesmail"
    ),
    ContactInfo(
        Res.string.about_contact_linkedin,
        "linkedin.com/in/ahmedmmesmail",
        Icons.Rounded.Person,
        "https://linkedin.com/in/ahmedmmesmail"
    ),
)

/* ----------------------------------------------------------------------
 * Main Screen
 * -------------------------------------------------------------------- */

/**
 * @param versionName بدل BuildConfig.VERSION_NAME (مش موجود في commonMain) —
 *                    مرّره من كل منصة (Android: BuildConfig.VERSION_NAME، iOS: من Info.plist).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    versionName: String,
    onBack: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val noAppMessage = stringResource(Res.string.no_app_to_open_link)

    // بديل Toast + Context.openUri
    val openUri: (String) -> Unit = { uri ->
        try {
            uriHandler.openUri(uri)
        } catch (_: Exception) {
            scope.launch { snackbarHostState.showSnackbar(noAppMessage) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedAppIcon()
            Spacer(Modifier.height(20.dp))

            FadeSlideIn(delayMs = 300) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(Res.string.app_name),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        stringResource(Res.string.settings_version) + " : " + versionName,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        stringResource(Res.string.app_desc),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(28.dp))
            FadeSlideIn(delayMs = 500) { DeveloperCard() }

            Spacer(Modifier.height(20.dp))
            FadeSlideIn(delayMs = 700) { ContactSection(onOpenUri = openUri) }

            Spacer(Modifier.height(24.dp))
            FadeSlideIn(delayMs = 900) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(Res.string.about_made_with),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                    Icon(
                        Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        stringResource(Res.string.about_in_egypt),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/* ----------------------------------------------------------------------
 * Developer Card
 * -------------------------------------------------------------------- */

@Composable
private fun DeveloperCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp),
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(Res.string.about_dev_header),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        stringResource(Res.string.about_dev_name),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        stringResource(Res.string.about_dev_title),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(Res.string.about_dev_bio),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/* ----------------------------------------------------------------------
 * Contact Section
 * -------------------------------------------------------------------- */

@Composable
private fun ContactSection(onOpenUri: (String) -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            stringResource(Res.string.about_contact_me),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        contacts.forEachIndexed { index, item ->
            FadeSlideIn(delayMs = 800 + index * 120) {
                ContactRow(
                    item,
                    isTop = index == 0,
                    isBottom = index == contacts.lastIndex,
                    onClick = { onOpenUri(item.uri) },
                )
            }
        }
    }
}

@Composable
private fun ContactRow(
    item: ContactInfo,
    onClick: () -> Unit,
    isTop: Boolean = false,
    isBottom: Boolean = false,
) {
    val shape = RoundedCornerShape(
        topStart = if (isTop) 16.dp else 4.dp,
        topEnd = if (isTop) 16.dp else 4.dp,
        bottomStart = if (isBottom) 16.dp else 4.dp,
        bottomEnd = if (isBottom) 16.dp else 4.dp
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    item.icon,
                    contentDescription = stringResource(item.label),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(Modifier.size(14.dp))
            Column {
                Text(stringResource(item.label), fontWeight = FontWeight.Medium)
                Text(
                    item.value,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Staggered entrance animation: delays appearance and slides content in vertically
@Composable
private fun FadeSlideIn(delayMs: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMs.toLong().milliseconds)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 4 },
    ) {
        content()
    }
}