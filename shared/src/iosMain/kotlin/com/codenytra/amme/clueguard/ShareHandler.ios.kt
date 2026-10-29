package com.codenytra.amme.clueguard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController

@Composable
actual fun rememberShareHandler(): ShareHandler = remember {
    object : ShareHandler {
        override fun shareText(text: String, title: String?) {
            val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return

            // أعلى شاشة معروضة (مهم لو بتشارك من جوه Bottom Sheet)
            var top: UIViewController = root
            while (top.presentedViewController != null) {
                top = top.presentedViewController!!
            }

            val controller = UIActivityViewController(
                activityItems = listOf(text),
                applicationActivities = null
            )
            // لازم على iPad وإلا بيعمل crash
            controller.popoverPresentationController?.sourceView = top.view

            top.presentViewController(controller, animated = true, completion = null)
        }
    }
}