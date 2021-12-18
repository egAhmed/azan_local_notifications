package com.ahmed.azanlocalnotifications.models

import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.Keep
import com.dexterous.flutterlocalnotifications.NotificationStyle
import com.dexterous.flutterlocalnotifications.RepeatInterval
import com.dexterous.flutterlocalnotifications.models.styles.BigPictureStyleInformation
import com.dexterous.flutterlocalnotifications.models.styles.BigTextStyleInformation
import com.dexterous.flutterlocalnotifications.models.styles.DefaultStyleInformation
import com.dexterous.flutterlocalnotifications.models.styles.InboxStyleInformation
import com.dexterous.flutterlocalnotifications.models.styles.MessagingStyleInformation
import com.dexterous.flutterlocalnotifications.models.styles.StyleInformation
import java.io.Serializable
import java.util.ArrayList
import java.util.Map

@Keep
class NotificationDetails : Serializable {
    var id: Integer? = null
    var title: String? = null
    var body: String? = null
    var icon: String? = null
    var channelId: String? = "Default_Channel_Id"
    var channelName: String? = null
    var channelDescription: String? = null
    var channelShowBadge: Boolean? = null
    var importance: Integer? = null
    var priority: Integer? = null
    var playSound: Boolean? = null
    var sound: String? = null
    var soundSource: SoundSource? = null
    var enableVibration: Boolean? = null
    var vibrationPattern: LongArray?
    var style: NotificationStyle? = null
    var styleInformation: StyleInformation? = null
    var repeatInterval: RepeatInterval? = null
    var repeatTime: Time? = null
    var millisecondsSinceEpoch: Long? = null
    var calledAt: Long? = null
    var payload: String? = null
    var groupKey: String? = null
    var setAsGroupSummary: Boolean? = null
    var groupAlertBehavior: Integer? = null
    var autoCancel: Boolean? = null
    var ongoing: Boolean? = null
    var day: Integer? = null
    var color: Integer? = null
    var largeIcon: Object? = null
    var largeIconBitmapSource: BitmapSource? = null
    var onlyAlertOnce: Boolean? = null
    var showProgress: Boolean? = null
    var maxProgress: Integer? = null
    var progress: Integer? = null
    var indeterminate: Boolean? = null
    var channelAction: NotificationChannelAction? = null
    var enableLights: Boolean? = null
    var ledColor: Integer? = null
    var ledOnMs: Integer? = null
    var ledOffMs: Integer? = null
    var ticker: String? = null
    var visibility: Integer? = null
    var allowWhileIdle: Boolean? = null
    var timeoutAfter: Long? = null
    var category: String? = null
    var additionalFlags: IntArray?
    var showWhen: Boolean? = null
    var usesChronometer: Boolean? = null
    var scheduledDateTime: String? = null
    var timeZoneName: String? = null
    var scheduledNotificationRepeatFrequency: ScheduledNotificationRepeatFrequency? = null
    var matchDateTimeComponents: DateTimeComponents? = null
    var `when`: Long? = null
    var fullScreenIntent: Boolean? = null
    var shortcutId: String? = null
    var subText: String? = null
    var tag: String? = null

    // Note: this is set on the Android to save details about the icon that should be used when
    // re-hydrating scheduled notifications when a device has been restarted.
    var iconResourceId: Integer? = null

