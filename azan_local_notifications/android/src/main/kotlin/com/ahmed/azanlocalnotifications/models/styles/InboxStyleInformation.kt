package com.ahmed.azanlocalnotifications.models.styles

import androidx.annotation.Keep
import java.util.ArrayList

@Keep
class InboxStyleInformation(
    htmlFormatTitle: Boolean?,
    htmlFormatBody: Boolean?,
    var contentTitle: String,
    var htmlFormatContentTitle: Boolean,
    var summaryText: String,
    var htmlFormatSummaryText: Boolean,
    lines: ArrayList<String?>,
    htmlFormatLines: Boolean
) : DefaultStyleInformation(htmlFormatTitle, htmlFormatBody) {
    var htmlFormatLines: Boolean
    var lines: ArrayList<String>

    init {
        this.lines = lines
        this.htmlFormatLines = htmlFormatLines
    }
}