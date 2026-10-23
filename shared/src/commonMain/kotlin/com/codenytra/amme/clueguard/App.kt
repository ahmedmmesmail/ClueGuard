package com.codenytra.amme.clueguard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.codenytra.amme.clueguard.ui.theme.ClueGuardAppTheme
import com.codenytra.amme.clueguard.ui.theme.ClueGuardDarkRed
import com.codenytra.amme.clueguard.ui.theme.ClueGuardRed

val BrandGradient = Brush.linearGradient(
    colors = listOf(ClueGuardDarkRed, ClueGuardRed)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    ClueGuardAppTheme {
        AppNavigation()
    }
}