package com.codenytra.amme.clueguard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import com.codenytra.amme.clueguard.data.database.ClueGuardDatabase
import com.codenytra.amme.clueguard.data.database.LocalClueGuardDatabase
import com.codenytra.amme.clueguard.data.repository.LocalPasswordRepository
import com.codenytra.amme.clueguard.data.repository.PasswordRepository
import com.codenytra.amme.clueguard.navigation.AppNavigation
import com.codenytra.amme.clueguard.security.createEncryptionManager
import com.codenytra.amme.clueguard.ui.theme.ClueGuardAppTheme
import com.codenytra.amme.clueguard.ui.theme.ClueGuardDarkRed
import com.codenytra.amme.clueguard.ui.theme.ClueGuardRed

val BrandGradient = Brush.linearGradient(
    colors = listOf(ClueGuardRed, ClueGuardDarkRed)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(database: ClueGuardDatabase) {

    val repository = remember(database) {
        PasswordRepository(
            dao = database.passwordDao,
            encryption = createEncryptionManager()
        )
    }

    CompositionLocalProvider(
        LocalClueGuardDatabase provides database,
        LocalPasswordRepository provides repository
    ) {

        ClueGuardAppTheme {
            AppNavigation()
        }
    }
}