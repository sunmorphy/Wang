package com.andikas.wang.service

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class FinancialNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        isConnected = true
        Log.d(TAG, "FinancialNotificationListener connected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        isConnected = false
        Log.w(TAG, "FinancialNotificationListener disconnected. Requesting rebind...")
        requestRebind(ComponentName(this, FinancialNotificationListener::class.java))
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // TODO: Implement transaction capture logic
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    companion object {
        private const val TAG = "FinancialNotifListener"

        @Volatile
        var isConnected: Boolean = false
            private set

        /**
         * Forces the system to rebind the NotificationListenerService if it got into
         * an unbind/desynced zombie state after an app update, hot swap, or process restart.
         */
        fun requestRebindService(context: Context) {
            val component = ComponentName(context, FinancialNotificationListener::class.java)
            try {
                requestRebind(component)
            } catch (e: Exception) {
                Log.w(TAG, "requestRebind failed: ${e.message}")
            }

            // Cycle component state to force Android's ManagedServices / NotificationManagerService
            // to clear stale/unregistered bindings and establish a fresh connection.
            try {
                val pm = context.packageManager
                pm.setComponentEnabledSetting(
                    component,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    component,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to toggle component state: ${e.message}")
            }
        }
    }
}