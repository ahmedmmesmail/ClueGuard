package com.codenytra.amme.clueguard.features.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Groups a category header and a list of SettingItem rows into a visual section
@Composable
fun SettingsSection(
    header: String,
    content: List<SettingItem>
) {
    Column {
        Text(
            text = header.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            content.forEachIndexed { index, item ->
                SettingsRow(
                    item = item,
                    isTop = index == 0,
                    isBottom = index == content.lastIndex
                )
            }
        }
    }
}