package com.codenytra.amme.clueguard

import androidx.compose.runtime.Composable

/** مشاركة نص عن طريق Share Sheet الخاصة بكل منصة. */
interface ShareHandler {
    fun shareText(text: String, title: String? = null)
}

@Composable
expect fun rememberShareHandler(): ShareHandler