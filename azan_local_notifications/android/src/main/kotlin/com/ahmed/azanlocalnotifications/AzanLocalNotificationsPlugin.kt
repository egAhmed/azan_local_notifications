package com.ahmed.azanlocalnotifications

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.ahmed.azannotifications.models.BitmapSource;
import com.ahmed.azannotifications.models.DateTimeComponents;
import com.ahmed.azannotifications.models.IconSource;
import com.ahmed.azannotifications.models.MessageDetails;
import com.ahmed.azannotifications.models.NotificationChannelAction;
import com.ahmed.azannotifications.models.NotificationChannelDetails;
import com.ahmed.azannotifications.models.NotificationChannelGroupDetails;
import com.ahmed.azannotifications.models.NotificationDetails;
import com.ahmed.azannotifications.models.PersonDetails;
import com.ahmed.azannotifications.models.ScheduledNotificationRepeatFrequency;
import com.ahmed.azannotifications.models.SoundSource;
import com.ahmed.azannotifications.models.styles.BigPictureStyleInformation;
import com.ahmed.azannotifications.models.styles.BigTextStyleInformation;
import com.ahmed.azannotifications.models.styles.DefaultStyleInformation;
import com.ahmed.azannotifications.models.styles.InboxStyleInformation;
import com.ahmed.azannotifications.models.styles.MessagingStyleInformation;
import com.ahmed.azannotifications.models.styles.StyleInformation;
import com.ahmed.azannotifications.utils.BooleanUtils;
import com.ahmed.azannotifications.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.loader.FlutterLoader;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** AzanNotificationsPlugin */
class AzanLoaclNotificationsPlugin:  MethodCallHandler, PluginRegistry.NewIntentListener,
  FlutterPlugin, ActivityAware {
  private var channel: MethodChannel? = null
  private var applicationContext: Context? = null
  private var mainActivity: Activity? = null
  private var launchIntent: Intent? = null
  private fun setActivity(flutterActivity: Activity) {
    mainActivity = flutterActivity
    if (mainActivity != null) {
      launchIntent = mainActivity.getIntent()
    }
  }

  private fun onAttachedToEngine(context: Context, binaryMessenger: BinaryMessenger) {
    applicationContext = context
    channel = MethodChannel(binaryMessenger, METHOD_CHANNEL)
    channel.setMethodCallHandler(this)
  }

  @Override
  fun onAttachedToEngine(binding: FlutterPluginBinding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger())
  }

  @Override
  fun onDetachedFromEngine(binding: FlutterPluginBinding?) {
  }

  @Override
  fun onAttachedToActivity(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    mainActivity = binding.getActivity()
    launchIntent = mainActivity.getIntent()
  }

  @Override
  fun onDetachedFromActivityForConfigChanges() {
    mainActivity = null
  }

  @Override
  fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    mainActivity = binding.getActivity()
  }

  @Override
  fun onDetachedFromActivity() {
    mainActivity = null
  }

  @Override
  fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      INITIALIZE_METHOD -> {
        initialize(call, result)
      }
      GET_NOTIFICATION_APP_LAUNCH_DETAILS_METHOD -> {
        getNotificationAppLaunchDetails(result)
      }
      SHOW_METHOD -> {
        show(call, result)
      }
      SCHEDULE_METHOD -> {
        schedule(call, result)
      }
      ZONED_SCHEDULE_METHOD -> {
        zonedSchedule(call, result)
      }
      PERIODICALLY_SHOW_METHOD, SHOW_DAILY_AT_TIME_METHOD, SHOW_WEEKLY_AT_DAY_AND_TIME_METHOD -> {
        repeat(call, result)
      }
      CANCEL_METHOD -> cancel(call, result)
      CANCEL_ALL_METHOD -> cancelAllNotifications(result)
      PENDING_NOTIFICATION_REQUESTS_METHOD -> pendingNotificationRequests(result)
      CREATE_NOTIFICATION_CHANNEL_GROUP_METHOD -> createNotificationChannelGroup(call, result)
      DELETE_NOTIFICATION_CHANNEL_GROUP_METHOD -> deleteNotificationChannelGroup(call, result)
      CREATE_NOTIFICATION_CHANNEL_METHOD -> createNotificationChannel(call, result)
      DELETE_NOTIFICATION_CHANNEL_METHOD -> deleteNotificationChannel(call, result)
      GET_ACTIVE_NOTIFICATIONS_METHOD -> getActiveNotifications(result)
      GET_NOTIFICATION_CHANNELS_METHOD -> getNotificationChannels(result)
      START_FOREGROUND_SERVICE -> startForegroundService(call, result)
      STOP_FOREGROUND_SERVICE -> stopForegroundService(result)
      else -> result.notImplemented()
    }
  }

  private fun pendingNotificationRequests(result: Result) {
    val scheduledNotifications: ArrayList<NotificationDetails> =
      loadScheduledNotifications(applicationContext)
    val pendingNotifications: List<Map<String, Object>> = ArrayList()
    for (scheduledNotification in scheduledNotifications) {
      val pendingNotification: HashMap<String, Object> = HashMap()
      pendingNotification.put("id", scheduledNotification.id)
      pendingNotification.put("title", scheduledNotification.title)
      pendingNotification.put("body", scheduledNotification.body)
      pendingNotification.put("payload", scheduledNotification.payload)
      pendingNotifications.add(pendingNotification)
    }
    result.success(pendingNotifications)
  }

  private fun cancel(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val id: Integer? = arguments[CANCEL_ID] as Integer?
    val tag = arguments[CANCEL_TAG] as String?
    cancelNotification(id, tag)
    result.success(null)
  }

  private fun repeat(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val notificationDetails: NotificationDetails? =
      extractNotificationDetails(result, arguments)
    if (notificationDetails != null) {
      repeatNotification(applicationContext, notificationDetails, true)
      result.success(null)
    }
  }

  private fun schedule(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val notificationDetails: NotificationDetails? =
      extractNotificationDetails(result, arguments)
    if (notificationDetails != null) {
      scheduleNotification(applicationContext, notificationDetails, true)
      result.success(null)
    }
  }

  private fun zonedSchedule(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val notificationDetails: NotificationDetails? =
      extractNotificationDetails(result, arguments)
    if (notificationDetails != null) {
      if (notificationDetails.matchDateTimeComponents != null) {
        notificationDetails.scheduledDateTime =
          getNextFireDateMatchingDateTimeComponents(notificationDetails)
      }
      zonedScheduleNotification(applicationContext, notificationDetails, true)
      result.success(null)
    }
  }

  private fun show(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val notificationDetails: NotificationDetails? =
      extractNotificationDetails(result, arguments)
    if (notificationDetails != null) {
      showNotification(applicationContext, notificationDetails)
      result.success(null)
    }
  }

  private fun getNotificationAppLaunchDetails(result: Result) {
    val notificationAppLaunchDetails: Map<String, Object> = HashMap()
    var payload: String? = null
    val notificationLaunchedApp = (mainActivity != null && SELECT_NOTIFICATION.equals(
      mainActivity.getIntent().getAction()
    )
            && !launchedActivityFromHistory(mainActivity.getIntent()))
    notificationAppLaunchDetails.put(NOTIFICATION_LAUNCHED_APP, notificationLaunchedApp)
    if (notificationLaunchedApp) {
      payload = launchIntent.getStringExtra(PAYLOAD)
    }
    notificationAppLaunchDetails.put(PAYLOAD, payload)
    result.success(notificationAppLaunchDetails)
  }

  private fun initialize(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val defaultIcon = arguments[DEFAULT_ICON] as String?
    if (!isValidDrawableResource(
        applicationContext, defaultIcon, result, INVALID_ICON_ERROR_CODE
      )
    ) {
      return
    }
    initAndroidThreeTen(applicationContext)
    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
      SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
    )
    val editor: Editor = sharedPreferences.edit()
    editor.putString(DEFAULT_ICON, defaultIcon)
    tryCommittingInBackground(editor, 3)
    result.success(true)
  }

  /// Extracts the details of the notifications passed from the Flutter side and also validates that
  // some of the details (especially resources) passed are valid
  private fun extractNotificationDetails(
    result: Result, arguments: Map<String, Object>
  ): NotificationDetails? {
    val notificationDetails: NotificationDetails = NotificationDetails.from(arguments)
    return if (hasInvalidIcon(result, notificationDetails.icon)
      || hasInvalidLargeIcon(
        result, notificationDetails.largeIcon, notificationDetails.largeIconBitmapSource
      )
      || hasInvalidBigPictureResources(result, notificationDetails)
      || hasInvalidRawSoundResource(result, notificationDetails)
      || hasInvalidLedDetails(result, notificationDetails)
    ) {
      null
    } else notificationDetails
  }

  private fun hasInvalidLedDetails(
    result: Result,
    notificationDetails: NotificationDetails
  ): Boolean {
    if (notificationDetails.ledColor != null
      && (notificationDetails.ledOnMs == null || notificationDetails.ledOffMs == null)
    ) {
      result.error(INVALID_LED_DETAILS_ERROR_CODE, INVALID_LED_DETAILS_ERROR_MESSAGE, null)
      return true
    }
    return false
  }

  private fun hasInvalidRawSoundResource(
    result: Result, notificationDetails: NotificationDetails
  ): Boolean {
    if (!StringUtils.isNullOrEmpty(notificationDetails.sound)
      && (notificationDetails.soundSource == null
              || notificationDetails.soundSource === SoundSource.RawResource)
    ) {
      val soundResourceId: Int = applicationContext
        .getResources()
        .getIdentifier(
          notificationDetails.sound,
          "raw",
          applicationContext.getPackageName()
        )
      if (soundResourceId == 0) {
        result.error(
          INVALID_SOUND_ERROR_CODE,
          String.format(INVALID_RAW_RESOURCE_ERROR_MESSAGE, notificationDetails.sound),
          null
        )
        return true
      }
    }
    return false
  }

  private fun hasInvalidBigPictureResources(
    result: Result, notificationDetails: NotificationDetails
  ): Boolean {
    if (notificationDetails.style === NotificationStyle.BigPicture) {
      val bigPictureStyleInformation: BigPictureStyleInformation =
        notificationDetails.styleInformation as BigPictureStyleInformation
      if (hasInvalidLargeIcon(
          result,
          bigPictureStyleInformation.largeIcon,
          bigPictureStyleInformation.largeIconBitmapSource
        )
      ) return true
      if (bigPictureStyleInformation.bigPictureBitmapSource === BitmapSource.DrawableResource) {
        val bigPictureResourceName = bigPictureStyleInformation.bigPicture as String
        return (StringUtils.isNullOrEmpty(bigPictureResourceName)
                && !isValidDrawableResource(
          applicationContext,
          bigPictureResourceName,
          result,
          INVALID_BIG_PICTURE_ERROR_CODE
        ))
      } else if (bigPictureStyleInformation.bigPictureBitmapSource === BitmapSource.FilePath) {
        val largeIconPath = bigPictureStyleInformation.bigPicture as String
        return StringUtils.isNullOrEmpty(largeIconPath)
      } else if (bigPictureStyleInformation.bigPictureBitmapSource === BitmapSource.ByteArray) {
        val byteArray = bigPictureStyleInformation.bigPicture as ByteArray
        return byteArray == null || byteArray.size == 0
      }
    }
    return false
  }

  private fun hasInvalidLargeIcon(
    result: Result, largeIcon: Object, largeIconBitmapSource: BitmapSource
  ): Boolean {
    if (largeIconBitmapSource === BitmapSource.DrawableResource
      || largeIconBitmapSource === BitmapSource.FilePath
    ) {
      val largeIconPath = largeIcon as String
      return (!StringUtils.isNullOrEmpty(largeIconPath)
              && largeIconBitmapSource === BitmapSource.DrawableResource && !isValidDrawableResource(
        applicationContext, largeIconPath, result, INVALID_LARGE_ICON_ERROR_CODE
      ))
    } else if (largeIconBitmapSource === BitmapSource.ByteArray) {
      val byteArray = largeIcon as ByteArray
      return byteArray.size == 0
    }
    return false
  }

  private fun hasInvalidIcon(result: Result, icon: String): Boolean {
    return (!StringUtils.isNullOrEmpty(icon)
            && !isValidDrawableResource(
      applicationContext,
      icon,
      result,
      INVALID_ICON_ERROR_CODE
    ))
  }

  private fun cancelNotification(id: Integer?, tag: String?) {
    val intent = Intent(applicationContext, ScheduledNotificationReceiver::class.java)
    val pendingIntent: PendingIntent = getBroadcastPendingIntent(applicationContext, id, intent)
    val alarmManager: AlarmManager = getAlarmManager(applicationContext)
    alarmManager.cancel(pendingIntent)
    val notificationManager: NotificationManagerCompat =
      getNotificationManager(applicationContext)
    if (tag == null) {
      notificationManager.cancel(id)
    } else {
      notificationManager.cancel(tag, id)
    }
    removeNotificationFromCache(applicationContext, id)
  }

  private fun cancelAllNotifications(result: Result) {
    val notificationManager: NotificationManagerCompat =
      getNotificationManager(applicationContext)
    notificationManager.cancelAll()
    val scheduledNotifications: ArrayList<NotificationDetails> =
      loadScheduledNotifications(applicationContext)
    if (scheduledNotifications == null || scheduledNotifications.isEmpty()) {
      result.success(null)
      return
    }
    val intent = Intent(applicationContext, ScheduledNotificationReceiver::class.java)
    for (scheduledNotification in scheduledNotifications) {
      val pendingIntent: PendingIntent =
        getBroadcastPendingIntent(applicationContext, scheduledNotification.id, intent)
      val alarmManager: AlarmManager = getAlarmManager(applicationContext)
      alarmManager.cancel(pendingIntent)
    }
    saveScheduledNotifications(applicationContext, ArrayList<NotificationDetails>())
    result.success(null)
  }

  @Override
  fun onNewIntent(intent: Intent): Boolean {
    val res = sendNotificationPayloadMessage(intent)
    if (res && mainActivity != null) {
      mainActivity.setIntent(intent)
    }
    return res
  }

  private fun sendNotificationPayloadMessage(intent: Intent): Boolean {
    if (SELECT_NOTIFICATION.equals(intent.getAction())) {
      val payload: String = intent.getStringExtra(PAYLOAD)
      channel.invokeMethod("selectNotification", payload)
      return true
    }
    return false
  }

  private fun createNotificationChannelGroup(call: MethodCall, result: Result) {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      val arguments: Map<String, Object> = call.arguments()
      val notificationChannelGroupDetails: NotificationChannelGroupDetails =
        NotificationChannelGroupDetails.from(arguments)
      val notificationManager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val notificationChannelGroup = NotificationChannelGroup(
        notificationChannelGroupDetails.id, notificationChannelGroupDetails.name
      )
      if (VERSION.SDK_INT >= VERSION_CODES.P) {
        notificationChannelGroup.setDescription(notificationChannelGroupDetails.description)
      }
      notificationManager.createNotificationChannelGroup(notificationChannelGroup)
    }
    result.success(null)
  }

  private fun deleteNotificationChannelGroup(call: MethodCall, result: Result) {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      val notificationManager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val groupId: String = call.arguments()
      notificationManager.deleteNotificationChannelGroup(groupId)
    }
    result.success(null)
  }

  private fun createNotificationChannel(call: MethodCall, result: Result) {
    val arguments: Map<String, Object> = call.arguments()
    val notificationChannelDetails: NotificationChannelDetails =
      NotificationChannelDetails.from(arguments)
    setupNotificationChannel(applicationContext, notificationChannelDetails)
    result.success(null)
  }

  private fun deleteNotificationChannel(call: MethodCall, result: Result) {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      val notificationManager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val channelId: String = call.arguments()
      notificationManager.deleteNotificationChannel(channelId)
    }
    result.success(null)
  }

  private fun getActiveNotifications(result: Result) {
    if (VERSION.SDK_INT < VERSION_CODES.M) {
      result.error(
        GET_ACTIVE_NOTIFICATIONS_ERROR_CODE, GET_ACTIVE_NOTIFICATIONS_ERROR_MESSAGE, null
      )
      return
    }
    val notificationManager: NotificationManager =
      applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    try {
      val activeNotifications: Array<StatusBarNotification> =
        notificationManager.getActiveNotifications()
      val activeNotificationsPayload: List<Map<String, Object>> = ArrayList()
      for (activeNotification in activeNotifications) {
        val activeNotificationPayload: HashMap<String, Object> = HashMap()
        activeNotificationPayload.put("id", activeNotification.getId())
        val notification: Notification = activeNotification.getNotification()
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
          activeNotificationPayload.put("channelId", notification.getChannelId())
        }
        activeNotificationPayload.put("groupKey", notification.getGroup())
        activeNotificationPayload.put(
          "title", notification.extras.getCharSequence("android.title")
        )
        activeNotificationPayload.put(
          "body",
          notification.extras.getCharSequence("android.text")
        )
        activeNotificationsPayload.add(activeNotificationPayload)
      }
      result.success(activeNotificationsPayload)
    } catch (e: Throwable) {
      result.error(GET_ACTIVE_NOTIFICATIONS_ERROR_CODE, e.getMessage(), e.getStackTrace())
    }
  }

  private fun getNotificationChannels(result: Result) {
    try {
      val notificationManagerCompat: NotificationManagerCompat =
        getNotificationManager(applicationContext)
      val channels: List<NotificationChannel> =
        notificationManagerCompat.getNotificationChannels()
      val channelsPayload: List<Map<String, Object>> = ArrayList()
      for (channel in channels) {
        val channelPayload: HashMap<String, Object> = getMappedNotificationChannel(channel)
        channelsPayload.add(channelPayload)
      }
      result.success(channelsPayload)
    } catch (e: Throwable) {
      result.error(GET_NOTIFICATION_CHANNELS_ERROR_CODE, e.getMessage(), e.getStackTrace())
    }
  }

  private fun getMappedNotificationChannel(channel: NotificationChannel): HashMap<String, Object> {
    val channelPayload: HashMap<String, Object> = HashMap()
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      channelPayload.put("id", channel.getId())
      channelPayload.put("name", channel.getName())
      channelPayload.put("description", channel.getDescription())
      channelPayload.put("groupId", channel.getGroup())
      channelPayload.put("showBadge", channel.canShowBadge())
      channelPayload.put("importance", channel.getImportance())
      val soundUri: Uri = channel.getSound()
      if (soundUri == null) {
        channelPayload.put("sound", null)
        channelPayload.put("playSound", false)
      } else {
        channelPayload.put("playSound", true)
        val soundSources: List<SoundSource> = Arrays.asList(SoundSource.values())
        if (soundUri.getScheme().equals("android.resource")) {
          val splitUri: Array<String> = soundUri.toString().split("/")
          val resource = splitUri[splitUri.size - 1]
          val resourceId: Integer? = tryParseInt(resource)
          if (resourceId == null) {
            channelPayload.put(
              "soundSource",
              soundSources.indexOf(SoundSource.RawResource)
            )
            channelPayload.put("sound", resource)
          } else {
            // Kept for backwards compatibility when the source resource used to be based on id
            val resourceName: String =
              applicationContext.getResources().getResourceEntryName(resourceId)
            if (resourceName != null) {
              channelPayload.put(
                "soundSource",
                soundSources.indexOf(SoundSource.RawResource)
              )
              channelPayload.put("sound", resourceName)
            }
          }
        } else {
          channelPayload.put("soundSource", soundSources.indexOf(SoundSource.Uri))
          channelPayload.put("sound", soundUri.toString())
        }
      }
      channelPayload.put("enableVibration", channel.shouldVibrate())
      channelPayload.put("vibrationPattern", channel.getVibrationPattern())
      channelPayload.put("enableLights", channel.shouldShowLights())
      channelPayload.put("ledColor", channel.getLightColor())
    }
    return channelPayload
  }

  private fun tryParseInt(value: String): Integer? {
    return try {
      Integer.parseInt(value)
    } catch (e: NumberFormatException) {
      null
    }
  }

  private fun startForegroundService(call: MethodCall, result: Result) {
    val notificationData: Map<String, Object> =
      call.< Map < String, Object>>argument<Map<String?, Object?>?>("notificationData")
    val startType: Integer = call.< Integer > argument < Integer ? > "startType"
    val foregroundServiceTypes: ArrayList<Integer> = call.argument("foregroundServiceTypes")
    if (foregroundServiceTypes == null || foregroundServiceTypes.size() !== 0) {
      if (notificationData != null && startType != null) {
        val notificationDetails: NotificationDetails? =
          extractNotificationDetails(result, notificationData)
        if (notificationDetails != null) {
          if (notificationDetails.id !== 0) {
            val parameter = ForegroundServiceStartParameter(
              notificationDetails, startType, foregroundServiceTypes
            )
            val intent = Intent(applicationContext, ForegroundService::class.java)
            intent.putExtra(ForegroundServiceStartParameter.EXTRA, parameter)
            ContextCompat.startForegroundService(applicationContext, intent)
            result.success(null)
          } else {
            result.error(
              "ARGUMENT_ERROR",
              "The id of the notification for a foreground service must not be 0!",
              null
            )
          }
        }
      } else {
        result.error(
          "ARGUMENT_ERROR", "An argument passed to startForegroundService was null!", null
        )
      }
    } else {
      result.error(
        "ARGUMENT_ERROR",
        "If foregroundServiceTypes is non-null it must not be empty!",
        null
      )
    }
  }

  private fun stopForegroundService(result: Result) {
    applicationContext.stopService(Intent(applicationContext, ForegroundService::class.java))
    result.success(null)
  }

  companion object {
    private const val SHARED_PREFERENCES_KEY = "notification_plugin_cache"
    private const val DRAWABLE = "drawable"
    private const val DEFAULT_ICON = "defaultIcon"
    private const val SELECT_NOTIFICATION = "SELECT_NOTIFICATION"
    private const val SCHEDULED_NOTIFICATIONS = "scheduled_notifications"
    private const val INITIALIZE_METHOD = "initialize"
    private const val CREATE_NOTIFICATION_CHANNEL_GROUP_METHOD =
      "createNotificationChannelGroup"
    private const val DELETE_NOTIFICATION_CHANNEL_GROUP_METHOD =
      "deleteNotificationChannelGroup"
    private const val CREATE_NOTIFICATION_CHANNEL_METHOD = "createNotificationChannel"
    private const val DELETE_NOTIFICATION_CHANNEL_METHOD = "deleteNotificationChannel"
    private const val GET_ACTIVE_NOTIFICATIONS_METHOD = "getActiveNotifications"
    private const val GET_NOTIFICATION_CHANNELS_METHOD = "getNotificationChannels"
    private const val START_FOREGROUND_SERVICE = "startForegroundService"
    private const val STOP_FOREGROUND_SERVICE = "stopForegroundService"
    private const val PENDING_NOTIFICATION_REQUESTS_METHOD = "pendingNotificationRequests"
    private const val SHOW_METHOD = "show"
    private const val CANCEL_METHOD = "cancel"
    private const val CANCEL_ALL_METHOD = "cancelAll"
    private const val SCHEDULE_METHOD = "schedule"
    private const val ZONED_SCHEDULE_METHOD = "zonedSchedule"
    private const val PERIODICALLY_SHOW_METHOD = "periodicallyShow"
    private const val SHOW_DAILY_AT_TIME_METHOD = "showDailyAtTime"
    private const val SHOW_WEEKLY_AT_DAY_AND_TIME_METHOD = "showWeeklyAtDayAndTime"
    private const val GET_NOTIFICATION_APP_LAUNCH_DETAILS_METHOD =
      "getNotificationAppLaunchDetails"
    private const val METHOD_CHANNEL = "dexterous.com/flutter/local_notifications"
    private const val PAYLOAD = "payload"
    private const val INVALID_ICON_ERROR_CODE = "INVALID_ICON"
    private const val INVALID_LARGE_ICON_ERROR_CODE = "INVALID_LARGE_ICON"
    private const val INVALID_BIG_PICTURE_ERROR_CODE = "INVALID_BIG_PICTURE"
    private const val INVALID_SOUND_ERROR_CODE = "INVALID_SOUND"
    private const val INVALID_LED_DETAILS_ERROR_CODE = "INVALID_LED_DETAILS"
    private const val GET_ACTIVE_NOTIFICATIONS_ERROR_CODE =
      "GET_ACTIVE_NOTIFICATIONS_ERROR_CODE"
    private const val GET_ACTIVE_NOTIFICATIONS_ERROR_MESSAGE =
      "Android version must be 6.0 or newer to use getActiveNotifications"
    private const val GET_NOTIFICATION_CHANNELS_ERROR_CODE =
      "GET_NOTIFICATION_CHANNELS_ERROR_CODE"
    private const val INVALID_LED_DETAILS_ERROR_MESSAGE =
      ("Must specify both ledOnMs and ledOffMs to configure the blink cycle on older versions of"
              + " Android before Oreo")
    private const val NOTIFICATION_LAUNCHED_APP = "notificationLaunchedApp"
    private const val INVALID_DRAWABLE_RESOURCE_ERROR_MESSAGE =
      ("The resource %s could not be found. Please make sure it has been added as a drawable"
              + " resource to your Android head project.")
    private const val INVALID_RAW_RESOURCE_ERROR_MESSAGE =
      ("The resource %s could not be found. Please make sure it has been added as a raw resource to"
              + " your Android head project.")
    private const val CANCEL_ID = "id"
    private const val CANCEL_TAG = "tag"
    var NOTIFICATION_DETAILS = "notificationDetails"
    var gson: Gson? = null
    @SuppressWarnings("deprecation")
    fun registerWith(registrar: io.flutter.plugin.common.PluginRegistry.Registrar) {
      val plugin = FlutterLocalNotificationsPlugin()
      plugin.setActivity(registrar.activity())
      registrar.addNewIntentListener(plugin)
      plugin.onAttachedToEngine(registrar.context(), registrar.messenger())
    }

    fun rescheduleNotifications(context: Context) {
      initAndroidThreeTen(context)
      val scheduledNotifications: ArrayList<NotificationDetails> =
        loadScheduledNotifications(context)
      for (scheduledNotification in scheduledNotifications) {
        if (scheduledNotification.repeatInterval == null) {
          if (scheduledNotification.timeZoneName == null) {
            scheduleNotification(context, scheduledNotification, false)
          } else {
            zonedScheduleNotification(context, scheduledNotification, false)
          }
        } else {
          repeatNotification(context, scheduledNotification, false)
        }
      }
    }

    private fun initAndroidThreeTen(context: Context?) {
      if (VERSION.SDK_INT < VERSION_CODES.O) {
        AndroidThreeTen.init(context)
      }
    }

    protected fun createNotification(
      context: Context, notificationDetails: NotificationDetails
    ): Notification {
      val notificationChannelDetails: NotificationChannelDetails =
        NotificationChannelDetails.fromNotificationDetails(notificationDetails)
      if (canCreateNotificationChannel(context, notificationChannelDetails)) {
        setupNotificationChannel(context, notificationChannelDetails)
      }
      val intent: Intent = getLaunchIntent(context)
      intent.setAction(SELECT_NOTIFICATION)
      intent.putExtra(PAYLOAD, notificationDetails.payload)
      var flags: Int = PendingIntent.FLAG_UPDATE_CURRENT
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
      }
      val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, notificationDetails.id, intent, flags)
      val defaultStyleInformation: DefaultStyleInformation =
        notificationDetails.styleInformation as DefaultStyleInformation
      val builder: NotificationCompat.Builder =
        Builder(context, notificationDetails.channelId)
          .setContentTitle(
            if (defaultStyleInformation.htmlFormatTitle) fromHtml(notificationDetails.title) else notificationDetails.title
          )
          .setContentText(
            if (defaultStyleInformation.htmlFormatBody) fromHtml(notificationDetails.body) else notificationDetails.body
          )
          .setTicker(notificationDetails.ticker)
          .setAutoCancel(BooleanUtils.getValue(notificationDetails.autoCancel))
          .setContentIntent(pendingIntent)
          .setPriority(notificationDetails.priority)
          .setOngoing(BooleanUtils.getValue(notificationDetails.ongoing))
          .setOnlyAlertOnce(BooleanUtils.getValue(notificationDetails.onlyAlertOnce))
      setSmallIcon(context, notificationDetails, builder)
      builder.setLargeIcon(
        getBitmapFromSource(
          context,
          notificationDetails.largeIcon,
          notificationDetails.largeIconBitmapSource
        )
      )
      if (notificationDetails.color != null) {
        builder.setColor(notificationDetails.color.intValue())
      }
      if (notificationDetails.showWhen != null) {
        builder.setShowWhen(BooleanUtils.getValue(notificationDetails.showWhen))
      }
      if (notificationDetails.`when` != null) {
        builder.setWhen(notificationDetails.`when`)
      }
      if (notificationDetails.usesChronometer != null) {
        builder.setUsesChronometer(notificationDetails.usesChronometer)
      }
      if (BooleanUtils.getValue(notificationDetails.fullScreenIntent)) {
        builder.setFullScreenIntent(pendingIntent, true)
      }
      if (!StringUtils.isNullOrEmpty(notificationDetails.shortcutId)) {
        builder.setShortcutId(notificationDetails.shortcutId)
      }
      if (!StringUtils.isNullOrEmpty(notificationDetails.subText)) {
        builder.setSubText(notificationDetails.subText)
      }
      setVisibility(notificationDetails, builder)
      applyGrouping(notificationDetails, builder)
      setSound(context, notificationDetails, builder)
      setVibrationPattern(notificationDetails, builder)
      setLights(notificationDetails, builder)
      setStyle(context, notificationDetails, builder)
      setProgress(notificationDetails, builder)
      setCategory(notificationDetails, builder)
      setTimeoutAfter(notificationDetails, builder)
      val notification: Notification = builder.build()
      if (notificationDetails.additionalFlags != null
        && notificationDetails.additionalFlags.length > 0
      ) {
        for (additionalFlag in notificationDetails.additionalFlags) {
          notification.flags = notification.flags or additionalFlag
        }
      }
      return notification
    }

    private fun canCreateNotificationChannel(
      context: Context, notificationChannelDetails: NotificationChannelDetails
    ): Boolean {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        val notificationManager: NotificationManager =
          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel: NotificationChannel =
          notificationManager.getNotificationChannel(notificationChannelDetails.id)
        // only create/update the channel when needed/specified. Allow this happen to when
        // channelAction may be null to support cases where notifications had been
        // created on older versions of the plugin where channel management options weren't available
        // back then
        return ((notificationChannel == null
                && (notificationChannelDetails.channelAction == null
                || notificationChannelDetails.channelAction
                === NotificationChannelAction.CreateIfNotExists))
                || (notificationChannel != null
                && notificationChannelDetails.channelAction === NotificationChannelAction.Update))
      }
      return false
    }

    private fun setSmallIcon(
      context: Context,
      notificationDetails: NotificationDetails,
      builder: NotificationCompat.Builder
    ) {
      if (!StringUtils.isNullOrEmpty(notificationDetails.icon)) {
        builder.setSmallIcon(getDrawableResourceId(context, notificationDetails.icon))
      } else {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
          SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val defaultIcon: String = sharedPreferences.getString(DEFAULT_ICON, null)
        if (StringUtils.isNullOrEmpty(defaultIcon)) {
          // for backwards compatibility: this is for handling the old way references to the icon used
          // to be kept but should be removed in future
          builder.setSmallIcon(notificationDetails.iconResourceId)
        } else {
          builder.setSmallIcon(getDrawableResourceId(context, defaultIcon))
        }
      }
    }

    @NonNull
    fun buildGson(): Gson? {
      if (gson == null) {
        val styleInformationAdapter: RuntimeTypeAdapterFactory<StyleInformation> =
          RuntimeTypeAdapterFactory.of(
            StyleInformation::class.java
          )
            .registerSubtype(DefaultStyleInformation::class.java)
            .registerSubtype(BigTextStyleInformation::class.java)
            .registerSubtype(BigPictureStyleInformation::class.java)
            .registerSubtype(InboxStyleInformation::class.java)
            .registerSubtype(MessagingStyleInformation::class.java)
        val builder: GsonBuilder =
          GsonBuilder().registerTypeAdapterFactory(styleInformationAdapter)
        gson = builder.create()
      }
      return gson
    }

    private fun loadScheduledNotifications(context: Context?): ArrayList<NotificationDetails> {
      var scheduledNotifications: ArrayList<NotificationDetails> = ArrayList()
      val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SCHEDULED_NOTIFICATIONS, Context.MODE_PRIVATE
      )
      val json: String = sharedPreferences.getString(SCHEDULED_NOTIFICATIONS, null)
      if (json != null) {
        val gson: Gson? = buildGson()
        val type: Type = object : TypeToken<ArrayList<NotificationDetails?>?>() {}.getType()
        scheduledNotifications = gson.fromJson(json, type)
      }
      return scheduledNotifications
    }

    private fun saveScheduledNotifications(
      context: Context?, scheduledNotifications: ArrayList<NotificationDetails>
    ) {
      val gson: Gson? = buildGson()
      val json: String = gson.toJson(scheduledNotifications)
      val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SCHEDULED_NOTIFICATIONS, Context.MODE_PRIVATE
      )
      val editor: Editor = sharedPreferences.edit()
      editor.putString(SCHEDULED_NOTIFICATIONS, json)
      tryCommittingInBackground(editor, 3)
    }

    private fun tryCommittingInBackground(
      editor: Editor, tries: Int
    ) {
      if (tries == 0) {
        return
      }
      Thread(
        object : Runnable() {
          @Override
          fun run() {
            val isCommitted: Boolean = editor.commit()
            if (!isCommitted) {
              tryCommittingInBackground(editor, tries - 1)
            }
          }
        })
        .start()
    }

    fun removeNotificationFromCache(context: Context?, notificationId: Integer?) {
      val scheduledNotifications: ArrayList<NotificationDetails> =
        loadScheduledNotifications(context)
      val it: Iterator<NotificationDetails> = scheduledNotifications.iterator()
      while (it.hasNext()) {
        val notificationDetails: NotificationDetails = it.next()
        if (notificationDetails.id.equals(notificationId)) {
          it.remove()
          break
        }
      }
      saveScheduledNotifications(context, scheduledNotifications)
    }

    @SuppressWarnings("deprecation")
    private fun fromHtml(html: String?): Spanned? {
      if (html == null) {
        return null
      }
      return if (VERSION.SDK_INT >= VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
      } else {
        Html.fromHtml(html)
      }
    }

    private fun scheduleNotification(
      context: Context,
      notificationDetails: NotificationDetails,
      updateScheduledNotificationsCache: Boolean
    ) {
      val gson: Gson? = buildGson()
      val notificationDetailsJson: String = gson.toJson(notificationDetails)
      val notificationIntent = Intent(context, ScheduledNotificationReceiver::class.java)
      notificationIntent.putExtra(NOTIFICATION_DETAILS, notificationDetailsJson)
      val pendingIntent: PendingIntent =
        getBroadcastPendingIntent(context, notificationDetails.id, notificationIntent)
      val alarmManager: AlarmManager = getAlarmManager(context)
      if (BooleanUtils.getValue(notificationDetails.allowWhileIdle)) {
        AlarmManagerCompat.setExactAndAllowWhileIdle(
          alarmManager,
          AlarmManager.RTC_WAKEUP,
          notificationDetails.millisecondsSinceEpoch,
          pendingIntent
        )
      } else {
        AlarmManagerCompat.setExact(
          alarmManager,
          AlarmManager.RTC_WAKEUP,
          notificationDetails.millisecondsSinceEpoch,
          pendingIntent
        )
      }
      if (updateScheduledNotificationsCache) {
        saveScheduledNotification(context, notificationDetails)
      }
    }

    private fun zonedScheduleNotification(
      context: Context?,
      notificationDetails: NotificationDetails?,
      updateScheduledNotificationsCache: Boolean
    ) {
      val gson: Gson? = buildGson()
      val notificationDetailsJson: String = gson.toJson(notificationDetails)
      val notificationIntent = Intent(context, ScheduledNotificationReceiver::class.java)
      notificationIntent.putExtra(NOTIFICATION_DETAILS, notificationDetailsJson)
      val pendingIntent: PendingIntent =
        getBroadcastPendingIntent(context, notificationDetails.id, notificationIntent)
      val alarmManager: AlarmManager = getAlarmManager(context)
      val epochMilli: Long = if (VERSION.SDK_INT >= VERSION_CODES.O) ZonedDateTime.of(
        LocalDateTime.parse(notificationDetails.scheduledDateTime),
        ZoneId.of(notificationDetails.timeZoneName)
      )
        .toInstant()
        .toEpochMilli() else org.threeten.bp.ZonedDateTime.of(
        org.threeten.bp.LocalDateTime.parse(notificationDetails.scheduledDateTime),
        org.threeten.bp.ZoneId.of(notificationDetails.timeZoneName)
      )
        .toInstant()
        .toEpochMilli()
      if (BooleanUtils.getValue(notificationDetails.allowWhileIdle)) {
        AlarmManagerCompat.setExactAndAllowWhileIdle(
          alarmManager, AlarmManager.RTC_WAKEUP, epochMilli, pendingIntent
        )
      } else {
        AlarmManagerCompat.setExact(
          alarmManager,
          AlarmManager.RTC_WAKEUP,
          epochMilli,
          pendingIntent
        )
      }
      if (updateScheduledNotificationsCache) {
        saveScheduledNotification(context, notificationDetails)
      }
    }

    fun scheduleNextRepeatingNotification(
      context: Context?, notificationDetails: NotificationDetails
    ) {
      val repeatInterval = calculateRepeatIntervalMilliseconds(notificationDetails)
      val notificationTriggerTime =
        calculateNextNotificationTrigger(notificationDetails.calledAt, repeatInterval)
      val gson: Gson? = buildGson()
      val notificationDetailsJson: String = gson.toJson(notificationDetails)
      val notificationIntent = Intent(context, ScheduledNotificationReceiver::class.java)
      notificationIntent.putExtra(NOTIFICATION_DETAILS, notificationDetailsJson)
      val pendingIntent: PendingIntent =
        getBroadcastPendingIntent(context, notificationDetails.id, notificationIntent)
      val alarmManager: AlarmManager = getAlarmManager(context)
      AlarmManagerCompat.setExactAndAllowWhileIdle(
        alarmManager, AlarmManager.RTC_WAKEUP, notificationTriggerTime, pendingIntent
      )
      saveScheduledNotification(context, notificationDetails)
    }

    private fun getActivityPendingIntent(
      context: Context,
      id: Int,
      intent: Intent
    ): PendingIntent {
      var flags: Int = PendingIntent.FLAG_UPDATE_CURRENT
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
      }
      return PendingIntent.getActivity(context, id, intent, flags)
    }

    private fun getBroadcastPendingIntent(
      context: Context?,
      id: Int,
      intent: Intent
    ): PendingIntent {
      var flags: Int = PendingIntent.FLAG_UPDATE_CURRENT
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
      }
      return PendingIntent.getBroadcast(context, id, intent, flags)
    }

    private fun repeatNotification(
      context: Context,
      notificationDetails: NotificationDetails,
      updateScheduledNotificationsCache: Boolean
    ) {
      val repeatInterval = calculateRepeatIntervalMilliseconds(notificationDetails)
      var notificationTriggerTime: Long = notificationDetails.calledAt
      if (notificationDetails.repeatTime != null) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, notificationDetails.repeatTime.hour)
        calendar.set(Calendar.MINUTE, notificationDetails.repeatTime.minute)
        calendar.set(Calendar.SECOND, notificationDetails.repeatTime.second)
        if (notificationDetails.day != null) {
          calendar.set(Calendar.DAY_OF_WEEK, notificationDetails.day)
        }
        notificationTriggerTime = calendar.getTimeInMillis()
      }
      notificationTriggerTime =
        calculateNextNotificationTrigger(notificationTriggerTime, repeatInterval)
      val gson: Gson? = buildGson()
      val notificationDetailsJson: String = gson.toJson(notificationDetails)
      val notificationIntent = Intent(context, ScheduledNotificationReceiver::class.java)
      notificationIntent.putExtra(NOTIFICATION_DETAILS, notificationDetailsJson)
      val pendingIntent: PendingIntent =
        getBroadcastPendingIntent(context, notificationDetails.id, notificationIntent)
      val alarmManager: AlarmManager = getAlarmManager(context)
      if (BooleanUtils.getValue(notificationDetails.allowWhileIdle)) {
        AlarmManagerCompat.setExactAndAllowWhileIdle(
          alarmManager, AlarmManager.RTC_WAKEUP, notificationTriggerTime, pendingIntent
        )
      } else {
        alarmManager.setInexactRepeating(
          AlarmManager.RTC_WAKEUP, notificationTriggerTime, repeatInterval, pendingIntent
        )
      }
      if (updateScheduledNotificationsCache) {
        saveScheduledNotification(context, notificationDetails)
      }
    }

    private fun calculateNextNotificationTrigger(
      notificationTriggerTime: Long, repeatInterval: Long
    ): Long {
      // ensures that time is in the future
      var notificationTriggerTime = notificationTriggerTime
      val currentTime: Long = System.currentTimeMillis()
      while (notificationTriggerTime < currentTime) {
        notificationTriggerTime += repeatInterval
      }
      return notificationTriggerTime
    }

    private fun calculateRepeatIntervalMilliseconds(notificationDetails: NotificationDetails): Long {
      var repeatInterval: Long = 0
      when (notificationDetails.repeatInterval) {
        EveryMinute -> repeatInterval = 60000
        Every5Minute -> repeatInterval = (60000 * 5 ).toLong()
        Every10Minute -> repeatInterval = (60000 * 10 ).toLong()
        Every15Minute -> repeatInterval = (60000 * 15 ).toLong()
        Every20Minute -> repeatInterval = (60000 * 20 ).toLong()
        Every30Minute -> repeatInterval = (60000 * 30 ).toLong()
        Every40Minute -> repeatInterval = (60000 * 40 ).toLong()
        Every50Minute -> repeatInterval = (60000 * 50 ).toLong()
        Hourly -> repeatInterval = (60000 * 60).toLong()
        Hour2 -> repeatInterval = (60000 * 60 * 2).toLong()
        Hour4 -> repeatInterval = (60000 * 60 * 4).toLong()
        Hour6 -> repeatInterval = (60000 * 60 * 6).toLong()
        Hour12 -> repeatInterval = (60000 * 60 * 12).toLong()
        Daily -> repeatInterval = (60000 * 60 * 24).toLong()
        Weekly -> repeatInterval = (60000 * 60 * 24 * 7).toLong()
        else -> {
        }
      }
      return repeatInterval
    }

    private fun saveScheduledNotification(
      context: Context?, notificationDetails: NotificationDetails?
    ) {
      val scheduledNotifications: ArrayList<NotificationDetails> =
        loadScheduledNotifications(context)
      val scheduledNotificationsToSave: ArrayList<NotificationDetails> = ArrayList()
      for (scheduledNotification in scheduledNotifications) {
        if (scheduledNotification.id.equals(notificationDetails.id)) {
          continue
        }
        scheduledNotificationsToSave.add(scheduledNotification)
      }
      scheduledNotificationsToSave.add(notificationDetails)
      saveScheduledNotifications(context, scheduledNotificationsToSave)
    }

    private fun getDrawableResourceId(context: Context, name: String): Int {
      return context.getResources().getIdentifier(name, DRAWABLE, context.getPackageName())
    }

    @SuppressWarnings("unchecked")
    private fun castObjectToByteArray(data: Object): ByteArray {
      val byteArray: ByteArray
      // if data is deserialized by gson, it is of the wrong type and we have to convert it
      if (data is ArrayList) {
        val l: List<Double> = data as ArrayList<Double>
        byteArray = ByteArray(l.size())
        for (i in 0 until l.size()) {
          byteArray[i] = l[i].intValue() as Byte
        }
      } else {
        byteArray = data
      }
      return byteArray
    }

    private fun getBitmapFromSource(
      context: Context, data: Object, bitmapSource: BitmapSource
    ): Bitmap? {
      var bitmap: Bitmap? = null
      if (bitmapSource === BitmapSource.DrawableResource) {
        bitmap = BitmapFactory.decodeResource(
          context.getResources(), getDrawableResourceId(context, data as String)
        )
      } else if (bitmapSource === BitmapSource.FilePath) {
        bitmap = BitmapFactory.decodeFile(data as String)
      } else if (bitmapSource === BitmapSource.ByteArray) {
        val byteArray = castObjectToByteArray(data)
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
      }
      return bitmap
    }

    private fun getIconFromSource(
      context: Context,
      data: Object,
      iconSource: IconSource
    ): IconCompat? {
      var icon: IconCompat? = null
      when (iconSource) {
        DrawableResource -> icon = IconCompat.createWithResource(
          context,
          getDrawableResourceId(context, data as String)
        )
        BitmapFilePath -> icon =
          IconCompat.createWithBitmap(BitmapFactory.decodeFile(data as String))
        ContentUri -> icon = IconCompat.createWithContentUri(data as String)
        FlutterBitmapAsset -> try {
          val flutterLoader: FlutterLoader = FlutterInjector.instance().flutterLoader()
          val assetFileDescriptor: AssetFileDescriptor = context.getAssets()
            .openFd(flutterLoader.getLookupKeyForAsset(data as String))
          val fileInputStream: FileInputStream = assetFileDescriptor.createInputStream()
          icon = IconCompat.createWithBitmap(BitmapFactory.decodeStream(fileInputStream))
          fileInputStream.close()
          assetFileDescriptor.close()
        } catch (e: IOException) {
          throw RuntimeException(e)
        }
        ByteArray -> {
          val byteArray = castObjectToByteArray(data)
          icon = IconCompat.createWithData(byteArray, 0, byteArray.size)
        }
        else -> {
        }
      }
      return icon
    }

    /**
     * Sets the visibility property to the input Notification Builder
     *
     * @throws IllegalArgumentException If `notificationDetails.visibility` is not null but also not
     * matches any known index.
     */
    private fun setVisibility(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (notificationDetails.visibility == null) {
        return
      }
      val visibility: Int
      visibility = when (notificationDetails.visibility) {
        0 -> NotificationCompat.VISIBILITY_PRIVATE
        1 -> NotificationCompat.VISIBILITY_PUBLIC
        2 -> NotificationCompat.VISIBILITY_SECRET
        else -> throw IllegalArgumentException("Unknown index: " + notificationDetails.visibility)
      }
      builder.setVisibility(visibility)
    }

    private fun applyGrouping(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      var isGrouped = false
      if (!StringUtils.isNullOrEmpty(notificationDetails.groupKey)) {
        builder.setGroup(notificationDetails.groupKey)
        isGrouped = true
      }
      if (isGrouped) {
        if (BooleanUtils.getValue(notificationDetails.setAsGroupSummary)) {
          builder.setGroupSummary(true)
        }
        builder.setGroupAlertBehavior(notificationDetails.groupAlertBehavior)
      }
    }

    private fun setVibrationPattern(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (BooleanUtils.getValue(notificationDetails.enableVibration)) {
        if (notificationDetails.vibrationPattern != null
          && notificationDetails.vibrationPattern.length > 0
        ) {
          builder.setVibrate(notificationDetails.vibrationPattern)
        }
      } else {
        builder.setVibrate(longArrayOf(0))
      }
    }

    private fun setLights(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (BooleanUtils.getValue(notificationDetails.enableLights)
        && notificationDetails.ledOnMs != null && notificationDetails.ledOffMs != null
      ) {
        builder.setLights(
          notificationDetails.ledColor,
          notificationDetails.ledOnMs,
          notificationDetails.ledOffMs
        )
      }
    }

    private fun setSound(
      context: Context,
      notificationDetails: NotificationDetails,
      builder: NotificationCompat.Builder
    ) {
      if (BooleanUtils.getValue(notificationDetails.playSound)) {
        val uri: Uri? = retrieveSoundResourceUri(
          context, notificationDetails.sound, notificationDetails.soundSource
        )
        builder.setSound(uri)
      } else {
        builder.setSound(null)
      }
    }

    private fun setCategory(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (notificationDetails.category == null) {
        return
      }
      builder.setCategory(notificationDetails.category)
    }

    private fun setTimeoutAfter(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (notificationDetails.timeoutAfter == null) {
        return
      }
      builder.setTimeoutAfter(notificationDetails.timeoutAfter)
    }

    private fun getLaunchIntent(context: Context): Intent {
      val packageName: String = context.getPackageName()
      val packageManager: PackageManager = context.getPackageManager()
      return packageManager.getLaunchIntentForPackage(packageName)
    }

    private fun setStyle(
      context: Context,
      notificationDetails: NotificationDetails,
      builder: NotificationCompat.Builder
    ) {
      when (notificationDetails.style) {
        BigPicture -> setBigPictureStyle(context, notificationDetails, builder)
        BigText -> setBigTextStyle(notificationDetails, builder)
        Inbox -> setInboxStyle(notificationDetails, builder)
        Messaging -> setMessagingStyle(context, notificationDetails, builder)
        Media -> setMediaStyle(builder)
        else -> {
        }
      }
    }

    private fun setProgress(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      if (BooleanUtils.getValue(notificationDetails.showProgress)) {
        builder.setProgress(
          notificationDetails.maxProgress,
          notificationDetails.progress,
          notificationDetails.indeterminate
        )
      }
    }

    private fun setBigPictureStyle(
      context: Context,
      notificationDetails: NotificationDetails,
      builder: NotificationCompat.Builder
    ) {
      val bigPictureStyleInformation: BigPictureStyleInformation =
        notificationDetails.styleInformation as BigPictureStyleInformation
      val bigPictureStyle = BigPictureStyle()
      if (bigPictureStyleInformation.contentTitle != null) {
        val contentTitle: CharSequence =
          if (bigPictureStyleInformation.htmlFormatContentTitle) fromHtml(
            bigPictureStyleInformation.contentTitle
          ) else bigPictureStyleInformation.contentTitle
        bigPictureStyle.setBigContentTitle(contentTitle)
      }
      if (bigPictureStyleInformation.summaryText != null) {
        val summaryText: CharSequence =
          if (bigPictureStyleInformation.htmlFormatSummaryText) fromHtml(
            bigPictureStyleInformation.summaryText
          ) else bigPictureStyleInformation.summaryText
        bigPictureStyle.setSummaryText(summaryText)
      }
      if (bigPictureStyleInformation.hideExpandedLargeIcon) {
        bigPictureStyle.bigLargeIcon(null)
      } else {
        if (bigPictureStyleInformation.largeIcon != null) {
          bigPictureStyle.bigLargeIcon(
            getBitmapFromSource(
              context,
              bigPictureStyleInformation.largeIcon,
              bigPictureStyleInformation.largeIconBitmapSource
            )
          )
        }
      }
      bigPictureStyle.bigPicture(
        getBitmapFromSource(
          context,
          bigPictureStyleInformation.bigPicture,
          bigPictureStyleInformation.bigPictureBitmapSource
        )
      )
      builder.setStyle(bigPictureStyle)
    }

    private fun setInboxStyle(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      val inboxStyleInformation: InboxStyleInformation =
        notificationDetails.styleInformation as InboxStyleInformation
      val inboxStyle = InboxStyle()
      if (inboxStyleInformation.contentTitle != null) {
        val contentTitle: CharSequence =
          if (inboxStyleInformation.htmlFormatContentTitle) fromHtml(inboxStyleInformation.contentTitle) else inboxStyleInformation.contentTitle
        inboxStyle.setBigContentTitle(contentTitle)
      }
      if (inboxStyleInformation.summaryText != null) {
        val summaryText: CharSequence =
          if (inboxStyleInformation.htmlFormatSummaryText) fromHtml(inboxStyleInformation.summaryText) else inboxStyleInformation.summaryText
        inboxStyle.setSummaryText(summaryText)
      }
      if (inboxStyleInformation.lines != null) {
        for (line in inboxStyleInformation.lines) {
          inboxStyle.addLine(if (inboxStyleInformation.htmlFormatLines) fromHtml(line) else line)
        }
      }
      builder.setStyle(inboxStyle)
    }

    private fun setMediaStyle(builder: NotificationCompat.Builder) {
      val mediaStyle = MediaStyle()
      builder.setStyle(mediaStyle)
    }

    private fun setMessagingStyle(
      context: Context,
      notificationDetails: NotificationDetails,
      builder: NotificationCompat.Builder
    ) {
      val messagingStyleInformation: MessagingStyleInformation =
        notificationDetails.styleInformation as MessagingStyleInformation
      val person: Person? = buildPerson(context, messagingStyleInformation.person)
      val messagingStyle = MessagingStyle(person)
      messagingStyle.setGroupConversation(
        BooleanUtils.getValue(messagingStyleInformation.groupConversation)
      )
      if (messagingStyleInformation.conversationTitle != null) {
        messagingStyle.setConversationTitle(messagingStyleInformation.conversationTitle)
      }
      if (messagingStyleInformation.messages != null
        && !messagingStyleInformation.messages.isEmpty()
      ) {
        for (messageDetails in messagingStyleInformation.messages) {
          val message: NotificationCompat.MessagingStyle.Message =
            createMessage(context, messageDetails)
          messagingStyle.addMessage(message)
        }
      }
      builder.setStyle(messagingStyle)
    }

    private fun createMessage(
      context: Context, messageDetails: MessageDetails
    ): NotificationCompat.MessagingStyle.Message {
      val message: NotificationCompat.MessagingStyle.Message = Message(
        messageDetails.text,
        messageDetails.timestamp,
        buildPerson(context, messageDetails.person)
      )
      if (messageDetails.dataUri != null && messageDetails.dataMimeType != null) {
        message.setData(messageDetails.dataMimeType, Uri.parse(messageDetails.dataUri))
      }
      return message
    }

    private fun buildPerson(context: Context, personDetails: PersonDetails?): Person? {
      if (personDetails == null) {
        return null
      }
      val personBuilder: Person.Builder = Builder()
      personBuilder.setBot(BooleanUtils.getValue(personDetails.bot))
      if (personDetails.icon != null && personDetails.iconBitmapSource != null) {
        personBuilder.setIcon(
          getIconFromSource(context, personDetails.icon, personDetails.iconBitmapSource)
        )
      }
      personBuilder.setImportant(BooleanUtils.getValue(personDetails.important))
      if (personDetails.key != null) {
        personBuilder.setKey(personDetails.key)
      }
      if (personDetails.name != null) {
        personBuilder.setName(personDetails.name)
      }
      if (personDetails.uri != null) {
        personBuilder.setUri(personDetails.uri)
      }
      return personBuilder.build()
    }

    private fun setBigTextStyle(
      notificationDetails: NotificationDetails, builder: NotificationCompat.Builder
    ) {
      val bigTextStyleInformation: BigTextStyleInformation =
        notificationDetails.styleInformation as BigTextStyleInformation
      val bigTextStyle = BigTextStyle()
      if (bigTextStyleInformation.bigText != null) {
        val bigText: CharSequence =
          if (bigTextStyleInformation.htmlFormatBigText) fromHtml(bigTextStyleInformation.bigText) else bigTextStyleInformation.bigText
        bigTextStyle.bigText(bigText)
      }
      if (bigTextStyleInformation.contentTitle != null) {
        val contentTitle: CharSequence =
          if (bigTextStyleInformation.htmlFormatContentTitle) fromHtml(
            bigTextStyleInformation.contentTitle
          ) else bigTextStyleInformation.contentTitle
        bigTextStyle.setBigContentTitle(contentTitle)
      }
      if (bigTextStyleInformation.summaryText != null) {
        val summaryText: CharSequence =
          if (bigTextStyleInformation.htmlFormatSummaryText) fromHtml(
            bigTextStyleInformation.summaryText
          ) else bigTextStyleInformation.summaryText
        bigTextStyle.setSummaryText(summaryText)
      }
      builder.setStyle(bigTextStyle)
    }

    private fun setupNotificationChannel(
      context: Context?, notificationChannelDetails: NotificationChannelDetails
    ) {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        val notificationManager: NotificationManager =
          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
          notificationChannelDetails.id,
          notificationChannelDetails.name,
          notificationChannelDetails.importance
        )
        notificationChannel.setDescription(notificationChannelDetails.description)
        notificationChannel.setGroup(notificationChannelDetails.groupId)
        if (notificationChannelDetails.playSound) {
          val audioAttributes: AudioAttributes =
            Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
          val uri: Uri? = retrieveSoundResourceUri(
            context,
            notificationChannelDetails.sound,
            notificationChannelDetails.soundSource
          )
          notificationChannel.setSound(uri, audioAttributes)
        } else {
          notificationChannel.setSound(null, null)
        }
        notificationChannel.enableVibration(
          BooleanUtils.getValue(notificationChannelDetails.enableVibration)
        )
        if (notificationChannelDetails.vibrationPattern != null
          && notificationChannelDetails.vibrationPattern.length > 0
        ) {
          notificationChannel.setVibrationPattern(notificationChannelDetails.vibrationPattern)
        }
        val enableLights: Boolean =
          BooleanUtils.getValue(notificationChannelDetails.enableLights)
        notificationChannel.enableLights(enableLights)
        if (enableLights && notificationChannelDetails.ledColor != null) {
          notificationChannel.setLightColor(notificationChannelDetails.ledColor)
        }
        notificationChannel.setShowBadge(BooleanUtils.getValue(notificationChannelDetails.showBadge))
        notificationManager.createNotificationChannel(notificationChannel)
      }
    }

    private fun retrieveSoundResourceUri(
      context: Context?, sound: String, soundSource: SoundSource?
    ): Uri? {
      var uri: Uri? = null
      if (StringUtils.isNullOrEmpty(sound)) {
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      } else {
        // allow null as soundSource was added later and prior to that, it was assumed to be a raw
        // resource
        if (soundSource == null || soundSource === SoundSource.RawResource) {
          uri = Uri.parse(
            "android.resource://" + context.getPackageName()
              .toString() + "/raw/" + sound
          )
        } else if (soundSource === SoundSource.Uri) {
          uri = Uri.parse(sound)
        }
      }
      return uri
    }

    private fun getAlarmManager(context: Context?): AlarmManager {
      return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun isValidDrawableResource(
      context: Context?, name: String?, result: Result, errorCode: String
    ): Boolean {
      val resourceId: Int =
        context.getResources().getIdentifier(name, DRAWABLE, context.getPackageName())
      if (resourceId == 0) {
        result.error(
          errorCode,
          String.format(INVALID_DRAWABLE_RESOURCE_ERROR_MESSAGE, name),
          null
        )
        return false
      }
      return true
    }

    fun showNotification(context: Context, notificationDetails: NotificationDetails) {
      val notification: Notification = createNotification(context, notificationDetails)
      val notificationManagerCompat: NotificationManagerCompat =
        getNotificationManager(context)
      if (notificationDetails.tag != null) {
        notificationManagerCompat.notify(
          notificationDetails.tag, notificationDetails.id, notification
        )
      } else {
        notificationManagerCompat.notify(notificationDetails.id, notification)
      }
    }

    fun zonedScheduleNextNotification(
      context: Context?, notificationDetails: NotificationDetails
    ) {
      initAndroidThreeTen(context)
      val nextFireDate = getNextFireDate(
        notificationDetails
      )
        ?: return
      notificationDetails.scheduledDateTime = nextFireDate
      zonedScheduleNotification(context, notificationDetails, true)
    }

    fun zonedScheduleNextNotificationMatchingDateComponents(
      context: Context?, notificationDetails: NotificationDetails
    ) {
      initAndroidThreeTen(context)
      val nextFireDate = getNextFireDateMatchingDateTimeComponents(
        notificationDetails
      )
        ?: return
      notificationDetails.scheduledDateTime = nextFireDate
      zonedScheduleNotification(context, notificationDetails, true)
    }

    fun getNextFireDate(notificationDetails: NotificationDetails): String? {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        if (notificationDetails.scheduledNotificationRepeatFrequency
          === ScheduledNotificationRepeatFrequency.Daily
        ) {
          val localDateTime: LocalDateTime =
            LocalDateTime.parse(notificationDetails.scheduledDateTime).plusDays(1)
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime)
        } else if (notificationDetails.scheduledNotificationRepeatFrequency
          === ScheduledNotificationRepeatFrequency.Weekly
        ) {
          val localDateTime: LocalDateTime =
            LocalDateTime.parse(notificationDetails.scheduledDateTime).plusWeeks(1)
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime)
        }
      } else {
        if (notificationDetails.scheduledNotificationRepeatFrequency
          === ScheduledNotificationRepeatFrequency.Daily
        ) {
          val localDateTime: LocalDateTime =
            org.threeten.bp.LocalDateTime.parse(notificationDetails.scheduledDateTime)
              .plusDays(1)
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            localDateTime
          )
        } else if (notificationDetails.scheduledNotificationRepeatFrequency
          === ScheduledNotificationRepeatFrequency.Weekly
        ) {
          val localDateTime: LocalDateTime =
            org.threeten.bp.LocalDateTime.parse(notificationDetails.scheduledDateTime)
              .plusWeeks(1)
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            localDateTime
          )
        }
      }
      return null
    }

    fun getNextFireDateMatchingDateTimeComponents(notificationDetails: NotificationDetails): String? {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        val zoneId: ZoneId = ZoneId.of(notificationDetails.timeZoneName)
        val scheduledDateTime: ZonedDateTime = ZonedDateTime.of(
          LocalDateTime.parse(notificationDetails.scheduledDateTime),
          zoneId
        )
        val now: ZonedDateTime = ZonedDateTime.now(zoneId)
        var nextFireDate: ZonedDateTime = ZonedDateTime.of(
          now.getYear(),
          now.getMonthValue(),
          now.getDayOfMonth(),
          scheduledDateTime.getHour(),
          scheduledDateTime.getMinute(),
          scheduledDateTime.getSecond(),
          scheduledDateTime.getNano(),
          zoneId
        )
        while (nextFireDate.isBefore(now)) {
          // adjust to be a date in the future that matches the time
          nextFireDate = nextFireDate.plusDays(1)
        }
        if (notificationDetails.matchDateTimeComponents === DateTimeComponents.Time) {
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(nextFireDate)
        } else if (notificationDetails.matchDateTimeComponents
          === DateTimeComponents.DayOfWeekAndTime
        ) {
          while (nextFireDate.getDayOfWeek() !== scheduledDateTime.getDayOfWeek()) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(nextFireDate)
        } else if (notificationDetails.matchDateTimeComponents
          === DateTimeComponents.DayOfMonthAndTime
        ) {
          while (nextFireDate.getDayOfMonth() !== scheduledDateTime.getDayOfMonth()) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(nextFireDate)
        } else if (notificationDetails.matchDateTimeComponents === DateTimeComponents.DateAndTime) {
          while (nextFireDate.getMonthValue() !== scheduledDateTime.getMonthValue()
            || nextFireDate.getDayOfMonth() !== scheduledDateTime.getDayOfMonth()
          ) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(nextFireDate)
        }
      } else {
        val zoneId: ZoneId = org.threeten.bp.ZoneId.of(notificationDetails.timeZoneName)
        val scheduledDateTime: ZonedDateTime = org.threeten.bp.ZonedDateTime.of(
          org.threeten.bp.LocalDateTime.parse(notificationDetails.scheduledDateTime),
          zoneId
        )
        val now: ZonedDateTime = org.threeten.bp.ZonedDateTime.now(zoneId)
        var nextFireDate: ZonedDateTime = org.threeten.bp.ZonedDateTime.of(
          now.getYear(),
          now.getMonthValue(),
          now.getDayOfMonth(),
          scheduledDateTime.getHour(),
          scheduledDateTime.getMinute(),
          scheduledDateTime.getSecond(),
          scheduledDateTime.getNano(),
          zoneId
        )
        while (nextFireDate.isBefore(now)) {
          // adjust to be a date in the future that matches the time
          nextFireDate = nextFireDate.plusDays(1)
        }
        if (notificationDetails.matchDateTimeComponents === DateTimeComponents.Time) {
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            nextFireDate
          )
        } else if (notificationDetails.matchDateTimeComponents
          === DateTimeComponents.DayOfWeekAndTime
        ) {
          while (nextFireDate.getDayOfWeek() !== scheduledDateTime.getDayOfWeek()) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            nextFireDate
          )
        } else if (notificationDetails.matchDateTimeComponents
          === DateTimeComponents.DayOfMonthAndTime
        ) {
          while (nextFireDate.getDayOfMonth() !== scheduledDateTime.getDayOfMonth()) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            nextFireDate
          )
        } else if (notificationDetails.matchDateTimeComponents === DateTimeComponents.DateAndTime) {
          while (nextFireDate.getMonthValue() !== scheduledDateTime.getMonthValue()
            || nextFireDate.getDayOfMonth() !== scheduledDateTime.getDayOfMonth()
          ) {
            nextFireDate = nextFireDate.plusDays(1)
          }
          return org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
            nextFireDate
          )
        }
      }
      return null
    }

    private fun getNotificationManager(context: Context?): NotificationManagerCompat {
      return NotificationManagerCompat.from(context)
    }

    private fun launchedActivityFromHistory(intent: Intent?): Boolean {
      return (intent != null
              && intent.getFlags() and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
              === Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
    }
  }
}