    companion object {
        private const val ID = "id"
        private const val TITLE = "title"
        private const val BODY = "body"
        private const val PAYLOAD = "payload"
        private const val MILLISECONDS_SINCE_EPOCH = "millisecondsSinceEpoch"
        private const val CALLED_AT = "calledAt"
        private const val REPEAT_INTERVAL = "repeatInterval"
        private const val REPEAT_TIME = "repeatTime"
        private const val PLATFORM_SPECIFICS = "platformSpecifics"
        private const val AUTO_CANCEL = "autoCancel"
        private const val ONGOING = "ongoing"
        private const val STYLE = "style"
        private const val ICON = "icon"
        private const val PRIORITY = "priority"
        private const val PLAY_SOUND = "playSound"
        private const val SOUND = "sound"
        private const val SOUND_SOURCE = "soundSource"
        private const val ENABLE_VIBRATION = "enableVibration"
        private const val VIBRATION_PATTERN = "vibrationPattern"
        private const val TAG = "tag"
        private const val GROUP_KEY = "groupKey"
        private const val SET_AS_GROUP_SUMMARY = "setAsGroupSummary"
        private const val GROUP_ALERT_BEHAVIOR = "groupAlertBehavior"
        private const val ONLY_ALERT_ONCE = "onlyAlertOnce"
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "channelName"
        private const val CHANNEL_DESCRIPTION = "channelDescription"
        private const val CHANNEL_SHOW_BADGE = "channelShowBadge"
        private const val IMPORTANCE = "importance"
        private const val STYLE_INFORMATION = "styleInformation"
        private const val BIG_TEXT = "bigText"
        private const val HTML_FORMAT_BIG_TEXT = "htmlFormatBigText"
        private const val CONTENT_TITLE = "contentTitle"
        private const val HTML_FORMAT_CONTENT_TITLE = "htmlFormatContentTitle"
        private const val SUMMARY_TEXT = "summaryText"
        private const val HTML_FORMAT_SUMMARY_TEXT = "htmlFormatSummaryText"
        private const val LINES = "lines"
        private const val HTML_FORMAT_LINES = "htmlFormatLines"
        private const val HTML_FORMAT_TITLE = "htmlFormatTitle"
        private const val HTML_FORMAT_CONTENT = "htmlFormatContent"
        private const val DAY = "day"
        private const val COLOR_ALPHA = "colorAlpha"
        private const val COLOR_RED = "colorRed"
        private const val COLOR_GREEN = "colorGreen"
        private const val COLOR_BLUE = "colorBlue"
        private const val LARGE_ICON = "largeIcon"
        private const val LARGE_ICON_BITMAP_SOURCE = "largeIconBitmapSource"
        private const val BIG_PICTURE = "bigPicture"
        private const val BIG_PICTURE_BITMAP_SOURCE = "bigPictureBitmapSource"
        private const val HIDE_EXPANDED_LARGE_ICON = "hideExpandedLargeIcon"
        private const val SHOW_PROGRESS = "showProgress"
        private const val MAX_PROGRESS = "maxProgress"
        private const val PROGRESS = "progress"
        private const val INDETERMINATE = "indeterminate"
        private const val PERSON = "person"
        private const val CONVERSATION_TITLE = "conversationTitle"
        private const val GROUP_CONVERSATION = "groupConversation"
        private const val MESSAGES = "messages"
        private const val TEXT = "text"
        private const val TIMESTAMP = "timestamp"
        private const val BOT = "bot"
        private const val ICON_SOURCE = "iconSource"
        private const val IMPORTANT = "important"
        private const val KEY = "key"
        private const val NAME = "name"
        private const val URI = "uri"
        private const val DATA_MIME_TYPE = "dataMimeType"
        private const val DATA_URI = "dataUri"
        private const val CHANNEL_ACTION = "channelAction"
        private const val ENABLE_LIGHTS = "enableLights"
        private const val LED_COLOR_ALPHA = "ledColorAlpha"
        private const val LED_COLOR_RED = "ledColorRed"
        private const val LED_COLOR_GREEN = "ledColorGreen"
        private const val LED_COLOR_BLUE = "ledColorBlue"
        private const val LED_ON_MS = "ledOnMs"
        private const val LED_OFF_MS = "ledOffMs"
        private const val VISIBILITY = "visibility"
        private const val TICKER = "ticker"
        private const val ALLOW_WHILE_IDLE = "allowWhileIdle"
        private const val CATEGORY = "category"
        private const val TIMEOUT_AFTER = "timeoutAfter"
        private const val SHOW_WHEN = "showWhen"
        private const val WHEN = "when"
        private const val USES_CHRONOMETER = "usesChronometer"
        private const val ADDITIONAL_FLAGS = "additionalFlags"
        private const val SCHEDULED_DATE_TIME = "scheduledDateTime"
        private const val TIME_ZONE_NAME = "timeZoneName"
        private const val SCHEDULED_NOTIFICATION_REPEAT_FREQUENCY =
            "scheduledNotificationRepeatFrequency"
        private const val MATCH_DATE_TIME_COMPONENTS = "matchDateTimeComponents"
        private const val FULL_SCREEN_INTENT = "fullScreenIntent"
        private const val SHORTCUT_ID = "shortcutId"
        private const val SUB_TEXT = "subText"
        fun from(arguments: Map<String?, Object?>): NotificationDetails {
            val notificationDetails = NotificationDetails()
            notificationDetails.payload = arguments[PAYLOAD]
            notificationDetails.id = arguments[ID] as Integer?
            notificationDetails.title = arguments[TITLE]
            notificationDetails.body = arguments[BODY]
            notificationDetails.scheduledDateTime = arguments[SCHEDULED_DATE_TIME]
            notificationDetails.timeZoneName = arguments[TIME_ZONE_NAME]
            if (arguments.containsKey(SCHEDULED_NOTIFICATION_REPEAT_FREQUENCY)) {
                notificationDetails.scheduledNotificationRepeatFrequency =
                    ScheduledNotificationRepeatFrequency.values().get(
                        arguments[SCHEDULED_NOTIFICATION_REPEAT_FREQUENCY] as Integer?
                    )
            }
            if (arguments.containsKey(MATCH_DATE_TIME_COMPONENTS)) {
                notificationDetails.matchDateTimeComponents = DateTimeComponents.values().get(
                    arguments[MATCH_DATE_TIME_COMPONENTS] as Integer?
                )
            }
            if (arguments.containsKey(MILLISECONDS_SINCE_EPOCH)) {
                notificationDetails.millisecondsSinceEpoch = arguments[MILLISECONDS_SINCE_EPOCH]
            }
            if (arguments.containsKey(CALLED_AT)) {
                notificationDetails.calledAt = arguments[CALLED_AT]
            }
            if (arguments.containsKey(REPEAT_INTERVAL)) {
                notificationDetails.repeatInterval =
                    RepeatInterval.values().get(arguments[REPEAT_INTERVAL] as Integer?)
            }
            if (arguments.containsKey(REPEAT_TIME)) {
                notificationDetails.repeatTime = Time.from(arguments[REPEAT_TIME])
            }
            if (arguments.containsKey(DAY)) {
                notificationDetails.day = arguments[DAY] as Integer?
            }
            readPlatformSpecifics(arguments, notificationDetails)
            return notificationDetails
        }

        private fun readPlatformSpecifics(
            arguments: Map<String?, Object?>, notificationDetails: NotificationDetails
        ) {
            @SuppressWarnings("unchecked") val platformChannelSpecifics: Map<String, Object>? =
                arguments[PLATFORM_SPECIFICS]
            if (platformChannelSpecifics != null) {
                notificationDetails.autoCancel = platformChannelSpecifics[AUTO_CANCEL]
                notificationDetails.ongoing = platformChannelSpecifics[ONGOING]
                notificationDetails.style =
                    NotificationStyle.values().get(platformChannelSpecifics[STYLE] as Integer?)
                readStyleInformation(notificationDetails, platformChannelSpecifics)
                notificationDetails.icon = platformChannelSpecifics[ICON]
                notificationDetails.priority = platformChannelSpecifics[PRIORITY] as Integer?
                readSoundInformation(notificationDetails, platformChannelSpecifics)
                notificationDetails.enableVibration = platformChannelSpecifics[ENABLE_VIBRATION]
                notificationDetails.vibrationPattern = platformChannelSpecifics[VIBRATION_PATTERN]
                readGroupingInformation(notificationDetails, platformChannelSpecifics)
                notificationDetails.onlyAlertOnce = platformChannelSpecifics[ONLY_ALERT_ONCE]
                notificationDetails.showWhen = platformChannelSpecifics[SHOW_WHEN]
                notificationDetails.`when` = parseLong(
                    platformChannelSpecifics[WHEN]
                )
                notificationDetails.usesChronometer = platformChannelSpecifics[USES_CHRONOMETER]
                readProgressInformation(notificationDetails, platformChannelSpecifics)
                readColor(notificationDetails, platformChannelSpecifics)
                readChannelInformation(notificationDetails, platformChannelSpecifics)
                readLedInformation(notificationDetails, platformChannelSpecifics)
                readLargeIconInformation(notificationDetails, platformChannelSpecifics)
                notificationDetails.ticker = platformChannelSpecifics[TICKER]
                notificationDetails.visibility = platformChannelSpecifics[VISIBILITY] as Integer?
                notificationDetails.allowWhileIdle = platformChannelSpecifics[ALLOW_WHILE_IDLE]
                notificationDetails.timeoutAfter = parseLong(
                    platformChannelSpecifics[TIMEOUT_AFTER]
                )
                notificationDetails.category = platformChannelSpecifics[CATEGORY]
                notificationDetails.fullScreenIntent = platformChannelSpecifics[FULL_SCREEN_INTENT]
                notificationDetails.shortcutId = platformChannelSpecifics[SHORTCUT_ID]
                notificationDetails.additionalFlags = platformChannelSpecifics[ADDITIONAL_FLAGS]
                notificationDetails.subText = platformChannelSpecifics[SUB_TEXT]
                notificationDetails.tag = platformChannelSpecifics[TAG]
            }
        }

        private fun parseLong(`object`: Object?): Long? {
            if (`object` is Integer) {
                return (`object` as Integer?).longValue()
            }
            return if (`object` is Long) {
                `object`
            } else null
        }

        private fun readProgressInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            notificationDetails.showProgress = platformChannelSpecifics[SHOW_PROGRESS]
            if (platformChannelSpecifics.containsKey(MAX_PROGRESS)) {
                notificationDetails.maxProgress = platformChannelSpecifics[MAX_PROGRESS] as Integer?
            }
            if (platformChannelSpecifics.containsKey(PROGRESS)) {
                notificationDetails.progress = platformChannelSpecifics[PROGRESS] as Integer?
            }
            if (platformChannelSpecifics.containsKey(INDETERMINATE)) {
                notificationDetails.indeterminate = platformChannelSpecifics[INDETERMINATE]
            }
        }

