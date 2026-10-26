package com.codenytra.amme.clueguard.data.database

import androidx.compose.runtime.staticCompositionLocalOf

val LocalClueGuardDatabase = staticCompositionLocalOf<ClueGuardDatabase> {
    error("ClueGuardDatabase was not provided")
}