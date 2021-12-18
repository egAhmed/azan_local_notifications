package com.ahmed.azan_local_notifications;

import com.ahmed.azan_local_notifications.models.NotificationDetails;

import java.io.Serializable;
import java.util.ArrayList;

public class ForegroundServiceStartParameter implements Serializable {
  public static final String EXTRA =
      "com.ahmed.azan_local_notifications.ForegroundServiceStartParameter";

  public final NotificationDetails notificationData;
  public final int startMode;
  public final ArrayList<Integer> foregroundServiceTypes;

  public ForegroundServiceStartParameter(
      NotificationDetails notificationData,
      int startMode,
      ArrayList<Integer> foregroundServiceTypes) {
    this.notificationData = notificationData;
    this.startMode = startMode;
    this.foregroundServiceTypes = foregroundServiceTypes;
  }

  @Override
  public String toString() {
    return "ForegroundServiceStartParameter{"
        + "notificationData="
        + notificationData
        + ", startMode="
        + startMode
        + ", foregroundServiceTypes="
        + foregroundServiceTypes
        + '}';
  }
}