        private fun readLargeIconInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            notificationDetails.largeIcon = platformChannelSpecifics[LARGE_ICON]
            if (platformChannelSpecifics.containsKey(LARGE_ICON_BITMAP_SOURCE)) {
                val argumentValue: Integer? =
                    platformChannelSpecifics[LARGE_ICON_BITMAP_SOURCE] as Integer?
                if (argumentValue != null) {
                    notificationDetails.largeIconBitmapSource =
                        BitmapSource.values().get(argumentValue)
                }
            }
        }

        private fun readGroupingInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            notificationDetails.groupKey = platformChannelSpecifics[GROUP_KEY]
            notificationDetails.setAsGroupSummary = platformChannelSpecifics[SET_AS_GROUP_SUMMARY]
            notificationDetails.groupAlertBehavior =
                platformChannelSpecifics[GROUP_ALERT_BEHAVIOR] as Integer?
        }

        private fun readSoundInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            notificationDetails.playSound = platformChannelSpecifics[PLAY_SOUND]
            notificationDetails.sound = platformChannelSpecifics[SOUND]
            val soundSourceIndex: Integer? = platformChannelSpecifics[SOUND_SOURCE] as Integer?
            if (soundSourceIndex != null) {
                notificationDetails.soundSource = SoundSource.values().get(soundSourceIndex)
            }
        }

        private fun readColor(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            val a: Integer? = platformChannelSpecifics[COLOR_ALPHA] as Integer?
            val r: Integer? = platformChannelSpecifics[COLOR_RED] as Integer?
            val g: Integer? = platformChannelSpecifics[COLOR_GREEN] as Integer?
            val b: Integer? = platformChannelSpecifics[COLOR_BLUE] as Integer?
            if (a != null && r != null && g != null && b != null) {
                notificationDetails.color = Color.argb(a, r, g, b)
            }
        }

        private fun readLedInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            val a: Integer? = platformChannelSpecifics[LED_COLOR_ALPHA] as Integer?
            val r: Integer? = platformChannelSpecifics[LED_COLOR_RED] as Integer?
            val g: Integer? = platformChannelSpecifics[LED_COLOR_GREEN] as Integer?
            val b: Integer? = platformChannelSpecifics[LED_COLOR_BLUE] as Integer?
            if (a != null && r != null && g != null && b != null) {
                notificationDetails.ledColor = Color.argb(a, r, g, b)
            }
            notificationDetails.enableLights = platformChannelSpecifics[ENABLE_LIGHTS]
            notificationDetails.ledOnMs = platformChannelSpecifics[LED_ON_MS] as Integer?
            notificationDetails.ledOffMs = platformChannelSpecifics[LED_OFF_MS] as Integer?
        }

        private fun readChannelInformation(
            notificationDetails: NotificationDetails, platformChannelSpecifics: Map<String, Object>
        ) {
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                notificationDetails.channelId = platformChannelSpecifics[CHANNEL_ID]
                notificationDetails.channelName = platformChannelSpecifics[CHANNEL_NAME]
                notificationDetails.channelDescription =
                    platformChannelSpecifics[CHANNEL_DESCRIPTION]
                notificationDetails.importance = platformChannelSpecifics[IMPORTANCE] as Integer?
                notificationDetails.channelShowBadge = platformChannelSpecifics[CHANNEL_SHOW_BADGE]
                notificationDetails.channelAction = NotificationChannelAction.values().get(
                    platformChannelSpecifics[CHANNEL_ACTION] as Integer?
                )
            }
        }

        @SuppressWarnings("unchecked")
        private fun readStyleInformation(
            notificationDetails: NotificationDetails, platformSpecifics: Map<String, Object>
        ) {
            val styleInformation: Map<String, Object>? = platformSpecifics[STYLE_INFORMATION]
            val defaultStyleInformation: DefaultStyleInformation =
                getDefaultStyleInformation(styleInformation)
            if (notificationDetails.style === NotificationStyle.Default) {
                notificationDetails.styleInformation = defaultStyleInformation
            } else if (notificationDetails.style === NotificationStyle.BigPicture) {
                readBigPictureStyleInformation(
                    notificationDetails, styleInformation, defaultStyleInformation
                )
            } else if (notificationDetails.style === NotificationStyle.BigText) {
                readBigTextStyleInformation(
                    notificationDetails,
                    styleInformation,
                    defaultStyleInformation
                )
            } else if (notificationDetails.style === NotificationStyle.Inbox) {
                readInboxStyleInformation(
                    notificationDetails,
                    styleInformation,
                    defaultStyleInformation
                )
            } else if (notificationDetails.style === NotificationStyle.Messaging) {
                readMessagingStyleInformation(
                    notificationDetails,
                    styleInformation,
                    defaultStyleInformation
                )
            } else if (notificationDetails.style === NotificationStyle.Media) {
                notificationDetails.styleInformation = defaultStyleInformation
            }
        }

        @SuppressWarnings("unchecked")
        private fun readMessagingStyleInformation(
            notificationDetails: NotificationDetails,
            styleInformation: Map<String, Object>?,
            defaultStyleInformation: DefaultStyleInformation
        ) {
            val conversationTitle = styleInformation!![CONVERSATION_TITLE] as String?
            val groupConversation = styleInformation[GROUP_CONVERSATION] as Boolean?
            val person: PersonDetails? = readPersonDetails(
                styleInformation[PERSON] as Map<String, Object>?
            )
            val messages: ArrayList<MessageDetails> = readMessages(
                styleInformation[MESSAGES] as ArrayList<Map<String, Object>>?
            )
            notificationDetails.styleInformation = MessagingStyleInformation(
                person,
                conversationTitle,
                groupConversation,
                messages,
                defaultStyleInformation.htmlFormatTitle,
                defaultStyleInformation.htmlFormatBody
            )
        }

        private fun readPersonDetails(person: Map<String, Object>?): PersonDetails? {
            if (person == null) {
                return null
            }
            val bot = person[BOT] as Boolean?
            val icon: Object? = person[ICON]
            val iconSourceIndex: Integer? = person[ICON_SOURCE] as Integer?
            val iconSource: IconSource? =
                if (iconSourceIndex == null) null else IconSource.values().get(iconSourceIndex)
            val important = person[IMPORTANT] as Boolean?
            val key = person[KEY] as String?
            val name = person[NAME] as String?
            val uri = person[URI] as String?
            return PersonDetails(bot, icon, iconSource, important, key, name, uri)
        }

        @SuppressWarnings("unchecked")
        private fun readMessages(messages: ArrayList<Map<String, Object>>?): ArrayList<MessageDetails> {
            val result: ArrayList<MessageDetails> = ArrayList()
            if (messages != null) {
                for (messageData in messages) {
                    result.add(
                        MessageDetails(
                            messageData[TEXT] as String,
                            messageData[TIMESTAMP] as Long,
                            readPersonDetails(
                                messageData[PERSON] as Map<String, Object>
                            ),
                            messageData[DATA_MIME_TYPE] as String,
                            messageData[DATA_URI] as String
                        )
                    )
                }
            }
            return result
        }

        private fun readInboxStyleInformation(
            notificationDetails: NotificationDetails,
            styleInformation: Map<String, Object>?,
            defaultStyleInformation: DefaultStyleInformation
        ) {
            val contentTitle = styleInformation!![CONTENT_TITLE] as String?
            val htmlFormatContentTitle = styleInformation[HTML_FORMAT_CONTENT_TITLE] as Boolean?
            val summaryText = styleInformation[SUMMARY_TEXT] as String?
            val htmlFormatSummaryText = styleInformation[HTML_FORMAT_SUMMARY_TEXT] as Boolean?
            @SuppressWarnings("unchecked") val lines: ArrayList<String>? =
                styleInformation[LINES] as ArrayList<String>?
            val htmlFormatLines = styleInformation[HTML_FORMAT_LINES] as Boolean?
            notificationDetails.styleInformation = InboxStyleInformation(
                defaultStyleInformation.htmlFormatTitle,
                defaultStyleInformation.htmlFormatBody,
                contentTitle,
                htmlFormatContentTitle,
                summaryText,
                htmlFormatSummaryText,
                lines,
                htmlFormatLines
            )
        }

        private fun readBigTextStyleInformation(
            notificationDetails: NotificationDetails,
            styleInformation: Map<String, Object>?,
            defaultStyleInformation: DefaultStyleInformation
        ) {
            val bigText = styleInformation!![BIG_TEXT] as String?
            val htmlFormatBigText = styleInformation[HTML_FORMAT_BIG_TEXT] as Boolean?
            val contentTitle = styleInformation[CONTENT_TITLE] as String?
            val htmlFormatContentTitle = styleInformation[HTML_FORMAT_CONTENT_TITLE] as Boolean?
            val summaryText = styleInformation[SUMMARY_TEXT] as String?
            val htmlFormatSummaryText = styleInformation[HTML_FORMAT_SUMMARY_TEXT] as Boolean?
            notificationDetails.styleInformation = BigTextStyleInformation(
                defaultStyleInformation.htmlFormatTitle,
                defaultStyleInformation.htmlFormatBody,
                bigText,
                htmlFormatBigText,
                contentTitle,
                htmlFormatContentTitle,
                summaryText,
                htmlFormatSummaryText
            )
        }

        private fun readBigPictureStyleInformation(
            notificationDetails: NotificationDetails,
            styleInformation: Map<String, Object>?,
            defaultStyleInformation: DefaultStyleInformation
        ) {
            val contentTitle = styleInformation!![CONTENT_TITLE] as String?
            val htmlFormatContentTitle = styleInformation[HTML_FORMAT_CONTENT_TITLE] as Boolean?
            val summaryText = styleInformation[SUMMARY_TEXT] as String?
            val htmlFormatSummaryText = styleInformation[HTML_FORMAT_SUMMARY_TEXT] as Boolean?
            val largeIcon: Object? = styleInformation[LARGE_ICON]
            var largeIconBitmapSource: BitmapSource? = null
            if (styleInformation.containsKey(LARGE_ICON_BITMAP_SOURCE)) {
                val largeIconBitmapSourceArgument: Integer? =
                    styleInformation[LARGE_ICON_BITMAP_SOURCE] as Integer?
                largeIconBitmapSource = BitmapSource.values().get(largeIconBitmapSourceArgument)
            }
            val bigPicture: Object? = styleInformation[BIG_PICTURE]
            val bigPictureBitmapSourceArgument: Integer? =
                styleInformation[BIG_PICTURE_BITMAP_SOURCE] as Integer?
            val bigPictureBitmapSource: BitmapSource =
                BitmapSource.values().get(bigPictureBitmapSourceArgument)
            val showThumbnail = styleInformation[HIDE_EXPANDED_LARGE_ICON] as Boolean?
            notificationDetails.styleInformation = BigPictureStyleInformation(
                defaultStyleInformation.htmlFormatTitle,
                defaultStyleInformation.htmlFormatBody,
                contentTitle,
                htmlFormatContentTitle,
                summaryText,
                htmlFormatSummaryText,
                largeIcon,
                largeIconBitmapSource,
                bigPicture,
                bigPictureBitmapSource,
                showThumbnail
            )
        }

        private fun getDefaultStyleInformation(
            styleInformation: Map<String, Object>?
        ): DefaultStyleInformation {
            val htmlFormatTitle = styleInformation!![HTML_FORMAT_TITLE] as Boolean?
            val htmlFormatBody = styleInformation[HTML_FORMAT_CONTENT] as Boolean?
            return DefaultStyleInformation(htmlFormatTitle, htmlFormatBody)
        }
    }
}