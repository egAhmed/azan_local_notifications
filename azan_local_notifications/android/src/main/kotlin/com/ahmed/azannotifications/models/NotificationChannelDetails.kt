package com.ahmed.azanlocalnotifications.models

import android.graphics.Color
import androidx.annotation.Keep
import java.io.Serializable
import java.util.Map

@Keep
class NotificationChannelDetails : Serializable {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var groupId: String? = null
    var showBadge: Boolean? = null
    var importance: Integer? = null
    var playSound: Boolean? = null
    var sound: String? = null
    var soundSource: SoundSource? = null
    var enableVibration: Boolean? = null
    var vibrationPattern: LongArray?
    var channelAction: NotificationChannelAction? = null
    var enableLights: Boolean? = null
    var ledColor: Integer? = null

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val GROUP_ID = "groupId"
        private const val SHOW_BADGE = "showBadge"
        private const val IMPORTANCE = "importance"
        private const val PLAY_SOUND = "playSound"
        private const val SOUND = "sound"
        private const val SOUND_SOURCE = "soundSource"
        private const val ENABLE_VIBRATION = "enableVibration"
        private const val VIBRATION_PATTERN = "vibrationPattern"
        private const val CHANNEL_ACTION = "channelAction"
        private const val ENABLE_LIGHTS = "enableLights"
        private const val LED_COLOR_ALPHA = "ledColorAlpha"
        private const val LED_COLOR_RED = "ledColorRed"
        private const val LED_COLOR_GREEN = "ledColorGreen"
        private const val LED_COLOR_BLUE = "ledColorBlue"
        fun from(arguments: Map<String?, Object?>): NotificationChannelDetails {
            val notificationChannel = NotificationChannelDetails()
            notificationChannel.id = arguments[ID]
            notificationChannel.name = arguments[NAME]
            notificationChannel.description = arguments[DESCRIPTION]
            notificationChannel.groupId = arguments[GROUP_ID]
            notificationChannel.importance = arguments[IMPORTANCE] as Integer?
            notificationChannel.showBadge = arguments[SHOW_BADGE]
            notificationChannel.channelAction =
                NotificationChannelAction.values().get(arguments[CHANNEL_ACTION] as Integer?)
            notificationChannel.enableVibration = arguments[ENABLE_VIBRATION]
            notificationChannel.vibrationPattern = arguments[VIBRATION_PATTERN]
            notificationChannel.playSound = arguments[PLAY_SOUND]
            notificationChannel.sound = arguments[SOUND]
            val soundSourceIndex: Integer? = arguments[SOUND_SOURCE] as Integer?
            if (soundSourceIndex != null) {
                notificationChannel.soundSource = SoundSource.values().get(soundSourceIndex)
            }
            val a: Integer? = arguments[LED_COLOR_ALPHA] as Integer?
            val r: Integer? = arguments[LED_COLOR_RED] as Integer?
            val g: Integer? = arguments[LED_COLOR_GREEN] as Integer?
            val b: Integer? = arguments[LED_COLOR_BLUE] as Integer?
            if (a != null && r != null && g != null && b != null) {
                notificationChannel.ledColor = Color.argb(a, r, g, b)
            }
            notificationChannel.enableLights = arguments[ENABLE_LIGHTS]
            return notificationChannel
        }

        fun fromNotificationDetails(
            notificationDetails: NotificationDetails
        ): NotificationChannelDetails {
            val notificationChannel = NotificationChannelDetails()
            notificationChannel.id = notificationDetails.channelId
            notificationChannel.name = notificationDetails.channelName
            notificationChannel.description = notificationDetails.channelDescription
            notificationChannel.importance = notificationDetails.importance
            notificationChannel.showBadge = notificationDetails.channelShowBadge
            if (notificationDetails.channelAction == null) {
                notificationChannel.channelAction = NotificationChannelAction.CreateIfNotExists
            } else {
                notificationChannel.channelAction = notificationDetails.channelAction
            }
            notificationChannel.enableVibration = notificationDetails.enableVibration
            notificationChannel.vibrationPattern = notificationDetails.vibrationPattern
            notificationChannel.playSound = notificationDetails.playSound
            notificationChannel.sound = notificationDetails.sound
            notificationChannel.soundSource = notificationDetails.soundSource
            notificationChannel.ledColor = notificationDetails.ledColor
            notificationChannel.enableLights = notificationDetails.enableLights
            return notificationChannel
        }
    }
}