package com.kaycloud.framework

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build

/**
 * Created by jiangyunkai on 2019/11/20
 */
internal class NotificationHelper(ctx: Context) : ContextWrapper(ctx) {

    private val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        val PRIMARY_CHANNEL = "default"
        val SECONDARY_CHANNEL = "second"
    }

    private val smallIcon: Int
        get() = android.R.drawable.stat_notify_chat

    fun notify(id: Int, notification: Notification.Builder) {
        manager.notify(id, notification.build())
    }

    fun getNotification2(title: String, body: String): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(applicationContext, SECONDARY_CHANNEL)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
        } else {
            TODO("VERSION.SDK_INT < O")

        }
    }

}