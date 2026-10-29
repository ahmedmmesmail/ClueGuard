package com.codenytra.amme.clueguard.features

import androidx.compose.runtime.Composable
import com.codenytra.amme.clueguard.data.repository.LocalPasswordRepository

@Composable
fun PasswordScreen(id: Int) {

    val repository = LocalPasswordRepository.current

}