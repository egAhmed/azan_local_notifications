package com.ahmed.azanlocalnotifications

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import java.util.ArrayList

class ForegroundService : Service() {
    @Override
    fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val parameter: ForegroundServiceStartParameter =
            intent.getSerializableExtra(ForegroundServiceStartParameter.EXTRA) as ForegroundServiceStartParameter
        val notification: Notification =
            FlutterLocalNotificationsPlugin.createNotification(this, parameter.notificationData)
        if (parameter.foregroundServiceTypes != null
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        ) {
            startForeground(
                parameter.notificationData.id,
                notification,
                orCombineFlags(parameter.foregroundServiceTypes)
            )
        } else {
            startForeground(parameter.notificationData.id, notification)
        }
        return parameter.startMode
    }

    @Override
    fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private fun orCombineFlags(flags: ArrayList<Integer>): Int {
            var flag: Int = flags.get(0)
            for (i in 1 until flags.size()) {
                flag = flag or flags.get(i)
            }
            return flag
        }
    }
}