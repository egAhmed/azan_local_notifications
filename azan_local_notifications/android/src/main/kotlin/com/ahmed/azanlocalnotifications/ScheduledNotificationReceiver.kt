package com.ahmed.azanlocalnotifications

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import androidx.core.app.NotificationManagerCompat
import com.dexterous.flutterlocalnotifications.models.NotificationDetails
import com.dexterous.flutterlocalnotifications.utils.StringUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/** Created by michaelbui on 24/3/18.  */
@Keep
class ScheduledNotificationReceiver : BroadcastReceiver() {
    @Override
    fun onReceive(context: Context?, intent: Intent) {
        val notificationDetailsJson: String =
            intent.getStringExtra(FlutterLocalNotificationsPlugin.NOTIFICATION_DETAILS)
        if (StringUtils.isNullOrEmpty(notificationDetailsJson)) {
            // This logic is needed for apps that used the plugin prior to 0.3.4
            val notification: Notification = intent.getParcelableExtra("notification")
            notification.`when` = System.currentTimeMillis()
            val notificationId: Int = intent.getIntExtra("notification_id", 0)
            val notificationManager: NotificationManagerCompat =
                NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, notification)
            val repeat: Boolean = intent.getBooleanExtra("repeat", false)
            if (!repeat) {
                FlutterLocalNotificationsPlugin.removeNotificationFromCache(context, notificationId)
            }
        } else {
            val gson: Gson = FlutterLocalNotificationsPlugin.buildGson()
            val type: Type = object : TypeToken<NotificationDetails?>() {}.getType()
            val notificationDetails: NotificationDetails =
                gson.fromJson(notificationDetailsJson, type)
            FlutterLocalNotificationsPlugin.showNotification(context, notificationDetails)
            if (notificationDetails.scheduledNotificationRepeatFrequency != null) {
                FlutterLocalNotificationsPlugin.zonedScheduleNextNotification(
                    context,
                    notificationDetails
                )
            } else if (notificationDetails.matchDateTimeComponents != null) {
                FlutterLocalNotificationsPlugin.zonedScheduleNextNotificationMatchingDateComponents(
                    context, notificationDetails
                )
            } else if (notificationDetails.repeatInterval != null) {
                FlutterLocalNotificationsPlugin.scheduleNextRepeatingNotification(
                    context, notificationDetails
                )
            } else {
                FlutterLocalNotificationsPlugin.removeNotificationFromCache(
                    context, notificationDetails.id
                )
            }
        }
    }
}