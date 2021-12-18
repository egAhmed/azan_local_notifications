package com.ahmed.azanlocalnotifications.models

import androidx.annotation.Keep
import java.io.Serializable
import java.util.Map

@Keep
class NotificationChannelGroupDetails : Serializable {
    var id: String? = null
    var name: String? = null
    var description: String? = null

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        fun from(arguments: Map<String?, Object?>): NotificationChannelGroupDetails {
            val notificationChannelGroupDetails = NotificationChannelGroupDetails()
            notificationChannelGroupDetails.id = arguments[ID]
            notificationChannelGroupDetails.name = arguments[NAME]
            notificationChannelGroupDetails.description = arguments[DESCRIPTION]
            return notificationChannelGroupDetails
        }
    }
}