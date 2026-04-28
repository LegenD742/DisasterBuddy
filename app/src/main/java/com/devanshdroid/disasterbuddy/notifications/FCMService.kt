package com.devanshdroid.disasterbuddy.notifications

import com.devanshdroid.disasterbuddy.core.utils.Constants
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferenceManager.fcmToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "Disaster Buddy"

        val body = message.notification?.body
            ?: message.data["body"]
            ?: "You have a new notification"

        val type = message.data["type"] ?: Constants.NOTIF_TYPE_ALERT

        when (type) {
            Constants.NOTIF_TYPE_ALERT ->
                notificationHelper.showDisasterAlertNotification(title, body)
            Constants.NOTIF_TYPE_REQUEST_UPDATE ->
                notificationHelper.showRequestUpdateNotification(title, body)
            else ->
                notificationHelper.showDisasterAlertNotification(title, body)
        }
    }
}