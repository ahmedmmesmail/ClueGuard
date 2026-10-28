package com.codenytra.amme.clueguard.features

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clueguard.shared.generated.resources.Res
import clueguard.shared.generated.resources.Res.string
import clueguard.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class App(
    val name: StringResource,
    val description: StringResource,
    val icon: DrawableResource,
    val id: String,
)

private val apps = listOf(
    App(
        name = string.app_motmaan_name,
        description = string.app_motmaan_desc,
        icon = Res.drawable.motmaan,
        id = "com.codenytra.amme.motmaan"
    ),
    App(
        name = string.app_cinemeteor_name,
        description = string.app_cinemeteor_desc,
        icon = Res.drawable.cinemeteor,
        id = "com.acms.cinemeteor"
    ),
    App(
        name = string.app_detauro_name,
        description = string.app_detauro_desc,
        icon = Res.drawable.detauro,
        id = "com.codenytra.amme.detauro"
    ),
)

/** الشاشة كاملة (Scaffold + TopAppBar) — بدل OurAppsActivity. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OurAppsRoute(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(string.settings_our_apps),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.3).sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = stringResource(string.back)
                        )
                    }
                },
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        OurAppsScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun OurAppsScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.padding(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                apps.forEachIndexed { index, app ->
                    AppRow(
                        app = app,
                        isTop = index == 0,
                        isBottom = index == apps.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
fun AppRow(
    app: App,
    isTop: Boolean = false,
    isBottom: Boolean = false,
) {
    val uriHandler = LocalUriHandler.current

    // Dynamically shapes corners to visually group list items into cohesive cards
    val shape = RoundedCornerShape(
        topStart = if (isTop) 18.dp else 4.dp,
        topEnd = if (isTop) 18.dp else 4.dp,
        bottomStart = if (isBottom) 18.dp else 4.dp,
        bottomEnd = if (isBottom) 18.dp else 4.dp
    )

    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable {
                // يفتح صفحة التطبيق على Google Play (أو المتصفح على iOS)
                runCatching {
                    uriHandler.openUri("https://play.google.com/store/apps/details?id=${app.id}")
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            ) {
                Image(
                    painter = painterResource(app.icon),
                    contentDescription = stringResource(app.name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(app.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(app.description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // كانت في الأصل برّه الـ Row بسبب قوس زيادة — اتصلحت
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}