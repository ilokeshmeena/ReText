package com.rexvit.retext.services

import android.content.ComponentName
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.rexvit.retext.utility.AppUtils
import com.rexvit.retext.data.database.DatabaseHelper
import com.rexvit.retext.data.model.DeletedNotification

class NotificationListener : NotificationListenerService() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate() {
        super.onCreate()
        dbHelper = DatabaseHelper(this)
        startForegroundService()
    }

    private fun startForegroundService() {
        val notification = NotificationHelper.createNotification(
            this,
            "ReText is monitoring notifications",
            "Tap to open app"
        )
        startForeground(1, notification)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn?.let { notification ->
            val packageName = notification.packageName
            if (dbHelper.isAppSelected(packageName)) {
                val notificationText = notification.notification?.extras?.getCharSequence("android.text")?.toString() ?: ""
                val notificationTitle = notification.notification?.extras?.getCharSequence("android.title")?.toString() ?: ""

                val deletedNotification = DeletedNotification(
                    packageName = packageName,
                    appName = AppUtils.getAppName(this, packageName),
                    title = notificationTitle,
                    text = notificationText,
                    timestamp = System.currentTimeMillis()
                )

                dbHelper.saveDeletedNotification(deletedNotification)
                Log.d("ReText", "Saved deleted notification from $packageName")
            }
        }
    }

    override fun onListenerDisconnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestRebind(ComponentName(this, NotificationListener::class.java))
        }
    }
}