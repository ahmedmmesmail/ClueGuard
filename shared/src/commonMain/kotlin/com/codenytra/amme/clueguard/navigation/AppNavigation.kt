package com.codenytra.amme.clueguard.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.codenytra.amme.clueguard.features.AboutScreen
import com.codenytra.amme.clueguard.features.HomeScreen
import com.codenytra.amme.clueguard.features.OurAppsRoute
import com.codenytra.amme.clueguard.features.settings.SettingsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data object Home : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object OurApps : NavKey

@Serializable
data object About : NavKey

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Home::class, Home.serializer())
            subclass(Settings::class, Settings.serializer())
            subclass(OurApps::class, OurApps.serializer())
            subclass(About::class, About.serializer())
        }
    }
}

private fun scaleForward(): ContentTransform =
    (fadeIn(tween(400, delayMillis = 100)) +
            scaleIn(tween(400, easing = FastOutSlowInEasing), initialScale = 0.75f)) togetherWith
            (fadeOut(tween(200)) +
                    scaleOut(tween(400, easing = FastOutSlowInEasing), targetScale = 0.99f))

private fun scaleBackward(): ContentTransform =
    (fadeIn(tween(350, delayMillis = 100)) +
            scaleIn(tween(350, easing = FastOutSlowInEasing), initialScale = 1.1f)) togetherWith
            (fadeOut(tween(200)) +
                    scaleOut(tween(350, easing = FastOutSlowInEasing), targetScale = 0.85f))

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(navConfig, Home)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },

            transitionSpec = { scaleForward() },
            popTransitionSpec = { scaleBackward() },
            predictivePopTransitionSpec = { scaleBackward() },

            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen(
                        onSettingsClick = { backStack.add(Settings) },
                        onIconClick = { backStack.add(About) }
                    )
                }
                entry<Settings> {
                    SettingsScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onAboutClick = { backStack.add(About) },
                        onOurAppsClick = { backStack.add(OurApps) }
                    )
                }
                entry<OurApps> {
                    OurAppsRoute(onBack = { backStack.removeLastOrNull() })
                }
                entry<About> {
                    AboutScreen(versionName = "1.0.0", onBack = { backStack.removeLastOrNull() })
                }
            }
        )
    }
}