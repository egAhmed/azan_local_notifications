package com.ahmed.azanlocalnotifications.models

import androidx.annotation.Keep

@Keep
enum class IconSource {
    DrawableResource, BitmapFilePath, ContentUri, FlutterBitmapAsset, ByteArray
}