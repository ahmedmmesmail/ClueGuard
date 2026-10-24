package com.codenytra.amme.clueguard

import androidx.compose.runtime.staticCompositionLocalOf

val LocalClueGuardDatabase = staticCompositionLocalOf<ClueGuardDatabase> {
    error("ClueGuardDatabase was not provided")
}