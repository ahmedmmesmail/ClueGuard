package com.codenytra.amme.clueguard.data.repository

import androidx.compose.runtime.staticCompositionLocalOf

val LocalPasswordRepository = staticCompositionLocalOf<PasswordRepository> {
    error("PasswordRepository was not provided")
}