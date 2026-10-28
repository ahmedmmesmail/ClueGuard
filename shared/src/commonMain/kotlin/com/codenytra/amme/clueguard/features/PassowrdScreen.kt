package com.codenytra.amme.clueguard.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.codenytra.amme.clueguard.data.database.LocalClueGuardDatabase
import com.codenytra.amme.clueguard.data.repository.PasswordRepository
import com.codenytra.amme.clueguard.security.createEncryptionManager

@Composable
fun PasswordScreen(id: Int) {

    val database = LocalClueGuardDatabase.current
    val dao = database.passwordDao
    val repository = remember(database) {
        PasswordRepository(
            dao = dao,
            encryption = createEncryptionManager()
        )
    }
//    val password = repository.findById(id)
}