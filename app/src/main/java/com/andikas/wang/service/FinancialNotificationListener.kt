package com.andikas.wang.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class FinancialNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // TODO: Implement transaction capture logic
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}