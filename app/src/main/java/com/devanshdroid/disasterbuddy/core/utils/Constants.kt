package com.devanshdroid.disasterbuddy.core.utils


object Constants {

    // --- Backend Base URLs (replace with your Render URLs after deployment) ---
    const val AUTH_BASE_URL = "https://your-auth-service.onrender.com/"
    const val ALERT_BASE_URL = "https://your-alert-service.onrender.com/"
    const val HELP_REQUEST_BASE_URL = "https://your-helprequest-service.onrender.com/"

    // --- Shared Preference Keys ---
    const val PREF_NAME = "disaster_buddy_prefs"
    const val KEY_JWT_TOKEN = "jwt_token"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_FCM_TOKEN = "fcm_token"

    // --- User Roles ---
    const val ROLE_CITIZEN = "citizen"
    const val ROLE_VOLUNTEER = "volunteer"

    // --- Request Status ---
    const val STATUS_PENDING = "Pending"
    const val STATUS_ACCEPTED = "Accepted"
    const val STATUS_RESOLVED = "Resolved"

    // --- Notification Channels ---
    const val CHANNEL_ID_ALERTS = "disaster_alerts"
    const val CHANNEL_NAME_ALERTS = "Disaster Alerts"
    const val CHANNEL_ID_REQUESTS = "help_requests"
    const val CHANNEL_NAME_REQUESTS = "Help Requests"

    // --- Notification Types (from FCM payload) ---
    const val NOTIF_TYPE_ALERT = "disaster_alert"
    const val NOTIF_TYPE_REQUEST_UPDATE = "request_update"

    // --- Location ---
    const val LOCATION_UPDATE_INTERVAL = 10000L    // 10 seconds
    const val LOCATION_FASTEST_INTERVAL = 5000L    // 5 seconds
    const val NEARBY_RADIUS_KM = 10.0
}