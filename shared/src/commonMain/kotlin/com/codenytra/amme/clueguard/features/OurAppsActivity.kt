/*
package com.codenytra.amme.el_8animah.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme

class OurAppsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.settings_our_apps),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = (-0.3).sp
                                    )
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = stringResource(R.string.back))
                                }
                            },
                            colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    OurAppsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class App(
    val nameRes: Int,
    val descriptionRes: Int,
    val icon: Int,
    val id: String
)

@Composable
fun OurAppsScreen(modifier: Modifier = Modifier) {

    val apps: List<App> = listOf(
        App(
            nameRes = R.string.app_motmaan_name,
            descriptionRes = R.string.app_motmaan_desc,
            icon = R.drawable.motmaan,
            id = "com.codenytra.amme.motmaan"
        ),
        App(
            nameRes = R.string.app_cinemeteor_name,
            descriptionRes = R.string.app_cinemeteor_desc,
            icon = R.drawable.cinemeteor,
            id = "com.acms.cinemeteor"
        ),
        App(
            nameRes = R.string.app_detauro_name,
            descriptionRes = R.string.app_detauro_desc,
            icon = R.drawable.detauro,
            id = "com.codenytra.amme.detauro"
        ),
    )

    LazyColumn(
        modifier
            .padding(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                apps.forEachIndexed { index, app ->
                    AppRow(
                        nameRes = app.nameRes,
                        descriptionRes = app.descriptionRes,
                        icon = app.icon,
                        id = app.id,
                        isTop = index == 0,
                        isBottom = index == apps.lastIndex
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OurAppsScreenPreview() {
    El8animahTheme {
        OurAppsScreen()
    }
}

@Composable
fun AppRow(
    nameRes: Int,
    descriptionRes: Int,
    icon: Int,
    id: String,
    isTop: Boolean = false,
    isBottom: Boolean = false,
    context: Context = LocalContext.current
) {
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
            .clickable(onClick = {
                // Opens the app page on Google Play Store via standard package URL
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=$id".toUri()
                    )
                )
            })
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
                    painter = painterResource(icon),
                    contentDescription = stringResource(nameRes),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(color = MaterialTheme.colorScheme.onSurface, width = 1.dp, shape = RoundedCornerShape(16.dp))
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(nameRes),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            }

            Spacer(modifier = Modifier.padding(start = 8.dp))


            Icon(
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }*/
