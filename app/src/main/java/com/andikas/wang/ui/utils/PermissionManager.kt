package com.andikas.wang.ui.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.biometric.BiometricManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.andikas.wang.service.FinancialNotificationListener

class PermissionManager(private val context: Context) {

    // ── Runtime permissions ────────────────

    // Android 13+ requires explicit grant to post notifications
    fun hasPostNotificationsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // auto-granted below Android 13
        }
    }

    // Biometric availability
    fun hasBiometricCapability(): BiometricStatus {
        val manager = BiometricManager.from(context)
        val authenticators = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        } else {
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        }

        return when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NOT_ENROLLED
            else -> BiometricStatus.UNAVAILABLE
        }
    }

    // Battery optimization
    fun isIgnoringBatteryOptimizations(): Boolean {
        val pm = context.getSystemService(PowerManager::class.java)
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    // ── Special permissions ────────────────

    // NotificationListenerService
    fun hasNotificationListenerPermission(): Boolean {
        val flat = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false
        return flat.contains(context.packageName)
    }

    // Floating bubble (Display over other apps)
    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    // ── Xiaomi-specific permissions ────────────────

    fun isXiaomiDevice(): Boolean {
        return Build.MANUFACTURER.lowercase() == "xiaomi" ||
                Build.MANUFACTURER.lowercase() == "redmi" ||
                Build.BRAND.lowercase() == "xiaomi" ||
                Build.BRAND.lowercase() == "redmi" ||
                Build.BRAND.lowercase() == "poco"
    }

    fun isMIUI(): Boolean {
        return getSystemProperty("ro.miui.ui.version.name").isNotEmpty()
    }

    fun getMIUIVersion(): Int {
        val version = getSystemProperty("ro.miui.ui.version.code")
        return version.toIntOrNull() ?: 0
    }

    @SuppressLint("PrivateApi")
    private fun getSystemProperty(key: String): String {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getMethod("get", String::class.java)
            method.invoke(null, key) as? String ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    // Check if autostart is enabled via ActivityManager since there's no API for this
    fun checkAutostartIndirectly(): AutostartStatus {
        if (!isXiaomiDevice()) return AutostartStatus.NOT_APPLICABLE
        // We can't read autostart state directly — best we can do is
        // check if our service was killed unexpectedly after some time
        val am = context.getSystemService(ActivityManager::class.java)
        val isServiceRunning = am.getRunningServices(100)?.any {
            it.service.className == FinancialNotificationListener::class.java.name
        } ?: false
        return if (isServiceRunning) AutostartStatus.LIKELY_ENABLED
        else AutostartStatus.LIKELY_DISABLED
    }

    enum class BiometricStatus { AVAILABLE, NO_HARDWARE, NOT_ENROLLED, UNAVAILABLE }
    enum class AutostartStatus { LIKELY_ENABLED, LIKELY_DISABLED, NOT_APPLICABLE }
}