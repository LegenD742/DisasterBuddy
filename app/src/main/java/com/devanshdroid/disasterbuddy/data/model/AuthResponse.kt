package com.devanshdroid.disasterbuddy.data.model


data class AuthResponse(
    val token: String = "",
    val user: User = User(),
    val message: String = ""
)

data class GeneralResponse(
    val message: String = "",
    val success: Boolean = false
)

data class HelpRequestResponse(
    val message: String = "",
    val request: HelpRequest = HelpRequest()
)

data class AlertsResponse(
    val alerts: List<Alert> = emptyList()
)

data class RequestsResponse(
    val requests: List<HelpRequest> = emptyList()
)