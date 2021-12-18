package com.ahmed.azanlocalnotifications.models.styles

import androidx.annotation.Keep

@Keep
class BigTextStyleInformation(
    htmlFormatTitle: Boolean?,
    htmlFormatBody: Boolean?,
    var bigText: String,
    var htmlFormatBigText: Boolean,
    var contentTitle: String,
    var htmlFormatContentTitle: Boolean,
    var summaryText: String,
    var htmlFormatSummaryText: Boolean
) : DefaultStyleInformation(htmlFormatTitle, htmlFormatBody)