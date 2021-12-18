package com.ahmed.azan_local_notifications.models;

import androidx.annotation.Keep;

@Keep
public enum IconSource {
  DrawableResource,
  BitmapFilePath,
  ContentUri,
  FlutterBitmapAsset,
  ByteArray
}
