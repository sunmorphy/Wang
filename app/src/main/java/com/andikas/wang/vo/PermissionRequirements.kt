package com.andikas.wang.vo

object PermissionRequirements {

    // Call this to know exactly what each feature needs
    fun     getRequirementsFor(feature: AppFeature): List<Permission> = when (feature) {

        AppFeature.MANUAL_ENTRY -> listOf(
            // Nothing — fully offline, no permissions needed
        )

        AppFeature.LOCAL_ALERTS -> listOf(
            Permission.POST_NOTIFICATIONS,          // Android 13+ runtime
            Permission.BATTERY_OPTIMIZATION,        // standard + MIUI battery
        )

        AppFeature.AUTO_TRANSACTION_CAPTURE -> listOf(
            Permission.NOTIFICATION_LISTENER,       // special: Settings redirect
            Permission.BATTERY_OPTIMIZATION,        // standard
            Permission.MIUI_AUTOSTART,              // Xiaomi only
            Permission.MIUI_BATTERY_NO_RESTRICT,    // Xiaomi only
        )

        AppFeature.FLOATING_BUBBLE -> listOf(
            Permission.SYSTEM_ALERT_WINDOW,         // special: Settings redirect
            Permission.MIUI_BACKGROUND_POPUP,       // Xiaomi only
        )

        AppFeature.AI_ADVISOR -> listOf(
            Permission.INTERNET,                    // normal, no dialog
            Permission.USER_API_KEY,                // not a system permission
        )

        AppFeature.BIOMETRIC_LOCK -> listOf(
            Permission.USE_BIOMETRIC,               // declared in manifest only
        )

        AppFeature.BACKUP_EXPORT -> listOf(
            // Android 10+: no storage permission needed (scoped storage)
            // Android 9 and below: WRITE_EXTERNAL_STORAGE
        )
    }
}

enum class AppFeature {
    MANUAL_ENTRY, LOCAL_ALERTS, AUTO_TRANSACTION_CAPTURE,
    FLOATING_BUBBLE, AI_ADVISOR, BIOMETRIC_LOCK, BACKUP_EXPORT
}

enum class Permission(
    val title: String,
    val description: String,
    val isMandatory: Boolean = false
) {
    POST_NOTIFICATIONS(
        "Notifications",
        "Required to show alerts and transaction summaries.",
        true
    ),
    BATTERY_OPTIMIZATION(
        "Battery Optimization",
        "Allow the app to run in the background for stable transaction tracking.",
        false
    ),
    NOTIFICATION_LISTENER(
        "Notification Access",
        "Required to automatically capture transactions from your finance apps.",
        true
    ),
    SYSTEM_ALERT_WINDOW(
        "Display Over Apps",
        "Required to show the floating bubble for quick transaction entry.",
        false
    ),
    USE_BIOMETRIC(
        "Biometric",
        "Used to secure your data with fingerprint or face unlock.",
        false
    ),
    INTERNET(
        "Internet",
        "Required for AI advisor features.",
        false
    ),
    USER_API_KEY(
        "API Key",
        "Used for AI advisor authentication.",
        false
    ),
    MIUI_AUTOSTART(
        "Autostart",
        "Xiaomi requirement to allow the app to start after device reboot.",
        false
    ),
    MIUI_BATTERY_NO_RESTRICT(
        "No Battery Restrictions",
        "Xiaomi requirement to prevent the system from killing the background tracker.",
        false
    ),
    MIUI_BACKGROUND_POPUP(
        "Background Pop-up",
        "Xiaomi requirement to show the floating bubble from background.",
        false
    )
}
