package com.ahmed.azanlocalnotifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.Keep

@Keep
class ScheduledNotificationBootReceiver : BroadcastReceiver() {
    @Override
    fun onReceive(context: Context?, intent: Intent) {
        val action: String = intent.getAction()
        if (action != null) {
            if (action.equals(android.content.Intent.ACTION_BOOT_COMPLETED)
                || action.equals(Intent.ACTION_MY_PACKAGE_REPLACED)
                || action.equals("android.intent.action.QUICKBOOT_POWERON")
                || action.equals("com.htc.intent.action.QUICKBOOT_POWERON")
            ) {
                FlutterLocalNotificationsPlugin.rescheduleNotifications(context)
            }
        }
    }
}