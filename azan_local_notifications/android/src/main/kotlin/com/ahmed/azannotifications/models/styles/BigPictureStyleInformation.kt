package com.ahmed.azanlocalnotifications.models.styles

import androidx.annotation.Keep
import com.dexterous.flutterlocalnotifications.models.BitmapSource

@Keep
class BigPictureStyleInformation(
    htmlFormatTitle: Boolean?,
    htmlFormatBody: Boolean?,
    var contentTitle: String,
    var htmlFormatContentTitle: Boolean,
    var summaryText: String,
    var htmlFormatSummaryText: Boolean,
    largeIcon: Object,
    largeIconBitmapSource: BitmapSource,
    bigPicture: Object,
    bigPictureBitmapSource: BitmapSource,
    hideExpandedLargeIcon: Boolean
) : DefaultStyleInformation(htmlFormatTitle, htmlFormatBody) {
    var largeIcon: Object
    var largeIconBitmapSource: BitmapSource
    var bigPicture: Object
    var bigPictureBitmapSource: BitmapSource
    var hideExpandedLargeIcon: Boolean

    init {
        this.largeIcon = largeIcon
        this.largeIconBitmapSource = largeIconBitmapSource
        this.bigPicture = bigPicture
        this.bigPictureBitmapSource = bigPictureBitmapSource
        this.hideExpandedLargeIcon = hideExpandedLargeIcon
    }
}