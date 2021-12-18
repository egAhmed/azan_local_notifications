package com.ahmed.azan_local_notifications.utils;

import androidx.annotation.Keep;

@Keep
public class BooleanUtils {
  public static boolean getValue(Boolean booleanObject) {
    return booleanObject != null && booleanObject.booleanValue();
  }
}
