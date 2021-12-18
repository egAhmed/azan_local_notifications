package com.ahmed.azan_local_notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Keep;
import androidx.core.app.NotificationManagerCompat;

import com.ahmed.azan_local_notifications.models.NotificationDetails;
import com.ahmed.azan_local_notifications.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/** Created by michaelbui on 24/3/18. */
@Keep
public class ScheduledNotificationReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, Intent intent) {
    String notificationDetailsJson =
        intent.getStringExtra(AzanLocalNotificationsPlugin.NOTIFICATION_DETAILS);
    if (StringUtils.isNullOrEmpty(notificationDetailsJson)) {
      // This logic is needed for apps that used the plugin prior to 0.3.4
      Notification notification = intent.getParcelableExtra("notification");
      notification.when = System.currentTimeMillis();
      int notificationId = intent.getIntExtra("notification_id", 0);
      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
      notificationManager.notify(notificationId, notification);
      boolean repeat = intent.getBooleanExtra("repeat", false);
      if (!repeat) {
        AzanLocalNotificationsPlugin.removeNotificationFromCache(context, notificationId);
      }
    } else {
      Gson gson = AzanLocalNotificationsPlugin.buildGson();
      Type type = new TypeToken<NotificationDetails>() {}.getType();
      NotificationDetails notificationDetails = gson.fromJson(notificationDetailsJson, type);
      AzanLocalNotificationsPlugin.showNotification(context, notificationDetails);
      if (notificationDetails.scheduledNotificationRepeatFrequency != null) {
        AzanLocalNotificationsPlugin.zonedScheduleNextNotification(context, notificationDetails);
      } else if (notificationDetails.matchDateTimeComponents != null) {
        AzanLocalNotificationsPlugin.zonedScheduleNextNotificationMatchingDateComponents(
            context, notificationDetails);
      } else if (notificationDetails.repeatInterval != null) {
        AzanLocalNotificationsPlugin.scheduleNextRepeatingNotification(
            context, notificationDetails);
      } else {
        AzanLocalNotificationsPlugin.removeNotificationFromCache(
            context, notificationDetails.id);
      }
    }
  }
}
