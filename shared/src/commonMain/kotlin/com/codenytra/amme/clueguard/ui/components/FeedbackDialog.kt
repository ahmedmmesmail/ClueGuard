package com.codenytra.amme.clueguard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clueguard.shared.generated.resources.Res
import clueguard.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

private const val FEEDBACK_EMAIL = "ahmedmme.26@gmail.com"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackSheet(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var selectedType by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }

    val types = listOf(
        Res.string.feedback_type_bug,
        Res.string.feedback_type_feature,
        Res.string.feedback_type_other
    )
    val typeIcons =
        listOf(Icons.Rounded.BugReport, Icons.Rounded.Lightbulb, Icons.Rounded.ChatBubble)

    // في KMP مفيش context.getString — بنجيب النصوص جوّه الـ Composable
    val typeLabels = types.map { stringResource(it) }
    val appName = stringResource(Res.string.app_name)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(Res.string.feedback_title),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.feedback_type_label).uppercase(),
                    style = TextStyle(
                        fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    types.forEachIndexed { i, _ ->
                        item {
                            FilterChip(
                                selected = selectedType == i,
                                onClick = { selectedType = i },
                                label = { Text(typeLabels[i], fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(typeIcons[i], null, modifier = Modifier.size(14.dp))
                                },
                                shape = MaterialTheme.shapes.small
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text(stringResource(Res.string.feedback_message_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 5
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large
                    ) { Text(stringResource(Res.string.feedback_cancel)) }

                    Button(
                        onClick = {
                            val subject = "[${typeLabels[selectedType]}] $appName Feedback"
                            // mailto مع subject/body مترمّزين — بديل Intent.ACTION_SENDTO
                            val uri = "mailto:$FEEDBACK_EMAIL" +
                                    "?subject=${subject.urlEncode()}&body=${message.urlEncode()}"
                            // لو مفيش تطبيق إيميل متسطّب مايحصلش crash
                            runCatching { uriHandler.openUri(uri) }
                            onDismiss()
                        },
                        enabled = message.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large
                    ) { Text(stringResource(Res.string.feedback_send)) }
                }
            }
        }
    }
}

// Percent-encoding (UTF-8) في commonMain، لأن java.net.URLEncoder مش متاح
private const val HEX = "0123456789ABCDEF"

private fun String.urlEncode(): String = buildString {
    for (byte in this@urlEncode.encodeToByteArray()) {
        val c = byte.toInt() and 0xFF
        val safe = (c in 'a'.code..'z'.code) || (c in 'A'.code..'Z'.code) ||
                (c in '0'.code..'9'.code) ||
                c == '-'.code || c == '_'.code || c == '.'.code || c == '~'.code
        if (safe) append(c.toChar()) else {
            append('%')
            append(HEX[c shr 4])
            append(HEX[c and 0x0F])
        }
    }
}