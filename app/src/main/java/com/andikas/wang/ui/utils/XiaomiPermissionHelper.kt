package com.andikas.wang.ui.utils

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

class XiaomiPermissionHelper(private val context: Context) {

    // ── Autostart permission ───────────────────────────────────
    // Without this, ALL background services are killed when app is closed
    // This kills NotificationListenerService, WorkManager, BubbleService
    // There is NO programmatic way to grant this. We have to guide the user.

    fun openAutostartSettings() {
        val intents = listOf(
            // MIUI 12+ (most common)
            Intent().apply {
                component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            },
            // MIUI 10–11 fallback
            Intent().apply {
                component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.securitycenter.MainActivity"
                )
            },
            // HyperOS (MIUI 14+) — different package
            Intent().apply {
                component = ComponentName(
                    "com.xiaomi.misettings",
                    "com.xiaomi.misettings.Main"
                )
            }
        )

        for (intent in intents) {
            try {
                context.startActivity(intent)
                return  // first one that works, stop
            } catch (e: ActivityNotFoundException) {
                continue
            }
        }

        // Last resort - open general app settings
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
        )
    }

    // ── Background pop-up windows ───────────────────────────────────
    // Required for the floating bubble to appear over other apps
    // Even if SYSTEM_ALERT_WINDOW is granted, MIUI has a second gate
    // Path: Settings > Apps > Manage apps > Wang > Other permissions
    //       > Display pop-up windows while running in the background

    fun openMiuiBackgroundPopupSettings() {
        val intents = listOf(
            Intent("miui.intent.action.APP_PERM_EDITOR").apply {
                setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity"
                )
                putExtra("extra_pkgname", context.packageName)
            },
            Intent("miui.intent.action.APP_PERM_EDITOR").apply {
                setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                putExtra("extra_pkgname", context.packageName)
            }
        )

        for (intent in intents) {
            try {
                context.startActivity(intent)
                return
            } catch (e: ActivityNotFoundException) {
                continue
            }
        }
    }

    // ── Battery saver - MIUI aggressive kill ───────────────────────────────────
    // MIUI ignores standard REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    // It has its own battery saver that kills background apps regardless
    // Path: Settings > Battery & performance > Choose apps > Wang > No restrictions

    fun openMiuiBatterySettings() {
        val intents = listOf(
            // MIUI 12+
            Intent().apply {
                component = ComponentName(
                    "com.miui.powerkeeper",
                    "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"
                )
                putExtra("package_name", context.packageName)
                putExtra("package_label", "Wang")
            },
            // HyperOS fallback
            Intent().apply {
                action = "miui.intent.action.POWER_HIDE_MODE_APP_LIST"
                putExtra("package_name", context.packageName)
            }
        )

        for (intent in intents) {
            try {
                context.startActivity(intent)
                return
            } catch (e: ActivityNotFoundException) {
                continue
            }
        }

        // Fallback - standard battery settings
        context.startActivity(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS))
    }

    // ── Check if MIUI notification listener actually works ───────────────────────────────────
    // MIUI sometimes grants the permission visually but kills the
    // service anyway. The only reliable check is whether the service
    // is currently running and receiving events.

    fun isNotificationListenerActuallyRunning(): Boolean {
        val am = context.getSystemService(ActivityManager::class.java)
        return am.getRunningServices(Int.MAX_VALUE)?.any {
            it.service.className.contains("FinancialNotificationListener")
        } ?: false
    }
}