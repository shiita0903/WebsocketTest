package jp.shiita.websockettest

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d("Received message")

        val data = message.data
        val notificationId = data[KEY_NOTIFICATION_ID]?.toIntOrNull() ?: return
        val title = data.getOrElse(KEY_TITLE) { "" }
        val text = data.getOrElse(KEY_TEXT) { "" }

        Timber.d("id = $notificationId, title = $title, text = $text")
        NotificationHelper(this).createNotification(
            notificationId,
            title,
            text
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("new token = $token")
    }

    companion object {
        private const val KEY_NOTIFICATION_ID = "notification_id"
        private const val KEY_TITLE = "title"
        private const val KEY_TEXT = "text"
    }
}
