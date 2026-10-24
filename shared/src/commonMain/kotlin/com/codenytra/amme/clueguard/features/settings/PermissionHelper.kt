/*
package com.codenytra.amme.el_8animah.features.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

object PermissionHelper {
    fun hasNotificationPermission(context: Context): Boolean {
        // NotificationManagerCompat = compatible with all android versions
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun openNotificationSettings(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8+ = opens the app's notification settings directly.
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        } else {
            // Older = Opens the general app settings page
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
        }
        context.startActivity(intent)
    }
}*/
