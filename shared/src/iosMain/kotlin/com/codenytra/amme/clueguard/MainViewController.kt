package com.codenytra.amme.clueguard

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val database = remember {
        getDatabase()
    }

    App(database = database)
}