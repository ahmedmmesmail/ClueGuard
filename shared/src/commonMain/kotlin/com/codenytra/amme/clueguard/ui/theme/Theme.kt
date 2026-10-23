package com.codenytra.amme.clueguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// --------------------------------------------------
// Light color scheme
// --------------------------------------------------

private val LightColors = lightColorScheme(
    primary = ClueGuardDarkRed,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE1E4),
    onPrimaryContainer = Color(0xFF740817),

    secondary = ClueGuardRed,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9DE),
    onSecondaryContainer = Color(0xFF680013),

    tertiary = Color(0xFF5E5E5E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE4E4E4),
    onTertiaryContainer = Color(0xFF1B1B1B),

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFE9E9E9),
    onSurfaceVariant = Color(0xFF5F5F5F),

    surfaceTint = ClueGuardDarkRed,
    inverseSurface = Color(0xFF2F2F2F),
    inverseOnSurface = Color(0xFFF2F2F2),
    inversePrimary = ClueGuardRed,

    // Neutral surfaces
    surfaceDim = Color(0xFFE2E2E2),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F5F5),
    surfaceContainer = Color(0xFFEFEFEF),
    surfaceContainerHigh = Color(0xFFE9E9E9),
    surfaceContainerHighest = Color(0xFFE3E3E3),

    outline = Color(0xFF7A7A7A),
    outlineVariant = Color(0xFFDDDDDD),
    scrim = Color(0x99000000),

    // Different from primary so errors don't look like normal actions
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

// --------------------------------------------------
// Dark color scheme
// --------------------------------------------------

private val DarkColors = darkColorScheme(
    primary = ClueGuardRed,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = ClueGuardDarkRed,
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFFFF9AA5),
    onSecondary = Color(0xFF56000D),
    secondaryContainer = Color(0xFF7C1927),
    onSecondaryContainer = Color(0xFFFFD9DE),

    tertiary = Color(0xFFC8C8C8),
    onTertiary = Color(0xFF303030),
    tertiaryContainer = Color(0xFF474747),
    onTertiaryContainer = Color(0xFFE4E4E4),

    background = Color(0xFF121212),
    onBackground = Color(0xFFEDEDED),

    surface = Color(0xFF181818),
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF2B2B2B),
    onSurfaceVariant = Color(0xFFB5B5B5),

    surfaceTint = ClueGuardRed,
    inverseSurface = Color(0xFFEDEDED),
    inverseOnSurface = Color(0xFF2F2F2F),
    inversePrimary = ClueGuardDarkRed,

    // Neutral surfaces
    surfaceDim = Color(0xFF121212),
    surfaceBright = Color(0xFF383838),
    surfaceContainerLowest = Color(0xFF0D0D0D),
    surfaceContainerLow = Color(0xFF1C1C1C),
    surfaceContainer = Color(0xFF212121),
    surfaceContainerHigh = Color(0xFF2B2B2B),
    surfaceContainerHighest = Color(0xFF363636),

    outline = Color(0xFF949494),
    outlineVariant = Color(0xFF484848),
    scrim = Color(0x99000000),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

// --------------------------------------------------
// Additional application-specific colors
// --------------------------------------------------

@Immutable
data class ClueGuardExtraColors(
    val brand: Color,
    val brandDark: Color,

    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    val textDisabled: Color,
    val divider: Color
)

private val LightExtraColors = ClueGuardExtraColors(
    brand = ClueGuardRed,
    brandDark = ClueGuardDarkRed,

    success = Color(0xFF247A48),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFD7F5DF),
    onSuccessContainer = Color(0xFF124B29),

    warning = Color(0xFF8A5700),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFE7B5),
    onWarningContainer = Color(0xFF4D2E00),

    info = Color(0xFF245FA6),
    onInfo = Color(0xFFFFFFFF),
    infoContainer = Color(0xFFD9E8FF),
    onInfoContainer = Color(0xFF153B6B),

    textDisabled = Color(0xFFA0A0A0),
    divider = Color(0xFFDDDDDD)
)

private val DarkExtraColors = ClueGuardExtraColors(
    brand = ClueGuardRed,
    brandDark = ClueGuardDarkRed,

    success = Color(0xFF86D9A0),
    onSuccess = Color(0xFF00391A),
    successContainer = Color(0xFF14532D),
    onSuccessContainer = Color(0xFFB8F3C7),

    warning = Color(0xFFFFC66D),
    onWarning = Color(0xFF472900),
    warningContainer = Color(0xFF674000),
    onWarningContainer = Color(0xFFFFE7B5),

    info = Color(0xFFADC9FF),
    onInfo = Color(0xFF002F60),
    infoContainer = Color(0xFF164783),
    onInfoContainer = Color(0xFFD9E8FF),

    textDisabled = Color(0xFF777777),
    divider = Color(0xFF484848)
)

private val LocalClueGuardColors =
    staticCompositionLocalOf { LightExtraColors }

object ClueGuardTheme {
    val extraColors: ClueGuardExtraColors
        @Composable
        @ReadOnlyComposable
        get() = LocalClueGuardColors.current
}

// --------------------------------------------------
// Main theme
// --------------------------------------------------

@Composable
fun ClueGuardAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme =
        if (darkTheme) DarkColors else LightColors

    val extraColors =
        if (darkTheme) DarkExtraColors else LightExtraColors

    CompositionLocalProvider(
        LocalClueGuardColors provides extraColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ClueGuardTypography,
            shapes = ClueGuardShapes,
            content = content
        )
    }
}