package com.ahmed.azanlocalnotifications

import com.dexterous.flutterlocalnotifications.models.NotificationDetails
import java.io.Serializable
import java.util.ArrayList

class ForegroundServiceStartParameter(
    notificationData: NotificationDetails,
    startMode: Int,
    foregroundServiceTypes: ArrayList<Integer?>
) : Serializable {
    val notificationData: NotificationDetails
    val startMode: Int
    val foregroundServiceTypes: ArrayList<Integer>

    @Override
    override fun toString(): String {
        return ("ForegroundServiceStartParameter{"
                + "notificationData="
                + notificationData
                + ", startMode="
                + startMode
                + ", foregroundServiceTypes="
                + foregroundServiceTypes
                + '}')
    }

    companion object {
        const val EXTRA = "com.dexterous.flutterlocalnotifications.ForegroundServiceStartParameter"
    }

    init {
        this.notificationData = notificationData
        this.startMode = startMode
        this.foregroundServiceTypes = foregroundServiceTypes
    }
}