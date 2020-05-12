package jp.shiita.websockettest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService

class NotificationHelper(
    private val context: Context,
    private val manager: NotificationManager = context.getSystemService()!!
) {

    fun createNotification(notificationId: Int, title: String, text: String) {
        createChannel()

        val intent = PendingIntent.getActivity(
            context,
            PUSH_REQUEST_CODE,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, PUSH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()
        manager.notify(notificationId, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (PUSH_CHANNEL_ID in manager.notificationChannels.map(NotificationChannel::getId)) return

            val channel = NotificationChannel(
                PUSH_CHANNEL_ID,
                PUSH_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = PUSH_CHANNEL_DESCRIPTION
            }
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val PUSH_CHANNEL_ID = "pushChannelId"
        const val PUSH_CHANNEL_NAME = "Push通知"
        const val PUSH_CHANNEL_DESCRIPTION = "Push通知"
        const val PUSH_REQUEST_CODE = 1000
    }
}