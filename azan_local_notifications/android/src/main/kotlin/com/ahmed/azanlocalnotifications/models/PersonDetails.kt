package com.ahmed.azanlocalnotifications.models

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class PersonDetails(
    var bot: Boolean,
    icon: Object,
    iconSource: IconSource,
    important: Boolean,
    key: String,
    name: String,
    uri: String
) : Serializable {
    var icon: Object
    var iconBitmapSource: IconSource
    var important: Boolean
    var key: String
    var name: String
    var uri: String

    init {
        this.icon = icon
        iconBitmapSource = iconSource
        this.important = important
        this.key = key
        this.name = name
        this.uri = uri
    }
}